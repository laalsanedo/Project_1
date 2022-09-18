package DAO;

import Model.Account;
import Util.Connectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo {
    private Connection connection;
    private ResultSet resultSet;

    //NO-ARGS CONSTRUCTOR
    public UserInfo(){
        connection = Connectivity.databaseConnect();
    }

    //================================ GET BASIC ACCOUNT INFO ================================

    //Get the UserID
    public int getID(String username) {
        String query = "SELECT id FROM user_info WHERE username = ?"; //Query to get the userID.
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return 0;
    }

    //Get the username based on UserID
    public String getUsername(int userID) {
        String query = "SELECT username FROM user_info WHERE id = ?"; //Query to get the userID.
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString(1);
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return null;
    }


    //Get the Total Balance
    public double getTotalBalance(String username) {
        String query = "SELECT total_balance FROM user_info WHERE username = ?"; //Query to get the totalBalance.
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble(1);
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return 0;
    }

    //Get the Buying Power
    public double getBuyingPower(String username)  {
        String query = "SELECT buying_power FROM user_info WHERE username = ?"; //Query to get the buyingPower.
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble(1);
            }
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return 0;
    }

    //================================ CREATES A NEW ACCOUNT ================================

    //Create a new Account
    public Account createNewAccount(String username, String password)  {
        String query = "INSERT INTO user_info( username, password, total_balance, buying_power)" +
                "VALUES ( ?, ?, ?, ?)"; //Inserting a new field in the user_info table
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setDouble(3, 10000);
            preparedStatement.setDouble(4, 10000);
            preparedStatement.executeUpdate();
            return new Account(username);
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return null;
    }

    //================================ CHECKS USER'S INFO ================================

    //Checks user's credentials
    public boolean checkCredentials(String username, String password) {
        String query = "SELECT username, password FROM user_info WHERE username = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return false;
    }
    //Checks if the username exsits
    public boolean checkUsername(String username) {
        String query = "SELECT username, password FROM user_info WHERE username = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return false;
    }

    //Check if enough balance
    public boolean enoughBalance(int ID, double investment){
        String query = "SELECT buying_power FROM user_info WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getDouble(1) >= investment){
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    //================================ UPDATE BALANCES ================================

    //Update used when closing
    public boolean updateBalance(String username, double PL)  {
        String query = "UPDATE user_info SET total_balance = total_balance + ? WHERE id = ?";  //update the balance to new P/L
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, PL);
            preparedStatement.setInt(2, getID(username));
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e){
            System.out.println("something went wrong while getting userID.\n"+e);
        }
        return false;
    }

    //Update the buying power after the trade closes and brings back the buying power close to or at the total balance.
    public boolean updatingBuyingPower(String username, double investment, double PL, int transaction_type)  {
        //transaction_type 1 means opening a new trade and 0 means closing a trade.
        try{
            if (transaction_type == 1){
                //Subtracting from buying power.
                String query = "UPDATE user_info SET buying_power = buying_power - ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, (PL+investment));
                preparedStatement.setInt(2, getID(username));
                preparedStatement.executeUpdate();
                return true;
            }
            if (transaction_type == 0){
                //Adding to buying power.
                String query = "UPDATE user_info SET buying_power = buying_power + ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, (PL+investment));
                preparedStatement.setInt(2, getID(username));
                preparedStatement.executeUpdate();
                return true;
            }
        }catch (SQLException e){
            System.out.println("something went wrong while updating the buying power.\n"+e);
        }
        return false;
    }

    //Refresh BuyingPower -->used when refreshing trade because we don't want to add back the investment
    // because the trades are still open and investment is still tied up in securities.
    public boolean refreshBuyingPower(String username, double PL)  {
        String query = "UPDATE user_info SET buying_power = buying_power + ? WHERE id = ?"; //update the buyingpower
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, PL);
            preparedStatement.setInt(2, getID(username));
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e){
            System.out.println("something went wrong while refreshing the buying power.\n"+e);
        }
        return false;
    }


}