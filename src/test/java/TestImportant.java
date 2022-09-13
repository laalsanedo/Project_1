import Model.Trade;
import Service.AccountInfo;
import Service.CloseTrade;
import Service.OpenTrade;
import org.junit.Test;

public class TestImportant {
    OpenTrade openTrade = new OpenTrade();
    CloseTrade closeTrade = new CloseTrade();
    AccountInfo info = new AccountInfo();
    //testing the update balance and buying power
    @Test
    public void openTrade(){
        Trade trade = new Trade(10, info.getID("laalsanedo"), "buy", "AAPL");
        openTrade.newTrade("laalsanedo", trade);
    }

    @Test
    public void closeTrade(){
        Trade trade = openTrade.getLatestTrade("laalsanedo");
        closeTrade.closeSymbol("laalsanedo", "AAPL");
    }
}