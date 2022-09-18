package Service;

import DAO.TradeOpen;
import DAO.UserInfo;
import Model.OpenTradeStats;
import Model.Trade;

import java.util.ArrayList;

public class OpenTrade {
    private TradeOpen tradeOpen;
    private ArrayList<Trade> trades;
    private Trade trade;
    private UserInfo userInfo;

    //Constructor: accepts username and initializes several objects
    public OpenTrade()  {
        tradeOpen = new TradeOpen();
        userInfo = new UserInfo();
    }

    //=================================== CREATE A NEW TRADE ===================================

    public boolean newTrade(String username, Trade trade) {
        trade.setUserID(userInfo.getID(username));
        refreshPL(username);
        return tradeOpen.newTrade(trade);
    }
    //=================================== GETS OPEN TRADES ===================================

    //Get the lasted trade that was opened
    public Trade getLatestTrade(String username) {
        return tradeOpen.getLatestTrade(username);
    }

    //Get a open trade based on a tradeID
    public Trade getOpenTradeID(String username, int tradeID) {
        return tradeOpen.getOpenTradeID(username, tradeID);
    }

    //Get the open trade(s) based on a orderTpe.
    public ArrayList<Trade> getTradeOrderType(String username, String orderType) {
        return tradeOpen.getTradeOrderType(username, orderType);
    }

    //Get the open trade(s) based on a Winners.
    public ArrayList<Trade> getOpenTradeWinners(String username) {
        return tradeOpen.getOpenTradeWinners(username);
    }

    //Get the open trade(s) based on a Losers.
    public ArrayList<Trade> getOpenTradeLosers(String username) {
        return tradeOpen.getCloseTradeLosers(username);
    }

    //Get the open trade(s) based on a symbol(s).
    public ArrayList<Trade> getOpenTradeSymbol(String username, String... symbols) {
        return tradeOpen.getOpenTradeSymbol(username, symbols);
    }

    //Get all the trades that the user currently has open
    public ArrayList<Trade> getAllOpenTrades(String username) {
        return tradeOpen.getAllOpenTrades(username);
    }

    //=================================== DELETES OPEN TRADES ===================================

    //Delete a single trade
    public boolean deleteTrade(String username, Trade trade) {
        return tradeOpen.deleteTrade(username, trade);
    }

    //Delete a multiple trade
    public boolean deleteTrades(String username, ArrayList<Trade> trades) {
        return tradeOpen.deleteTrades(username, trades);
    }

    //=================================== UPDATE OPEN TRADES ===================================

    //Update the current price
    public void updateOpenTradePrices(String username) {
        tradeOpen.updateOpenTradePrices(username);
    }

    //Update the PL
    public void refreshPL(String username) {
        updateOpenTradePrices(username);
        tradeOpen.refreshPL(username);
    }

    //=================================== GET STATS ===================================

    public OpenTradeStats getOpenStats(String username){
        OpenTradeStats stats = new OpenTradeStats();
        stats.setCurrentInvestment(getCurrentInvestment(username));
        stats.setInvestment(getInvestmentInto(username));
        stats.setOpenPL(getOpenPL(username));
        stats.setOpenTrades(getNumOfOpenTrades(username));
        stats.setWinners(getNumOfWinners(username));
        stats.setLosers(getNumOfLosers(username));
        stats.setNeutral(getNumOfNeutrals(username));
        stats.setBuy(getNumOfBuys(username));
        stats.setBuyW(getNumOfBuyWinner(username));
        stats.setBuyL(getNumOfBuyLoser(username));
        stats.setShorts(getNumOfShorts(username));
        stats.setShortW(getNumOfShortWinner(username));
        stats.setShortL(getNumOfShortLoser(username));
        stats.setWinPercent(getPercentageWinners(username));
        stats.setLossPercent(getPercentageLosers(username));
        stats.setBuyPercent(getPercentageBuys(username));
        stats.setBuyWPercent(getPercentageBuyW(username));
        stats.setBuyLPercent(getPercentageBuyL(username));
        stats.setShortPercent(getPercentageShorts(username));
        stats.setShortWPercent(getPercentageShortW(username));
        stats.setShortLPercent(getPercentageShortL(username));
        return stats;
    }

    //=================================== OPEN TRADES STATS ===================================

    //Get the current investment value includes the PL
    public double getCurrentInvestment(String username) {
        return tradeOpen.getCurrentInvestment(username);
    }

    //How much Investment was needed to open current positions
    public double getInvestmentInto(String username) {
        return tradeOpen.getInvestmentInto(username);
    }

    //Get the Open total PL
    public double getOpenPL(String username) {
        return tradeOpen.getOpenPL(username);
    }

    //Get count of open trades
    public int getNumOfOpenTrades(String username) {
        return tradeOpen.getNumOfOpenTrades(username);
    }

    //Get count of lossers
    public int getNumOfLosers(String username) {
        return tradeOpen.getNumOfLosers(username);
    }

    //Get count of winners
    public int getNumOfWinners(String username) {
        return tradeOpen.getNumOfWinners(username);
    }

    //Get count of neutral
    public int getNumOfNeutrals(String username) {
        return (getNumOfOpenTrades(username) - (getNumOfWinners(username) + getNumOfLosers(username)));
    }

    //Get count of Buys
    public int getNumOfBuys(String username) {
        return tradeOpen.getNumOfBuys(username);
    }

    //Get count of buy winners
    public int getNumOfBuyWinner(String username) {
        return tradeOpen.getNumOfBuyWinner(username);
    }

    //Get count of buy losers
    public int getNumOfBuyLoser(String username) {
        return tradeOpen.getNumOfBuyLoser(username);
    }

    //Get count of Shorts
    public int getNumOfShorts(String username) {
        return tradeOpen.getNumOfShorts(username);
    }

    //Get count of short winners
    public int getNumOfShortWinner(String username) {
        return tradeOpen.getNumOfShortWinner(username);
    }

    //Get count of short losers
    public int getNumOfShortLoser(String username) {
        return tradeOpen.getNumOfShortLoser(username);
    }


    //====================================== DERIVED STATS ======================================

    //Get % losers ---D
    public double getPercentageLosers(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfLosers(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % winners ---D
    public double getPercentageWinners(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfWinners(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % neutral ---D
    public double getPercentageNeutral(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfNeutrals(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Buys ---D
    public double getPercentageBuys(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfBuys(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Buys Winners ---D
    public double getPercentageBuyW(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfBuyWinner(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Buys Losers ---D
    public double getPercentageBuyL(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfBuyLoser(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Short ---D
    public double getPercentageShorts(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfShorts(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Short Winners ---D
    public double getPercentageShortW(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfShortWinner(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }

    //Get % Short Losers ---D
    public double getPercentageShortL(String username) {
        if (getNumOfOpenTrades(username)>0) {
            return (getNumOfShortLoser(username) * 100 / getNumOfOpenTrades(username));
        }
        return 0;
    }
}