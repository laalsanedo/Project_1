package Service;

import DAO.TradeHistory;
import Model.CloseTradeStats;
import Model.Trade;

import java.util.ArrayList;

public class CloseTrade {
    private TradeHistory tradeHistory;
    private OpenTrade openTrade;
    private AccountInfo accountInfo;

    //Constructor: accepts username and initializes several objects;
    public CloseTrade()  {
        tradeHistory = new TradeHistory();
        openTrade = new OpenTrade();
        accountInfo = new AccountInfo();
    }

    //====================================== CLOSE TRADES ======================================

    //Close latest open trade
    public boolean closeLatestTrade(String username)  {
        Trade trade = openTrade.getLatestTrade(username);
         boolean result = tradeHistory.closeTrade(trade);
         if (result){
             accountInfo.updateBalance(username, trade.getPL());
             accountInfo.updatingBuyingPower(username, trade.getEntryPrice()*trade.getNumOfShares(), trade.getPL(), 0);
             return  openTrade.deleteTrade(username, trade);
         }
         return false;
    }

    //Close a particular ID
    public boolean closeTradeID(String username, int ID)  {
        Trade trade = openTrade.getOpenTradeID(username, ID);
        boolean result = tradeHistory.closeTrade(trade);
        if (result){
            accountInfo.updateBalance(username, trade.getPL());
            accountInfo.updatingBuyingPower(username, trade.getEntryPrice()*trade.getNumOfShares(), trade.getPL(), 0);
            return  openTrade.deleteTrade(username, trade);
        }
        return false;
    }

    //Close all winners
    public boolean closeWinners(String username)  {
        ArrayList<Trade> trades = openTrade.getOpenTradeWinners(username);
        boolean result = tradeHistory.closeTrades(trades);
        if (result){
            for (Trade trade1 : trades){
                accountInfo.updateBalance(username, trade1.getPL());
                accountInfo.updatingBuyingPower(username, trade1.getEntryPrice()*trade1.getNumOfShares(), trade1.getPL(), 0);
            }
            return openTrade.deleteTrades(username, trades);
        }
        return false;
    }

    //Close all losers
    public boolean closeLosers(String username)  {
        ArrayList<Trade> trades = openTrade.getOpenTradeLosers(username);
        boolean result = tradeHistory.closeTrades(trades);
        if (result){
            for (Trade trade1 : trades){
                accountInfo.updateBalance(username, trade1.getPL());
                accountInfo.updatingBuyingPower(username, trade1.getEntryPrice()*trade1.getNumOfShares(), trade1.getPL(), 0);
            }
            return openTrade.deleteTrades(username, trades);
        }
        return false;
    }

    //Close by order_type
    public boolean closeOrderType(String username, String orderType)  {
        ArrayList<Trade> trades = openTrade.getTradeOrderType(username, orderType);
        boolean result = tradeHistory.closeTrades(trades);
        if (result){
            for (Trade trade1 : trades){
                accountInfo.updateBalance(username, trade1.getPL());
                accountInfo.updatingBuyingPower(username, trade1.getEntryPrice()*trade1.getNumOfShares(), trade1.getPL(), 0);
            }
            return openTrade.deleteTrades(username, trades);
        }
        return false;
    }

    //Close by symbol
    public boolean closeSymbol(String username, String... symbol)  {
        ArrayList<Trade> trades = openTrade.getOpenTradeSymbol(username, symbol);
        boolean result = tradeHistory.closeTrades(trades);
        if (result){
            for (Trade trade1 : trades){
                accountInfo.updateBalance(username, trade1.getPL());
                accountInfo.updatingBuyingPower(username, trade1.getEntryPrice()*trade1.getNumOfShares(), trade1.getPL(), 0);
            }
            return openTrade.deleteTrades(username, trades);
        }
        return false;
    }

    //Close all trades
    public boolean closeAllTrades(String username)  {
        ArrayList<Trade> trades = openTrade.getAllOpenTrades(username);
        boolean result = tradeHistory.closeTrades(trades);
        if (result){
            for (Trade trade1 : trades){
                accountInfo.updateBalance(username, trade1.getPL());
                accountInfo.updatingBuyingPower(username, trade1.getEntryPrice()*trade1.getNumOfShares(), trade1.getPL(), 0);
            }
            return openTrade.deleteTrades(username, trades);
        }
        return false;
    }

    //====================================== GET CLOSED TRADES ======================================

    //Get the lasted trade that was opened
    public Trade getLatestCloseTrade(String username) {
        return tradeHistory.getLatestCloseTrade(username);
    }

    //Get a close trade based on a tradeID
    public Trade getCloseTradeID(String username, int tradeID) {
        return tradeHistory.getCloseTradeID(username, tradeID);
    }

    //Get the close trade(s) based on a orderTpe.
    public ArrayList<Trade> getCloseTradeOrderType(String username, String orderType) {
        return tradeHistory.getCloseTradeOrderType(username, orderType);
    }

