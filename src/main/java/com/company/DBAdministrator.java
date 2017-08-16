package com.company;

import com.company.utils.DBConnector;
import com.company.utils.Gender;
import com.company.utils.ItemRank;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the store database administrator functionality
 * It has all queries (constants) which are necessary to present proper information about customers, products
 * and sales on manager's or sales department requests. This class is responsible for database maintaining.
 * @author  Leonid Shelest
 */

public class DBAdministrator {
    private DBConnector connector = new DBConnector();

    private Connection connection = null;

    private static final String INSERT_CUSTOMERS = "INSERT INTO customers(id, name, date_of_birth, address, " +
            "gender, phone_number, date_of_last_purchase) VALUES (?,?,?,?,?,?,?)";

    private static final String INSERT_SALES = "INSERT INTO sales(customerID, itemID) VALUES (?,?)";

    private static final String INSERT_ITEMS = "INSERT INTO items(id, title, code, producer, date_of_last_update) VALUES (?,?,?,?,?)";

    private static final String DROP_SALES = "DROP TABLE IF EXISTS sales";

    private static final String DROP_CUSTOMERS = "DROP TABLE IF EXISTS customers";

    private static final String DROP_ITEMS = "DROP TABLE IF EXISTS items";

    private static final String CREATE_CUSTOMERS = "CREATE TABLE IF NOT EXISTS `customers` (\n" +
            "  `id` INT(11) NOT NULL,\n" + "  `name` VARCHAR(60) NOT NULL,\n" + "  `date_of_birth` DATE NOT NULL,\n" +
            "  `address` VARCHAR(100) NOT NULL,\n" + "  `gender` VARCHAR(6) NOT NULL,\n" +
            "  `phone_number` VARCHAR(20) DEFAULT NULL,\n" + "  `date_of_last_purchase` DATE NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" + "  KEY `gender_idx` (`gender`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";

    private static final String CREATE_ITEMS = "CREATE TABLE IF NOT EXISTS `items` (\n" + "  `id` INT(11) NOT NULL,\n" +
            "  `title` VARCHAR(100) NOT NULL,\n" + "  `code` INT(11) NOT NULL,\n" +
            "  `producer` VARCHAR(100) NOT NULL,\n" + "  `date_of_last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "  `primary_item` VARCHAR(10) NOT NULL DEFAULT 'false',\n" + "  `candidate_to_remove` VARCHAR(10) NOT NULL DEFAULT 'false',\n" +
            "  PRIMARY KEY (`id`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";

    private static final String CREATE_SALES = "CREATE TABLE IF NOT EXISTS `sales` (\n" + "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `customerID` INT(11) NOT NULL,\n" + "  `itemID` INT(11) NOT NULL,\n" + "  PRIMARY KEY (`id`),\n" +
            "  KEY `customerID_idx` (`customerID`),\n" + "  KEY `itemID_idx` (`itemID`),\n" +
            "  CONSTRAINT `customerID` FOREIGN KEY (`customerID`) REFERENCES `customers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `itemID` FOREIGN KEY (`itemID`) REFERENCES `items` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;\n";

    private static final String SELECT_POPULAR_ITEMS_SET_LIMIT = "SELECT c.id, c.title, c.code, c.producer, count(c.id) AS total_sold FROM sales a \n" +
            "RIGHT OUTER JOIN customers b ON a.customerID = b.id \n" + "JOIN items c ON a.itemID = c.id\n" +
            "GROUP BY c.title\n" + "ORDER BY total_sold DESC LIMIT ?";

    private static final String SELECT_UNPOPULAR_ITEMS_SET_LIMIT = "SELECT c.id, c.title, c.code, c.producer, count(c.id) AS total_sold FROM sales a \n" +
            "RIGHT OUTER JOIN customers b ON a.customerID = b.id \n" + "JOIN items c ON a.itemID = c.id\n" +
            "GROUP BY c.title\n" + "ORDER BY total_sold ASC LIMIT ?";

    private static final String SELECT_POPULAR_ITEMS_BY_CUSTOMER_GENDER = "SELECT c.id, c.title, c.code, c.producer, count(c.id) AS total_sold FROM sales a \n" +
            "RIGHT OUTER JOIN customers b ON a.customerID = b.id \n" + "JOIN items c ON a.itemID = c.id\n" +
            "WHERE b.gender = ? \n" + "GROUP BY c.title\n" + "ORDER BY count(c.id) DESC LIMIT ?;\n";

