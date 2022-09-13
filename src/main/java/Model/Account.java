package Model;

public class Account {
    int ID;
    double totalBalance, buyingPower;
    String username;

    //Constructor: assigns all the username for the account;
    public Account(int ID, String username, double totalBalance, double buyingPower){
        this.ID = ID;
        this.username = username;
        this.totalBalance = totalBalance;
        this.buyingPower = buyingPower;
    }

    //Constructor: assigns the username and password for the account;
    public Account(String username){
        this.username = username;
    }

    //Constructor: NO-PARAMS;
    public Account(){}

    //=========================== GETTERS ===========================

    //Gets the account ID
    public int getID() {
        return ID;
    }

    //Gets the username
    public String getUsername() {
        return username;
    }

    //Gets the  account total balance
    public double getTotalBalance() {
        return totalBalance;
    }

    //Gets the account buying power
    public double getBuyingPower() {
        return buyingPower;
    }

    //=========================== SETTERS ===========================
    //Sets the account ID
    public void setID(int ID) {
        this.ID = ID;
    }
    
    //Sets the username
    public void setUsername(String username) {
        this.username = username;
    }

    //Sets the account total balance
    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    //Sets the account buying power
    public void setBuyingPower(double buyingPower) {
        this.buyingPower = buyingPower;
    }

}