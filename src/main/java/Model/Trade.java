package Model;

import Service.GetStockData;

public class Trade {
    int tradeID, numOfShares, userID;
    String orderType, symbol;
    double entryPrice, currentPrice, closePrice, PL;

    //Constructors for a trade object
    public Trade(){};

    //Constructor that will create a new trade
    public Trade(int numOfShares, int userID, String orderType, String symbol) {
        GetStockData getStockData = new GetStockData(symbol);
        this.numOfShares = numOfShares;
        this.userID = userID;
        this.orderType = orderType;
        this.symbol = symbol;
        if (orderType.equals("buy")) {
            this.entryPrice = getStockData.getBidPrice();
            this.PL = getStockData.getBidPrice() - getStockData.getCurrentPrice();
        }
        else {
            this.entryPrice = getStockData.getAskPrice();
            this.PL = getStockData.getCurrentPrice() - getStockData.getAskPrice();
        }


    }


    //==================================  GETTERS ==================================
    //Gets a tradeID.
    public int getTradeID() {
        return tradeID;
    }

    //Gets the number of shares.
    public int getNumOfShares() {
        return numOfShares;
    }

    //gets the userID.
    public int getUserID() {
        return userID;
    }

    //Gets the orderType.
    public String getOrderType() {
        return orderType;
    }

    //Gets the symbol
    public String getSymbol() {
        return symbol;
    }

    //Gets the entryPrice
    public double getEntryPrice() {
        return entryPrice;
    }

    //Gets the currentPrice
    public double getCurrentPrice() {
        return currentPrice;
    }

    //Gets the closePrice
    public double getClosePrice() {
        return closePrice;
    }

    //Gets the P/L
    public double getPL() {
        return PL;
    }

    //==================================  SETTERS ==================================
    //Sets the tradeID
    public void setTradeID(int tradeID) {
        this.tradeID = tradeID;
    }

    //Sets the number of shares
    public void setNumOfShares(int numOfShares) {
        this.numOfShares = numOfShares;
    }

    //Sets the userID
    public void setUserID(int userID) {
        this.userID = userID;
    }

    //Sets the orderType
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    //Sets the symbol
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //Sets the entryPrice
    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    //Sets the currentPrice
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    //Sets the closePrice
    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    //Sets the P/L
    public void setPL(double PL) {
        this.PL = PL;
    }

}