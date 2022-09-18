package DAO;

import Model.Trade;
import Service.GetStockData;
import Util.Connectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradeOpen {
    private Connection connection;
    private ResultSet resultSet;
    private UserInfo userInfo;
    private Trade trade;
    private ArrayList<Trade> list;

    public TradeOpen()  {
        userInfo = new UserInfo();
        connection = Connectivity.databaseConnect();
    }

    //=================================== CREATE A NEW TRADE ===================================

    public boolean newTrade(Trade trade)  {
        String query = "INSERT INTO trade_open( order_type, symbol, no_of_shares, entry_price, current_price, p_l, user_id) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        GetStockData stockData = new GetStockData(trade.getSymbol());
        int ID = trade.getUserID();
        if (trade.getOrderType().equals("buy")){
            if (!userInfo.enoughBalance(ID, (stockData.getBidPrice()*trade.getNumOfShares()))){
                return false;
            }
        }
        else{
            if (!userInfo.enoughBalance(trade.getUserID(), (stockData.getAskPrice()*trade.getNumOfShares()))){
                return false;
            }
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trade.getOrderType());
            preparedStatement.setString(2, trade.getSymbol());
            preparedStatement.setInt(3, trade.getNumOfShares());
            
            if (trade.getOrderType().equals("buy")){
                preparedStatement.setDouble(4, stockData.getBidPrice());
                preparedStatement.setDouble(5, stockData.getCurrentPrice()); //CHange to current price
                preparedStatement.setDouble(6, trade.getPL());
                preparedStatement.setInt(7, trade.getUserID());
                userInfo.updatingBuyingPower(userInfo.getUsername(trade.getUserID()), (stockData.getBidPrice()*trade.getNumOfShares()), 0, 1);
                return preparedStatement.executeUpdate() >= 1;
            }
            else{
                preparedStatement.setDouble(4, stockData.getAskPrice());
                preparedStatement.setDouble(5, stockData.getCurrentPrice()); //CHange to current price
                preparedStatement.setDouble(6, trade.getPL());
                preparedStatement.setInt(7, trade.getUserID());
                userInfo.updatingBuyingPower(userInfo.getUsername(trade.getUserID()), (stockData.getAskPrice()*trade.getNumOfShares()), 0, 1);
                return preparedStatement.executeUpdate() >= 1;
            }
        }catch (SQLException e){
            System.out.println("something went wrong while creating a new trade.\n"+e);
        }
        return false;
    }

    //=================================== GETS OPEN TRADES ===================================

    //Get the lasted trade that was opened
    public Trade getLatestTrade(String username)  {

        trade = new Trade();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND trade_id = (SELECT max(trade_id) FROM trade_open WHERE user_id = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.setInt(2, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: latest.\n"+e);
        }
        return trade;

    }

    //Get an open trade based on a tradeID
    public Trade getOpenTradeID(String username, int tradeID)  {

        trade = new Trade();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND trade_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.setInt(2, tradeID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: tradeID.\n"+e);
        }

        return trade;

    }

    //Get the open trade(s) based on a orderTpe.
    public ArrayList<Trade> getTradeOrderType(String username, String orderType)  {

        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND order_type = ? ORDER BY trade_id ASC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.setString(2, orderType);
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in an arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: orderType.\n"+e);
        }
        return list;
    }

    //Get the open trade(s) based on a Winners.
    public ArrayList<Trade> getOpenTradeWinners(String username)  {

        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND p_l > 0 ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in an arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: winners.\n"+e);
        }

        return list;
    }

    //Get the open trade(s) based on a Losers.
    public ArrayList<Trade> getCloseTradeLosers(String username)  {

        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND p_l < 0 ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in an arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: losers.\n"+e);
        }
        return list;
    }

    //Get the open trade(s) based on a symbol(s).
    public ArrayList<Trade> getOpenTradeSymbol(String username, String... symbols)  {

        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_open WHERE user_id = ? AND symbol = ? ORDER BY trade_id ASC";
        try {
            for (String symbol : symbols){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userInfo.getID(username));
                preparedStatement.setString(2, symbol);
                resultSet = preparedStatement.executeQuery();
                //Stores each trade in result set inside the trade object which in turn is store in an arraylist of type Trade.
                while(resultSet.next()){
                    trade.setTradeID(resultSet.getInt(1));
                    trade.setOrderType(resultSet.getString(2));
                    trade.setSymbol(resultSet.getString(3));
                    trade.setNumOfShares(resultSet.getInt(4));
                    trade.setEntryPrice(resultSet.getDouble(5));
                    trade.setCurrentPrice(resultSet.getDouble(6));
                    trade.setPL(resultSet.getDouble(7));
                    trade.setUserID(resultSet.getInt(8));
                    list.add(trade);
                    trade = new Trade();
                }
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: Symbols.\n"+e);
        }

        return list;
    }

    //Get all the trades that the user currently has open in descending order
    public ArrayList<Trade> getAllOpenTrades(String username)  {
        
        trade = new Trade();
        list = new ArrayList<>();

        String query = "SELECT * FROM trade_open WHERE user_id = ? ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in an arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setCurrentPrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade: AllOpenTrades.\n"+e);
        }
        return list;
    }


    //=================================== DELETES OPEN TRADES ===================================

    //Delete a single trade
    public boolean deleteTrade(String username, Trade trade)  {
        String query = "DELETE FROM trade_open WHERE trade_id = ? and user_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, trade.getTradeID());
            preparedStatement.setInt(2, trade.getUserID());
            return preparedStatement.executeUpdate() >= 1;
        }catch (SQLException e){
            System.out.println("something went wrong while deleting a trade");
        }
        return false;
    }

    //Delete a multiple trade
    public boolean deleteTrades(String username, ArrayList<Trade> trades)  {
        String query = "DELETE FROM trade_open WHERE trade_id = ? and user_id = ?";
        try{
            for (Trade trade1 : trades){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, trade1.getTradeID());
                preparedStatement.setInt(2, trade1.getUserID());
                preparedStatement.executeUpdate();
            }
            return true;
        }catch (SQLException e){
            System.out.println("something went wrong while deleting trades");
        }
        return  false;
    }

    //=================================== UPDATE OPEN TRADES ===================================

    //Update the current price
    public void updateOpenTradePrices(String username)  {
        try {
            String query = "SELECT symbol FROM trade_open WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            query = "UPDATE trade_open SET current_price = ? WHERE symbol = ?";
            while(resultSet.next()){
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, new GetStockData(resultSet.getString(1)).getCurrentPrice());
                preparedStatement.setString(2, resultSet.getString(1));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            System.out.println("something went wrong while updating- 000 -- the current prices.\n"+e);
        }
    }

    //Update the PL
    public void refreshPL(String username)  {
        try {
            String query = "UPDATE trade_open SET p_l = no_of_shares * (current_price - entry_price) WHERE order_type = 'buy' AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.executeUpdate();
            query = "UPDATE trade_open SET p_l = no_of_shares * (entry_price - current_price) WHERE order_type = 'sell' AND user_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("something went wrong while refreshing the P/L.\n"+e);
        }
    }

    //=================================== OPEN TRADES STATS ===================================

    //Get the current position value --> growth
    public double getCurrentInvestment(String username)  {
        String query = "SELECT sum(no_of_shares * current_price) FROM trade_open WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: CurrentInvestment.\n"+e);
        }

        return 0;    }

    //Get the total investment invested at the time of buying or shorting the security
    public double getInvestmentInto(String username)  {
        String query = "SELECT sum(no_of_shares * entry_price) FROM trade_open WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: InvestmentInto.\n"+e);
        }

        return 0;    }

    //Get the Open total PL
    public double getOpenPL(String username)  {
        String query = "SELECT sum(p_l) FROM trade_open WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting open trade stat: OpenPL.\n"+e);
        }

        return 0;
    }

    //Get count of open trades
    public int getNumOfOpenTrades(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfOpenTrades.\n"+e);
        }
        return 0;
    }

    //Get count of lossers
    public int getNumOfLosers(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND p_l < 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfLosers.\n"+e);
        }

        return 0;
    }

    //Get count of wiinners
    public int getNumOfWinners(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND p_l > 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfWinners.\n"+e);
        }

        return 0;
    }

    //Get count of Buys
    public int getNumOfBuys(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'buy'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfBuys.\n"+e);
        }

        return 0;
    }

    //Get count of Shorts
    public int getNumOfShorts(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'sell'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfShorts.\n"+e);
        }

        return 0;
    }

    //Get count of buy winners
    public int getNumOfBuyWinner(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'buy' AND p_l > 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfBuyWinner.\n"+e);
        }

        return 0;
    }

    //Get count of buy losers
    public int getNumOfBuyLoser(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'buy' AND p_l < 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfBuyLoser.\n"+e);
        }

        return 0;
    }

    //Get count of short winners
    public int getNumOfShortWinner(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'sell' AND p_l > 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfShortWinner.\n"+e);
        }

        return 0;    }

    //Get count of short losers
    public int getNumOfShortLoser(String username)  {
        String query = "SELECT count(trade_id) FROM trade_open WHERE user_id = ? AND order_type = 'sell' AND p_l < 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println("somwthing went wrong while getting open trade stat: NumOfShortLoser.\n"+e);
        }

        return 0;    }

}