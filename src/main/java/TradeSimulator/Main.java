package TradeSimulator;

import Model.Trade;
import Service.AccountInfo;
import Service.CloseTrade;
import Service.GetStockData;
import Service.OpenTrade;
import Util.TokenHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    static Logger logger = LogManager.getLogger(Main.class);

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

        //Jakson object
        ObjectMapper mapper = new ObjectMapper();

        //Making a logger instance

        logger.info("Application Started");


        //============================================ ACCOUNT ============================================

        //Endpoint /ts/login/
        app.post("/ts/login/", context -> {

            if (info.authentication(context.header("username"), context.header("password"))){
                logger.info("USER "+context.header("username"));
                context.status(200);
                context.result(tokenHandler.encoder(context.header("username")));
            }
            else {
                context.status(401);
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
            }
        });

        //Endpoint /ts/user/ID/
        app.get("/ts/user/id/", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if ( info.checkUsername(username)){
                context.result(String.valueOf(info.getID(username)));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/user/balance/
        app.get("/ts/user/balance/", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.result(String.valueOf(info.getTotalBalance(username)));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint /ts/user/buyingpower/
        app.get("/ts/user/power/", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.result(String.valueOf(info.getBuyingPower(username)));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //============================================ OPEN TRADE ============================================

        //Endpoint /ts/opentrade/open
        app.post("/ts/opentrade/open" , context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            Trade trade = mapper.readValue(context.body(), Trade.class);
            System.out.println(trade.getSymbol()+trade.getNumOfShares()+trade.getOrderType());
            if (info.checkUsername(username)){
                if (open.newTrade(username, trade)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint /ts/opentrade/get/latest
        app.get("/ts/opentrade/get/latest", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            if (info.checkUsername(username)){
                context.json(open.getLatestTrade(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/opentrade/get/tradeid
        app.get("/ts/opentrade/get/tradeid", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            if (info.checkUsername(username)){
                context.json(open.getOpenTradeID(username, tradeID));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }


        });

        //Endpoint /ts/opentrade/get/ordertype
        app.get("/ts/opentrade/get/ordertype", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            String orderType = context.header("orderType");
            if (info.checkUsername(username)){
                context.json(open.getTradeOrderType(username, orderType));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/opentrade/get/winners
        app.get("/ts/opentrade/get/winners", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            if (info.checkUsername(username)){
                context.json(open.getOpenTradeWinners(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint /ts/opentrade/get/losers
        app.get("/ts/opentrade/get/losers", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            if (info.checkUsername(username)){
                context.json(open.getOpenTradeLosers(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/opentrade/get/symbol
        app.get("/ts/opentrade/get/symbol", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            String[] symbol = context.header("symbol").split(",");
            if (info.checkUsername(username)){
                context.json(open.getOpenTradeSymbol(username, symbol));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/opentrade/get/all
        app.get("/ts/opentrade/get/all", context -> {
            String username = (tokenHandler.verify(context.header("jwtKey")));
            if (info.checkUsername(username)){
                context.json(open.getAllOpenTrades(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //============================================ CLOSE TRADE ============================================

        //Endpoint /ts/closetrade/close/latest
        app.delete("/ts/closetrade/close/latest", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                if (close.closeLatestTrade(username)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/tradeid
        app.delete("/ts/closetrade/close/tradeid", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            if (info.checkUsername(username)){
                if (close.closeTradeID(username, tradeID)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/winners
        app.delete("/ts/closetrade/close/winners", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                if (close.closeWinners(username)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/losers
        app.delete("/ts/closetrade/close/losers", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                if (close.closeLosers(username)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/ordertype
        app.delete("/ts/closetrade/close/ordertype", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            String orderType = context.header("orderType");
            if (info.checkUsername(username)){
                if (close.closeOrderType(username, orderType)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/symbol
        app.delete("/ts/closetrade/close/symbol", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            String[] symbol = context.header("symbol").split(",");
            if (info.checkUsername(username)){
                if (close.closeSymbol(username, symbol)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/close/all
        app.delete("/ts/closetrade/close/all", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                if (close.closeAllTrades(username)){
                    context.result("true");
                }
                else{
                    context.result("false");
                }
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });


        //====================================== GET CLOSED TRADES ======================================

        //Endpoint /ts/closetrade/get/latest
        app.get("/ts/closetrade/get/latest", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(close.getLatestCloseTrade(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });
        
        //Endpoint /ts/closetrade/get/tradeid
        app.get("/ts/closetrade/get/tradeid", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            int tradeID = Integer.parseInt(context.header("tradeID"));
            if (info.checkUsername(username)){
                context.json(close.getCloseTradeID(username, tradeID));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/get/winners
        app.get("/ts/closetrade/get/winners", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(close.getCloseTradeWinners(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/get/losers
        app.get("/ts/closetrade/get/losers", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(close.getCloseTradeLosers(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/get/ordertype
        app.get("/ts/closetrade/get/ordertype", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            String orderType = context.header("orderType");
            if (info.checkUsername(username)){
                context.json(close.getCloseTradeOrderType(username, orderType));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //Endpoint /ts/closetrade/get/symbol
        app.get("/ts/closetrade/get/symbol", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            String[] symbol = context.header("symbol").split(",");
            if (info.checkUsername(username)){
                context.json(close.getCloseTradeSymbol(username, symbol));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }


        });

        //Endpoint /ts/closetrade/get/all
        app.get("/ts/closetrade/get/all", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(close.getAllCloseTrades(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //====================================== GET QUOTES ======================================
        
        //Endpoint ts/quotes/stocks/{symbol}
        app.get("/ts/quotes/stocks/", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                GetStockData stockData = new GetStockData((context.queryParam("symbol")));
                context.json(stockData.getQuote());//.header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }

        });

        //====================================== GET STATS ======================================

        //Endpoint ts/stats/opentrades
        app.get("/ts/stats/opentrades", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(open.getOpenStats(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint ts/stats/closetrades
        app.get("/ts/stats/closetrades", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.json(close.getCloseStats(username));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint ts/stats/openpl
        app.get("/ts/stats/openpl", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                context.result(String.valueOf(open.getOpenPL(username)));
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });

        //Endpoint to update the records
        app.put("/ts/update", context -> {
            String username = tokenHandler.verify(context.header("jwtKey"));
            if (info.checkUsername(username)){
                open.refreshPL(username);
            }
            else{
                context.status(401);
                context.result("Not Authorized");
            }
        });
    }
}