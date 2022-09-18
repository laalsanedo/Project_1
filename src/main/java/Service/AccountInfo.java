package Service;

import DAO.UserInfo;
import Model.Account;

public class AccountInfo {
    private Account account;
    private UserInfo userInfo;

    //================================ CONSTRUCTORS ================================

    //NO-PARAMS Constructor;
    public AccountInfo(){
        userInfo = new UserInfo();
    }

    //================================ AUTHENTICATION ================================

    //Checks the user's username and password and returns an account object. The method also initializes an account object with account info.
    public boolean authentication(String username, String password)  {
        userInfo = new UserInfo();
        return userInfo.checkCredentials(username, password);
    }

    //Check whether the user exists:
    public boolean checkUsername(String username){
        return userInfo.checkUsername(username);
    }

    //================================ CREATING A NEW USER ================================

    //Creates a new user
    public boolean newUser(String username, String password) {
        userInfo = new UserInfo();
        account = new Account();
        if (!userInfo.checkUsername(username)){
            userInfo.createNewAccount(username, password);
            account.setUsername(username);
            account.setID(userInfo.getID(username));
            account.setTotalBalance(userInfo.getTotalBalance(username));
            account.setBuyingPower(userInfo.getBuyingPower(username));
            return true;
        }

        return false;
    }

    //================================ UPDATE BALANCES ================================

    //Update/Refresh TotalBalance --> used when closing and refreshing trades
    public boolean updateBalance(String username, double PL)  {
        return userInfo.updateBalance(username, PL);
    }

    //Update the buying power after the trade closes and brings back the buying power close to or at the total balance.
    public boolean updatingBuyingPower(String username, double investment, double PL, int transaction_type) {
        return userInfo.updatingBuyingPower(username, investment, PL, transaction_type);
    }

    //Refresh BuyingPower -->used when refreshing trade because we don't want to add back the investment
    // because the trades are still open and investment is still tied up in securities.

    public boolean refreshBuyingPower(String username, double PL) {
        return userInfo.refreshBuyingPower(username, PL);
    }

    //================================ Getters ================================

    //Get a Account object back
    public Account getAccount(String username)  {
        account = new Account(username);
        account.setID(userInfo.getID(username));
        account.setTotalBalance(userInfo.getTotalBalance(username));
        account.setBuyingPower(userInfo.getBuyingPower(username));
        return account;
    }

    //Gets the account ID
    public int getID(String username)  {
        return userInfo.getID(username);
    }

    //Gets the  account total balance
    public double getTotalBalance(String username)  {
        return userInfo.getTotalBalance(username);
    }

    //Gets the account buying power
    public double getBuyingPower(String username)  {
        return userInfo.getBuyingPower(username);
    }

}