    //Get the close trade(s) based on a Winners.
    public ArrayList<Trade> getCloseTradeWinners(String username) {
        return tradeHistory.getCloseTradeWinners(username);
    }

    //Get the close trade(s) based on a Losers.
    public ArrayList<Trade> getCloseTradeLosers(String username) {
        return tradeHistory.getCloseTradeLosers(username);
    }

    //Get the close trade(s) based on a symbol(s).
    public ArrayList<Trade> getCloseTradeSymbol(String username, String... symbols) {
        return tradeHistory.getCloseTradeSymbol(username, symbols);
    }

    //Get all the closed trades for a given user
    public ArrayList<Trade> getAllCloseTrades(String username) {
        return tradeHistory.getAllCloseTrades(username);
    }

    //====================================== GET CLOSED TRADE STATS ======================================

    public CloseTradeStats getCloseStats(String username){
        CloseTradeStats stats = new CloseTradeStats();
        stats.setHistoricInvestment(getHistoricInvestment(username));
        stats.setClosePL(getClosePL(username));
        stats.setCloseTrades(getNumOfCloseTrades(username));
        stats.setcWinners(getNumOfWinners(username));
        stats.setcLosers(getNumOfLosers(username));
        stats.setcNeutral(getNumOfNeutrals(username));
        stats.setcBuy(getNumOfBuys(username));
        stats.setcBuyW(getNumOfBuyWinner(username));
        stats.setcBuyL(getNumOfBuyLoser(username));
        stats.setcShorts(getNumOfShorts(username));
        stats.setcShortW(getNumOfShortWinner(username));
        stats.setcShortL(getNumOfShortLoser(username));
        stats.setcWinPercent(getPercentageWinners(username));
        stats.setcLossPercent(getPercentageLosers(username));
        stats.setcBuyPercent(getPercentageBuys(username));
        stats.setcBuyWPercent(getPercentageBuyW(username));
        stats.setcBuyLPercent(getPercentageBuyL(username));
        stats.setcShortPercent(getPercentageShorts(username));
        stats.setcShortWPercent(getPercentageShortW(username));
        stats.setcShortLPercent(getPercentageShortL(username));
        return stats;
    }

    //====================================== CLOSED TRADE STATS ======================================

    //Get the historic position value --> growth
    public double getHistoricInvestment(String username) {
        return tradeHistory.getHistoricInvestment(username);
    }

    //Get the Close total PL
    public double getClosePL(String username) {
        return tradeHistory.getClosePL(username);
    }

    //Get count of closed trades
    public int getNumOfCloseTrades(String username) {
        return tradeHistory.getNumOfCloseTrades(username);
    }

    //Get count of lossers
    public int getNumOfLosers(String username) {
        return tradeHistory.getNumOfLosers(username);
    }

    //Get count of winners
    public int getNumOfWinners(String username) {
        return tradeHistory.getNumOfWinners(username);
    }

    //Get count of neutral
    public int getNumOfNeutrals(String username) {
        return (getNumOfCloseTrades(username) - (getNumOfWinners(username) + getNumOfLosers(username)));
    }

    //Get count of Buys
    public int getNumOfBuys(String username) {
        return tradeHistory.getNumOfBuys(username);
    }

    //Get count of buy winners
    public int getNumOfBuyWinner(String username) {
        return tradeHistory.getNumOfBuyWinner(username);
    }

    //Get count of buy losers
    public int getNumOfBuyLoser(String username) {
        return tradeHistory.getNumOfBuyLoser(username);
    }

    //Get count of Shorts
    public int getNumOfShorts(String username) {
        return tradeHistory.getNumOfShorts(username);
    }

    //Get count of short winners
    public int getNumOfShortWinner(String username) {
        return tradeHistory.getNumOfShortWinner(username);
    }

    //Get count of short losers
    public int getNumOfShortLoser(String username) {
        return tradeHistory.getNumOfShortLoser(username);
    }


    //====================================== DERIVED STATS ======================================

    //Get % losers ---D
    public double getPercentageLosers(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfLosers(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % winners ---D
    public double getPercentageWinners(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfWinners(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % neutral ---D
    public double getPercentageNeutral(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfNeutrals(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Buys ---D
    public double getPercentageBuys(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfBuys(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Buys Winners ---D
    public double getPercentageBuyW(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfBuyWinner(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Buys Losers ---D
    public double getPercentageBuyL(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfBuyLoser(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Short ---D
    public double getPercentageShorts(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfShorts(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Short Winners ---D
    public double getPercentageShortW(String username) {
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfShortWinner(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

    //Get % Short Losers ---D
    public double getPercentageShortL(String username){
        if (getNumOfCloseTrades(username)>0) {
            return (getNumOfShortLoser(username) * 100 / getNumOfCloseTrades(username));
        }
        return 0;
    }

}