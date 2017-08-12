package com.company.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Home on 11.08.2017.
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
