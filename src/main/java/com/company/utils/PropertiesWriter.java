package com.company.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Class {@code PropertiesWriter} is a util tool for storing <>store</> database access properties in a separate
 * config.properties file
 * @author Leonid Shelest
 */
public class PropertiesWriter {
    public static void write(){
        Properties properties = new Properties();

        try(OutputStream output = new FileOutputStream("src/config.properties")) {
            properties.setProperty("user", "root");
            properties.setProperty("password", "root");
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