    private static final String SELECT_POPULAR_ITEMS_ON_PERIOD = "SELECT c.id, c.title, c.code, c.producer, count(c.id) AS total_sold FROM sales a\n" +
            "RIGHT OUTER JOIN customers b ON a.customerID = b.id \n" + "JOIN items c ON a.itemID = c.id\n" +
            "WHERE b.date_of_last_purchase BETWEEN ? AND ? \n" + "GROUP BY c.title\n" + "ORDER BY total_sold DESC LIMIT ?";

    private static final String UPDATE_PRIMARY_ITEMS = "UPDATE items SET primary_item = 'true' WHERE id = ?";

    private static final String UPDATE_CANDIDATES_TO_REMOVE = "UPDATE items SET candidate_to_remove = 'true' WHERE id = ?";

    public void insertCustomersToDB(List<Customer> customers){
        connection = connector.getConnection();

        try (PreparedStatement preparedStmt = connection.prepareStatement(INSERT_CUSTOMERS)) {
            connection.setAutoCommit(false);
            if (customers != null) {
                for (Customer customer : customers) {
                    preparedStmt.setInt(1, customer.getId());
                    preparedStmt.setString(2, customer.getName());
                    preparedStmt.setDate(3, java.sql.Date.valueOf(customer.getDateOfBirth()));
                    preparedStmt.setString(4, customer.getAddress());
                    preparedStmt.setString(5, customer.getGender().name());
                    preparedStmt.setString(6, customer.getPhoneNumber());
                    preparedStmt.setDate(7, java.sql.Date.valueOf(customer.getDateOfLastPurchase()));
                    preparedStmt.execute();
                }
            } else connection.rollback();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void insertSalesToDB(List<Customer> customers){
        connection = connector.getConnection();

        try (PreparedStatement preparedStmt = connection.prepareStatement(INSERT_SALES)) {
            connection.setAutoCommit(false);
            if (customers != null) {
                for (Customer customer : customers){
                    for (Integer soldItem : customer.getLastPurchases()){
                        preparedStmt.setInt(1, customer.getId());
                        preparedStmt.setInt(2, soldItem);
                        preparedStmt.execute();
                    }
                }
            } else {
                connection.rollback();
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void insertItemsToDB(List<Item> items){
        connection = connector.getConnection();

        try (PreparedStatement preparedStmt = connection.prepareStatement(INSERT_ITEMS)) {
            connection.setAutoCommit(false);
            if (items != null) {
                for (Item item : items) {
                    preparedStmt.setInt(1, item.getId());
                    preparedStmt.setString(2, item.getTitle());
                    preparedStmt.setInt(3, item.getCode());
                    preparedStmt.setString(4, item.getProducer());
                    preparedStmt.setTimestamp(5, Timestamp.valueOf(item.getDateOfLastUpdate()));
                    preparedStmt.execute();
                }
            } else {
                connection.rollback();
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void recreateAllTables(){
        connection = connector.getConnection();

        try(Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.addBatch(DROP_SALES);
            statement.addBatch(DROP_CUSTOMERS);
            statement.addBatch(DROP_ITEMS);
            statement.addBatch(CREATE_CUSTOMERS);
            statement.addBatch(CREATE_ITEMS);
            statement.addBatch(CREATE_SALES);
            statement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public List<Item> getItemsByRank(int limit, ItemRank rank) {
        List<Item> items = null;
        connection = connector.getConnection();
        ResultSet rs;

        try {
            connection.setAutoCommit(false);
            switch (rank) {
                case POPULAR:
                    PreparedStatement popularstmt = connection.prepareStatement(SELECT_POPULAR_ITEMS_SET_LIMIT);
                    popularstmt.setInt(1, limit);
                    rs = popularstmt.executeQuery();
                    connection.commit();
                    items = setItemValues(rs);
                    popularstmt.close();
                    break;
                case UNPOPULAR:
                    PreparedStatement unpopularstmt = connection.prepareStatement(SELECT_UNPOPULAR_ITEMS_SET_LIMIT);
                    unpopularstmt.setInt(1, limit);
                    rs = unpopularstmt.executeQuery();
                    connection.commit();
                    items = setItemValues(rs);
                    unpopularstmt.close();
                    break;
            }
            return items;
        } catch (SQLException e) {
            System.out.println("Problem in method getItemsByRank()");
            e.printStackTrace();
            return null;
        }
    }

    public List<Item> getPopularItemsByCustomersGender(Gender gender, int limit) {
        List<Item> items;
        connection = connector.getConnection();

        try (PreparedStatement prepstmt = connection.prepareStatement(SELECT_POPULAR_ITEMS_BY_CUSTOMER_GENDER)) {
            connection.setAutoCommit(false);
            prepstmt.setString(1, gender.name());
            prepstmt.setInt(2, limit);
            ResultSet resultSet = prepstmt.executeQuery();
            connection.commit();
            items = setItemValues(resultSet);
            return items;
        } catch (SQLException e) {
            System.out.println("Problem in method getPopularItemsByCustomersGender()");
            e.printStackTrace();
            return null;
        }

    }

    public List<Item> getPopularItemsOfPeriod(LocalDate startDate, LocalDate endDate, int limit) {
        List<Item> items;
        connection = connector.getConnection();

        try (PreparedStatement prepstmt = connection.prepareStatement(SELECT_POPULAR_ITEMS_ON_PERIOD)) {
            connection.setAutoCommit(false);
            prepstmt.setDate(1, java.sql.Date.valueOf(startDate));
            prepstmt.setDate(2, java.sql.Date.valueOf(endDate));
            prepstmt.setInt(3, limit);
            ResultSet resultSet = prepstmt.executeQuery();
            connection.commit();
            items = setItemValues(resultSet);
            return items;
        } catch (SQLException e) {
            System.out.println("Problem in method getPopularItemsOfPeriod()");
            e.printStackTrace();
            return null;
        }
    }

    public void markPopularItemsInDB(int limit){
        connection = connector.getConnection();

        try (PreparedStatement primaryPrepstmt = connection.prepareStatement(UPDATE_PRIMARY_ITEMS)) {
            connection.setAutoCommit(false);
            List<Item> primaries = getItemsByRank(limit, ItemRank.POPULAR);

            for (Item item : primaries) {
                primaryPrepstmt.setInt(1, item.getId());
                primaryPrepstmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Problem in method markPopularItemsInDB()");
            e.printStackTrace();
            if (connection != null) try {
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Unable to rollback in method markPopularItemsInDB()");
                e1.printStackTrace();
            }
        }
    }

    public void markUnpopularItemsInDB(int limit){
        connection = connector.getConnection();

        try (PreparedStatement outsidersPrepstmt = connection.prepareStatement(UPDATE_CANDIDATES_TO_REMOVE)) {
            connection.setAutoCommit(false);
            List<Item> outsiders = getItemsByRank(limit, ItemRank.UNPOPULAR);

            for (Item item : outsiders) {
                outsidersPrepstmt.setInt(1, item.getId());
                outsidersPrepstmt.executeUpdate();
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println("Problem in method markUnpopularItemsInDB()");
            e.printStackTrace();
            if (connection != null) try {
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Unable to rollback in method markUnpopularItemsInDB()");
                e1.printStackTrace();
            }
        }
    }

    public void writePopularItemsToFile(int limit){
        connection = connector.getConnection();

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("src/Primary_Items.csv"), ';', CSVWriter.NO_QUOTE_CHARACTER);
             PreparedStatement stmt = connection.prepareStatement(SELECT_POPULAR_ITEMS_SET_LIMIT)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            csvWriter.writeAll(rs, true);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUnpopularItemsToFile(int limit){
        connection = connector.getConnection();

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("src/Candidates_To_Remove.csv"), ';', CSVWriter.NO_QUOTE_CHARACTER);
             PreparedStatement stmt = connection.prepareStatement(SELECT_UNPOPULAR_ITEMS_SET_LIMIT)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            csvWriter.writeAll(rs, true);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Item> setItemValues(ResultSet resultset){
        List<Item> items = new ArrayList<>();

        try{
            while (resultset.next()) {
                Item item = new Item();

                item.setId(resultset.getInt(1));
                item.setTitle(resultset.getString(2));
                item.setCode(resultset.getInt(3));
                item.setProducer(resultset.getString(4));
                items.add(item);
            }
            return items;
        }catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
}







