package Service;

import Model.Quote;
import Util.TDAPI;
import com.fasterxml.jackson.databind.JsonNode;

public class GetStockData {
    
    private TDAPI tdapi;
    private JsonNode node;
    String symbol;

    //CONSTRUCTOR: accepts a Quote object to send a request to the API and get the data about the symbol in quote object.
    public GetStockData(Quote quote){
        //Creating an API object.
        tdapi = new TDAPI();
        //Setting the symbol so that it can be reused
        this.symbol = quote.getSymbol();
        //Building URL to get data from the TD.
        String URL = tdapi.buildQuotesURL(quote.getSymbol());
        //Getting the stock data in a JSON Format.
        node = tdapi.parseQuotes(tdapi.getResponse(URL));

    }

    //CONSTRUCTOR: accepts a String to send a request to the API and get the data about the symbol in quote object.
    public GetStockData(String symbol){
        //Creating an API object.
        tdapi = new TDAPI();
        //Setting the symbol so that it can be reused
        this.symbol = symbol.toUpperCase();
        //Building URL to get data from the TD.
        String URL = tdapi.buildQuotesURL(symbol);
        //Getting the stock data in a JSON Format.
        node = tdapi.parseQuotes(tdapi.getResponse(URL));
    }
    ////==================================  GET A QUOTE OBJECT WITH STOCK DATA ==================================
    public Quote getQuote(){
        Quote quote = new Quote(symbol);
        //Inserting values inside the quotes object.
        quote.setAssetType(getAssetType());
        quote.setShortable(isShortable());
        quote.setDescription(getDescription());
        quote.setExchange(getExchange());
        quote.setCurrentPrice(getCurrentPrice());
        quote.setBidPrice(getBidPrice());
        quote.setAskPrice(getAskPrice());
        quote.setOpenPrice(getOpenPrice());
        quote.setHighPrice(getHighPrice());
        quote.setLowPrice(getLowPrice());
        quote.setClosePrice(getClosePrice());
        quote.setNetChange(getNetChange());
        quote.setWk52High(getWk52High());
        quote.setWk52Low(getWk52Low());
        quote.setVolatility(getVolatility());
        quote.setPeRatio(getPeRatio());
        quote.setDivAmount(getDivAmount());
        quote.setDivYield(getDivYield());
        quote.setNetPercentChange(getNetPercentChange());
        quote.setTotalVolume(getTotalVolume());
        return quote;
    }





    //==================================  GETTERS ==================================

    //Is the security shortable?
    public Boolean isShortable() {
        return node.get(symbol).get("shortable").asBoolean();
    }

    //Get Description
    public String getDescription() {
        return node.get(symbol).get("description").asText();
    }
    //Get Asset Type
    public String getAssetType() {
        return node.get(symbol).get("assetType").asText();
    }

    // What is the exchange the security is traded on?
    public String getExchange() {
        return node.get(symbol).get("exchangeName").asText();
    }

    //Get the broker's buying price or our shorting price or our closing price
    public double getBidPrice() {
        return node.get(symbol).get("bidPrice").asDouble();
    }

    //Get the broker's selling price or our buying price or our buying back price
    public double getAskPrice() {
        return node.get(symbol).get("askPrice").asDouble();
    }

    //Get the Open Price
    public double getOpenPrice() {
        return node.get(symbol).get("openPrice").asDouble();
    }

    //Get the High Price
    public double getHighPrice() {
        return node.get(symbol).get("highPrice").asDouble();
    }

    //Get the Low Price
    public double getLowPrice() {
        return node.get(symbol).get("lowPrice").asDouble();
    }

    //Get the Close Price
    public double getClosePrice() {
        return node.get(symbol).get("closePrice").asDouble();
    }

    //Get the Net Change in dollars
    public double getNetChange() {
        return node.get(symbol).get("regularMarketNetChange").asDouble();
    }

    //Get the 52 week High
    public double getWk52High() {
        return node.get(symbol).get("52WkHigh").asDouble();
    }

    //Get the 52 Week Low
    public double getWk52Low() {
        return node.get(symbol).get("52WkLow").asDouble();
    }

    //Get the Volatility
    public double getVolatility() {
        return node.get(symbol).get("volatility").asDouble();
    }

    //Get the PE Ratio
    public double getPeRatio() {
        return node.get(symbol).get("peRatio").asDouble();
    }

    //Get the Dividend amount
    public double getDivAmount() {
        return node.get(symbol).get("divAmount").asDouble();
    }

    //Get the Dividend Yeild
    public double getDivYield() {
        return node.get(symbol).get("divYield").asDouble();
    }

    //Get the Net Percentage Change
    public double getNetPercentChange() {
        return node.get(symbol).get("netPercentChangeInDouble").asDouble();
    }

    //Get the Current Price
    public double getCurrentPrice() {
        return node.get(symbol).get("regularMarketLastPrice").asDouble();
    }

    //Get the Total Volume
    public long getTotalVolume() {
        return node.get(symbol).get("totalVolume").asLong();
    }
}