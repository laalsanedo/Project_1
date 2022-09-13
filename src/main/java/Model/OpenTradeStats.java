package Model;

public class OpenTradeStats {
    private double currentInvestment, investment, openPL, openPLPercent, lossPercent, winPercent, neutralPercent
            , buyPercent, buyWPercent, buyLPercent, shortPercent, shortWPercent, shortLPercent;
    private int openTrades, winners, losers, neutral, buy, buyW, buyL, shorts, shortW, shortL;


    // ================================= GETTERS ================================

    public double getCurrentInvestment() {
        return currentInvestment;
    }

    public double getInvestment() {
        return investment;
    }

    public double getOpenPL() {
        return openPL;
    }

    public double getOpenPLPercent() {
        return openPLPercent;
    }

    public double getLossPercent() {
        return lossPercent;
    }

    public double getWinPercent() {
        return winPercent;
    }

    public double getNeutralPercent() {
        return neutralPercent;
    }

    public double getBuyPercent() {
        return buyPercent;
    }

    public double getBuyWPercent() {
        return buyWPercent;
    }

    public double getBuyLPercent() {
        return buyLPercent;
    }

    public double getShortPercent() {
        return shortPercent;
    }

    public double getShortWPercent() {
        return shortWPercent;
    }

    public double getShortLPercent() {
        return shortLPercent;
    }

    public int getOpenTrades() {
        return openTrades;
    }

    public int getWinners() {
        return winners;
    }

    public int getLosers() {
        return losers;
    }

    public int getNeutral() {
        return neutral;
    }

    public int getBuy() {
        return buy;
    }

    public int getBuyW() {
        return buyW;
    }

    public int getBuyL() {
        return buyL;
    }

    public int getShorts() {
        return shorts;
    }

    public int getShortW() {
        return shortW;
    }

    public int getShortL() {
        return shortL;
    }

    // ================================= SETTERS ================================

    public void setCurrentInvestment(double currentInvestment) {
        this.currentInvestment = currentInvestment;
    }

    public void setInvestment(double investment) {
        this.investment = investment;
    }

    public void setOpenPL(double openPL) {
        this.openPL = openPL;
    }

    public void setOpenPLPercent(double openPLPercent) {
        this.openPLPercent = openPLPercent;
    }

    public void setLossPercent(double lossPercent) {
        this.lossPercent = lossPercent;
    }

    public void setWinPercent(double winPercent) {
        this.winPercent = winPercent;
    }

    public void setNeutralPercent(double neutralPercent) {
        this.neutralPercent = neutralPercent;
    }

    public void setBuyPercent(double buyPercent) {
        this.buyPercent = buyPercent;
    }

    public void setBuyWPercent(double buyWPercent) {
        this.buyWPercent = buyWPercent;
    }

    public void setBuyLPercent(double buyLPercent) {
        this.buyLPercent = buyLPercent;
    }

    public void setShortPercent(double shortPercent) {
        this.shortPercent = shortPercent;
    }

    public void setShortWPercent(double shortWPercent) {
        this.shortWPercent = shortWPercent;
    }

    public void setShortLPercent(double shortLPercent) {
        this.shortLPercent = shortLPercent;
    }

    public void setOpenTrades(int openTrades) {
        this.openTrades = openTrades;
    }

    public void setWinners(int winners) {
        this.winners = winners;
    }

    public void setLosers(int losers) {
        this.losers = losers;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public void setBuyW(int buyW) {
        this.buyW = buyW;
    }

    public void setBuyL(int buyL) {
        this.buyL = buyL;
    }

    public void setShorts(int shorts) {
        this.shorts = shorts;
    }

    public void setShortW(int shortW) {
        this.shortW = shortW;
    }

    public void setShortL(int shortL) {
        this.shortL = shortL;
    }
}