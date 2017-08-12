package com.company.utils;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Home on 07.08.2017.
 */
public class DBConnector {
    private final String URL = "jdbc:mysql://localhost:3306/store";
    private Properties properties = new Properties();
    private Connection connection;

    public DBConnector(){
        try{
            InputStream input =  new FileInputStream("src/config.properties");
            properties.load(input);
            connection = DriverManager.getConnection(URL, properties);
        }
        catch(FileNotFoundException fex){
            fex.printStackTrace();
            System.out.println("Cannot load properties from file");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cannot find driver");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot load properties");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
