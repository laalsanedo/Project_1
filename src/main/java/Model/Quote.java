package Model;

public class Quote {
    private String symbol, description, assetType, exchange;
    private double currentPrice, bidPrice, askPrice, openPrice, highPrice, lowPrice, closePrice,
            netChange, Wk52High, Wk52Low, volatility, peRatio, divAmount, divYield, netPercentChange;
    private long totalVolume;
    private boolean shortable;
    public Quote(String symbol){
        this.symbol = symbol.toUpperCase().trim();
    }
    //==================================  GETTERS ==================================
    //Is the security shortable?
    public Boolean isShortable() {
        return shortable;
    }

    //Get Symbol
    public String getSymbol() {
        return symbol;
    }

    //Get Description
    public String getDescription() {
        return description;
    }

    //Get Asset Type
    public String getAssetType() {
        return assetType;
    }

    // What is the exchange the security is traded on?
    public String getExchange() {
        return exchange;
    }

    //Get the current price
    public double getCurrentPrice() {
        return currentPrice;
    }

    //Get the broker's buying price or our shorting price or our closing price
    public double getBidPrice() {
        return bidPrice;
    }

    //Get the broker's selling price or our buying price or our buying back price
    public double getAskPrice() {
        return askPrice;
    }

    //Get the Open Price
    public double getOpenPrice() {
        return openPrice;
    }

    //Get the High Price
    public double getHighPrice() {
        return highPrice;
    }

    //Get the Low Price
    public double getLowPrice() {
        return lowPrice;
    }

    //Get the Close Price
    public double getClosePrice() {
        return closePrice;
    }

    //Get the Net Change in dollars
    public double getNetChange() {
        return netChange;
    }

    //Get the 52 week High
    public double getWk52High() {
        return Wk52High;
    }

    //Get the 52 Week Low
    public double getWk52Low() {
        return Wk52Low;
    }

    //Get the Volatility
    public double getVolatility() {
        return volatility;
    }

    //Get the PE Ratio
    public double getPeRatio() {
        return peRatio;
    }

    //Get the Dividend amount
    public double getDivAmount() {
        return divAmount;
    }

    //Get the Dividend Yeild
    public double getDivYield() {
        return divYield;
    }

    //Get the Net Percentage Change
    public double getNetPercentChange() {
        return netPercentChange;
    }

    //Get the Total Volume
    public long getTotalVolume() {
        return totalVolume;
    }

    //==================================  SETTERS ==================================
    //Set if the security is Shortable or not?
    public void setShortable(boolean shortable) {
        this.shortable = shortable;
    }

    //Set the Symbol
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //Set the Decription
    public void setDescription(String description) {
        this.description = description;
    }

    //Get the Asset Type
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    //Set the Exhange the symbol is quoted on
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    //Set the current price
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    //Set the Bid Price
    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    //Set the Ask price
    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    //Set the OpenPrice
    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    //Set the High Price
    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    //Set the Low Price
    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    //Set the Close Price
    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    //Set the Net Change in dollars
    public void setNetChange(double netChange) {
        this.netChange = netChange;
    }

    //Set the 52Week High
    public void setWk52High(double wk52High) {
        Wk52High = wk52High;
    }

    //Set the 52Week Low
    public void setWk52Low(double wk52Low) {
        Wk52Low = wk52Low;
    }

    //Set the Volatility
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    //Set the PE Ratio
    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    //Set the Dividend Amount
    public void setDivAmount(double divAmount) {
        this.divAmount = divAmount;
    }

    //Set the Dividend Yeild
    public void setDivYield(double divYield) {
        this.divYield = divYield;
    }

    //Set the Net Percentage Change
    public void setNetPercentChange(double netPercentChange) {
        this.netPercentChange = netPercentChange;
    }

    //Set the Total Volume
    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
    }

    //======================================= To String =======================================
    @Override
    public String toString(){
        return "Random: "+bidPrice+", "+askPrice+", "+totalVolume;
    }

}