import Model.Trade;
import Service.AccountInfo;
import Service.CloseTrade;
import Service.OpenTrade;
import org.junit.Test;

public class TestImportant {
    OpenTrade openTrade = new OpenTrade();
    CloseTrade closeTrade = new CloseTrade();
    AccountInfo info = new AccountInfo();

    @Test
    public void openTrade(){
        Trade trade = new Trade(10, info.getID("laalsanedo"), "buy", "AAPL");
        openTrade.newTrade("laalsanedo", trade);
    }

    @Test
    public void closeTrade(){
        Trade trade = openTrade.getLatestTrade("laalsanedo");
        closeTrade.closeAllTrades("laalsanedo");
    }

    @Test
    public void updateTrade(){
        openTrade.updateOpenTradePrices("laalsanedo");
    }
}