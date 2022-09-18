package DAO;

import Model.Trade;
import Service.GetStockData;
import Util.Connectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradeHistory {
    private Connection connection;
    private ResultSet resultSet;
    private UserInfo userInfo;
    private Trade trade;
    private ArrayList<Trade> list;

    //Constructor: accepts username and initializes several objects
    public TradeHistory() {
        userInfo = new UserInfo();
        connection = Connectivity.databaseConnect();
    }

    //=================================== CREATE A CLOSE TRADE ===================================

    //Close the Latest Trade Opened OR particular TradeID
    public boolean closeTrade(Trade trade) {
        String query = "INSERT INTO trade_history(trade_id, order_type, symbol, no_of_shares, buying_price, selling_price, p_l, user_id)" +
                "Values (?, ?, ?, ?, ?, ?, ?, ?)";
        if (trade.getOrderType() == null)
            return false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, trade.getTradeID());
            preparedStatement.setString(2, trade.getOrderType());
            preparedStatement.setString(3, trade.getSymbol());
            preparedStatement.setInt(4, trade.getNumOfShares());
            if ( trade.getOrderType().equalsIgnoreCase("buy") ){
                preparedStatement.setDouble(5, trade.getEntryPrice());
                preparedStatement.setDouble(6, new GetStockData(trade.getSymbol()).getAskPrice());
            }
            if ( trade.getOrderType().equalsIgnoreCase("sell")){
                preparedStatement.setDouble(5, new GetStockData(trade.getSymbol()).getBidPrice());
                preparedStatement.setDouble(6, trade.getEntryPrice());
            }
            preparedStatement.setDouble(7, trade.getPL());
            preparedStatement.setInt(8, trade.getUserID());
            return preparedStatement.executeUpdate() >=1;

        } catch (SQLException e) {
            System.out.println("something went wrong while closing a trade. \n"+e);
        }
        return false;
    }

    //Close a particular trade(s) based on the symbol(s) OR All Trades
    public boolean closeTrades(ArrayList<Trade> trades) {
        String query = "INSERT INTO trade_history(trade_id, order_type, symbol, no_of_shares, buying_price, selling_price, p_l, user_id)" +
                "Values (?, ?, ?, ?, ?, ?, ?, ?)";
        for (Trade trade1 : trades){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, trade1.getTradeID());
                preparedStatement.setString(2, trade1.getOrderType());
                preparedStatement.setString(3, trade1.getSymbol());
                preparedStatement.setInt(4, trade1.getNumOfShares());
                if (trade1.getOrderType().equalsIgnoreCase("buy")){
                    preparedStatement.setDouble(5, trade1.getEntryPrice());
                    preparedStatement.setDouble(6, trade1.getCurrentPrice());
                }
                if (trade1.getOrderType().equalsIgnoreCase("sell")){
                    preparedStatement.setDouble(5, trade1.getCurrentPrice());
                    preparedStatement.setDouble(6, trade1.getEntryPrice());
                }
                preparedStatement.setDouble(7, trade1.getPL());
                preparedStatement.setInt(8, trade1.getUserID());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("something went wrong while closing trades.\n"+e);
            }
        }
        return true;
    }

    //=================================== GETS CLOSE TRADES ===================================

    //Get the lasted trade that was opened
    public Trade getLatestCloseTrade(String username) {
        trade = new Trade();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND trade_id = (SELECT max(trade_id) FROM trade_history WHERE user_id = ?)";
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
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                return trade;
            }

        } catch (SQLException e) {
            System.out.println("something went wrong while getting the latest closed trade.\n"+e);
        }
        return null;
    }

    //Get a close trade based on a tradeID
    public Trade getCloseTradeID(String username, int tradeID){
        trade = new Trade();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND trade_id = ?";
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
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
            }
            return trade;
        } catch (SQLException e) {
            System.out.println("something went wrong while getting a closed trade by tradeID.\n"+e);

        }
        return null;
    }

    //Get the close trade(s) based on a orderTpe.
    public ArrayList<Trade> getCloseTradeOrderType(String username, String orderType) {
        trade = new Trade();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND order_type = ? ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            preparedStatement.setString(2, orderType);
            resultSet = preparedStatement.executeQuery();

            //Stores each trade in result set inside the trade object which in turn is store in a arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }

        } catch (SQLException e) {
            System.out.println("something went wrong while getting closed trades by order type.\n"+e);
        }
        return list;
    }

    //Get the close trade(s) based on a Winners.
    public ArrayList<Trade> getCloseTradeWinners(String username) {
        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND p_l > 0 ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in a arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while getting closed trades by winner.\n"+e);
        }
        return list;
    }

    //Get the close trade(s) based on a Losers.
    public ArrayList<Trade> getCloseTradeLosers(String username) {
        trade = new Trade();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND p_l < 0 ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in a arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while getting closed trades by losers.\n"+e);
        }
        return list;
    }

    //Get the close trade(s) based on a symbol(s).
    public ArrayList<Trade> getCloseTradeSymbol(String username, String... symbols)  {
        trade = new Trade();
        String query = "SELECT * FROM trade_history WHERE user_id = ? AND symbol = ? ORDER BY trade_id DESC";
        try{
            for (String symbol : symbols){
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userInfo.getID(username));
                preparedStatement.setString(2, symbol);
                resultSet = preparedStatement.executeQuery();

                //Stores each trade in result set inside the trade object which in turn is store in a arraylist of type Trade.
                while(resultSet.next()){
                    trade.setTradeID(resultSet.getInt(1));
                    trade.setOrderType(resultSet.getString(2));
                    trade.setSymbol(resultSet.getString(3));
                    trade.setNumOfShares(resultSet.getInt(4));
                    trade.setEntryPrice(resultSet.getDouble(5));
                    trade.setClosePrice(resultSet.getDouble(6));
                    trade.setPL(resultSet.getDouble(7));
                    trade.setUserID(resultSet.getInt(8));
                    list.add(trade);
                    trade = new Trade();
                }
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting closed trades by symbols.\n"+e);
        }
        return list;
    }

    //Get all the closed trades for a given user
    public ArrayList<Trade> getAllCloseTrades(String username)  {
        trade = new Trade();
        list = new ArrayList<>();
        String query = "SELECT * FROM trade_history WHERE user_id = ? ORDER BY trade_id DESC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            //Stores each trade in result set inside the trade object which in turn is store in a arraylist of type Trade.
            while(resultSet.next()){
                trade.setTradeID(resultSet.getInt(1));
                trade.setOrderType(resultSet.getString(2));
                trade.setSymbol(resultSet.getString(3));
                trade.setNumOfShares(resultSet.getInt(4));
                trade.setEntryPrice(resultSet.getDouble(5));
                trade.setClosePrice(resultSet.getDouble(6));
                trade.setPL(resultSet.getDouble(7));
                trade.setUserID(resultSet.getInt(8));
                list.add(trade);
                trade = new Trade();
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while getting all closed trades.\n"+e);
        }

        return list;
    }


    //=================================== CLOSE TRADES STATS ===================================

    //Get the historic position value --> growth
    public double getHistoricInvestment(String username)  {
        double sum = 0;
        try{
            //Sum for buy trades
            String query = "SELECT sum(no_of_shares * buying_price) FROM trade_history WHERE user_id = ? AND order_type = 'buy'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                sum = resultSet.getDouble(1);
            }
            //Sum for short trades
            query = "SELECT sum(no_of_shares * selling_price) FROM trade_history WHERE user_id = ? AND order_type = 'sell'";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                sum += resultSet.getDouble(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: Historic Investment\n"+e);
        }

        return sum;
    }

    //Get the Open total PL
    public double getClosePL(String username)  {
        String query = "SELECT sum(p_l) FROM trade_history WHERE user_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: ClosePL\n"+e);
        }
        return 0;
    }

    //Get count of closed trades
    public int getNumOfCloseTrades(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfCloseTrades\n"+e);
        }
        return 0;
    }

    //Get count of lossers
    public int getNumOfLosers(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND p_l < 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfLosers\n"+e);
        }
        return 0;
    }

    //Get count of winners
    public int getNumOfWinners(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND p_l > 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfWinners\n"+e);
        }
        return 0;
    }

    //Get count of Buys
    public int getNumOfBuys(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'buy'";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfBuys\n"+e);
        }
        return 0;
    }

    //Get count of Shorts
    public int getNumOfShorts(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'sell'";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfShorts\n"+e);
        }
        return 0;
    }

    //Get count of buy winners
    public int getNumOfBuyWinner(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'buy' AND p_l > 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfBuyWinner\n"+e);
        }
        return 0;
    }

    //Get count of buy losers
    public int getNumOfBuyLoser(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'buy' AND p_l < 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfBuyLoser\n"+e);
        }
        return 0;
    }

    //Get count of short winners
    public int getNumOfShortWinner(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'sell' AND p_l > 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfShortWinner\n"+e);
        }

        return 0;    }

    //Get count of short losers
    public int getNumOfShortLoser(String username)  {
        String query = "SELECT count(trade_id) FROM trade_history WHERE user_id = ? AND order_type = 'sell' AND p_l < 0";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userInfo.getID(username));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("something went wrong while getting closed trade stats: NumOfShortLoser\n"+e);
        }
        return 0;    }
}