package Util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connectivity {

    private static final String url = "jdbc:sqlserver://patelr55.database.windows.net:1433;database=db1;" +
                                        "encrypt=true;trustServerCertificate=false;" +
                                        "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static final String username = "revature";
    private static final String password = "Password@123";

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