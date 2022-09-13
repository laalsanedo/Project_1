package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectivity {

    private static final String url = "jdbc:postgresql://localhost:1433/TradingSimulator";
    private static final String username = "postgres";
    private static final String password = "2525";

    private static Connection connection;

    public static Connection databaseConnect(){

        try {
            if(connection != null){
                return connection;
            }
            else {
                connection = DriverManager.getConnection(url, username, password);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong while connecting to the database");
            System.out.println(e);
        }
        return connection;
    }


}
