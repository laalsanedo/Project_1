package TradeSimulator;

import Model.Trade;
import Service.AccountInfo;
import Service.CloseTrade;
import Service.GetStockData;
import Service.OpenTrade;
import Util.TokenHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.core.util.Header;

public class Main {
    public static void main(String[] args) {
        //Creates an account service --> used for getting user related data.
        AccountInfo info = new AccountInfo();

        //Creates a closeTrade object --> used for closing trades and other closed trade related data.
        CloseTrade close = new CloseTrade();

        //Creates an openTrade object --> used for opening trades and other open trade related data.
        OpenTrade open = new OpenTrade();

        //Create a tokenHandler that encodes and decodes the JWT
        TokenHandler tokenHandler = new TokenHandler();

        //Creates a web server
        Javalin app = Javalin.create(config -> {
                config.enableCorsForAllOrigins();
        }).start(9000);

        //============================================ ACCOUNT ============================================

        //Endpoint /ts/login/
        app.get("/ts/login/", context -> {
            if (info.authentication(context.header("username"), context.header("password"))){
                context.status(200);
                context.cookieStore("jwtKey", tokenHandler.encoder(context.header("username")));
            }
            else {
                context.status(401);
                context.result("username and/or password did not match. Please try again or Create a new account.");
            }
        });

        //Endpoint /ts/login/newuser/
        app.post("/ts/login/newuser/", context -> {
            if(info.newUser(context.header("username"), context.header("password"))){
                context.status(200);
                context.cookieStore("jwtKey", tokenHandler.encoder(context.header("username")));
            }
            else{
                context.status(401);
                context.result("username already exists. Try another username or login to the account.");
            }
        });

        //Endpoint /ts/user/ID/
        app.get("/ts/user/id/", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.result(String.valueOf(info.getID(username)));
        });

        //Endpoint /ts/user/balance/
        app.get("/ts/user/balance/", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            System.out.println(username);
            context.result(String.valueOf(info.getTotalBalance(username)));
        });

        //Endpoint /ts/user/buyingpower/
        app.get("/ts/user/power/", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.result(String.valueOf(info.getBuyingPower(username)));
        });

        //============================================ OPEN TRADE ============================================

        //Endpoint /ts/opentrade/open
        app.post("/ts/opentrade/open" , context -> {
            ObjectMapper mapper = new ObjectMapper();
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            Trade trade = mapper.readValue(context.body(), Trade.class);
            if (open.newTrade(username, trade)){
                context.json(open.getLatestTrade(username));
            }
            else{
                context.result("Something went wrong while opening a trade");
            }
        });

        //Endpoint /ts/opentrade/get/latest
        app.get("/ts/opentrade/get/latest", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            context.json(open.getLatestTrade(username));
        });

        //Endpoint /ts/opentrade/get/tradeid
        app.get("/ts/opentrade/get/tradeid", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            context.json(open.getOpenTradeID(username, tradeID));
        });

        //Endpoint /ts/opentrade/get/ordertype
        app.get("/ts/opentrade/get/ordertype", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            String orderType = context.header("orderType");
            context.json(open.getTradeOrderType(username, orderType));
        });

        //Endpoint /ts/opentrade/get/winners
        app.get("/ts/opentrade/get/winners", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            context.json(open.getOpenTradeWinners(username));
        });

        //Endpoint /ts/opentrade/get/losers
        app.get("/ts/opentrade/get/losers", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            context.json(open.getOpenTradeLosers(username));
        });

        //Endpoint /ts/opentrade/get/symbol
        app.get("/ts/opentrade/get/symbol", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            String[] symbol = context.header("symbol").split(",");
            context.json(open.getOpenTradeSymbol(username, symbol));
        });

        //Endpoint /ts/opentrade/get/all
        app.get("/ts/opentrade/get/all", context -> {
            String username = (tokenHandler.verify(context.cookieStore("jwtKey")));
            context.json(open.getAllOpenTrades(username));
        });

        //============================================ CLOSE TRADE ============================================

        //Endpoint /ts/closetrade/close/latest
        app.delete("/ts/closetrade/close/latest", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            if (close.closeLatestTrade(username)){
                context.json(close.getLatestCloseTrade(username));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/tradeid
        app.delete("/ts/closetrade/close/tradeid", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            if (close.closeTradeID(username, tradeID)){
                context.json(close.getCloseTradeID(username, tradeID));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/winners
        app.delete("/ts/closetrade/close/winners", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            if (close.closeWinners(username)){
                context.json(close.getCloseTradeWinners(username));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/losers
        app.delete("/ts/closetrade/close/losers", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            if (close.closeLosers(username)){
                context.json(close.getCloseTradeLosers(username));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/ordertype
        app.delete("/ts/closetrade/close/ordertype", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            String orderType = context.header("orderType");
            if (close.closeOrderType(username, orderType)){
                context.json(close.getCloseTradeOrderType(username, orderType));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/symbol
        app.delete("/ts/closetrade/close/symbol", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            String[] symbol = context.header("symbol").split(",");
            if (close.closeSymbol(username, symbol)){
                context.json(close.getCloseTradeSymbol(username, symbol));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });

        //Endpoint /ts/closetrade/close/all
        app.delete("/ts/closetrade/close/all", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            if (close.closeAllTrades(username)){
                context.json(close.getAllCloseTrades(username));
            }
            else{
                System.out.println("something went wrong while close the trade");
            }
        });


        //====================================== GET CLOSED TRADES ======================================

        //Endpoint /ts/closetrade/get/latest
        app.get("/ts/closetrade/get/latest", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(close.getLatestCloseTrade(username));
        });
        
        //Endpoint /ts/closetrade/get/tradeid
        app.get("/ts/closetrade/get/tradeid", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            context.json(close.getCloseTradeID(username, tradeID));
        });

        //Endpoint /ts/closetrade/get/winners
        app.get("/ts/closetrade/get/winners", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(close.getCloseTradeWinners(username));
        });

        //Endpoint /ts/closetrade/get/losers
        app.get("/ts/closetrade/get/losers", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(close.getCloseTradeLosers(username));
        });

        //Endpoint /ts/closetrade/get/ordertype
        app.get("/ts/closetrade/get/ordertype", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            String orderType = context.header("orderType");
            context.json(close.getCloseTradeOrderType(username, orderType));
        });

        //Endpoint /ts/closetrade/get/symbol
        app.get("/ts/closetrade/get/symbol", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            String[] symbol = context.header("symbol").split(",");
            context.json(close.getCloseTradeSymbol(username, symbol));
        });

        //Endpoint /ts/closetrade/get/all
        app.get("/ts/closetrade/get/all", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(close.getAllCloseTrades(username));
        });

        //====================================== GET QUOTES ======================================
        
        //Endpoint ts/quotes/stocks/{symbol}
        app.get("/ts/quotes/stocks/", context -> {
            //String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            GetStockData stockData = new GetStockData(context.queryParam("symbol"));
            context.json(stockData.getQuote()).header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        });

        //====================================== GET STATS ======================================

        //Endpoint ts/stats/opentrades
        app.get("ts/stats/opentrades", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(open.getOpenStats(username));
        });

        //Endpoint ts/stats/closetrades
        app.get("ts/stats/closetrades", context -> {
            String username = tokenHandler.verify(context.cookieStore("jwtKey"));
            context.json(close.getCloseStats(username));
        });
    }
}