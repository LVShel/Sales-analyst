package com.company;

import com.company.utils.Gender;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class represents Sales Department of the product store.
 * It stores lists of current customers and products and has an instance of
 * database administrator (class DBAdministrator) in order to provide operations over
 * products and customers. For that options it has such methods as initCustomers(),
 * initItems(), showItems etc. on manager's request (Manager.main())
 * @author  Leonid Shelest
 */
public class SalesDepartment {
    private List<Customer> customers = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private DBAdministrator admin = new DBAdministrator();
    File customersFile = new File("src/Customers.csv");
    File itemsFile = new File("src/items.csv");

    public void initCustomers(){
        try(CSVReader reader = new CSVReader(new FileReader(customersFile), ';', '"', 1)) {
            String[] parts;
            while ((parts = reader.readNext()) != null) {
                parseCustomerLine(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initItems(){
        try(CSVReader reader = new CSVReader(new FileReader(itemsFile), ';', '"', 1)) {
            String[] parts;
            while ((parts = reader.readNext()) != null) {
                parseItemLine(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshStoreTables(){
        try {
            admin.recreateAllTables();
            admin.insertCustomersToDB(getCustomers());
            admin.insertItemsToDB(getItems());
            admin.insertSalesToDB(getCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void parseCustomerLine(String[] parts) {
        DateTimeFormatter oldFormat1 = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.US);
        DateTimeFormatter oldFormat2 = DateTimeFormatter.ofPattern("M/d/yyyy");
        List<Integer> lastPurchases = new ArrayList<>();
        String[] subParts = parts[5].replaceAll("\"", "").split(",");
        for(int i = 0 ; i<subParts.length ; i++)
            lastPurchases.add(Integer.parseInt(subParts[i]));
        String name = parts[0];
        LocalDate dateOfBirth = LocalDate.parse(parts[1], oldFormat1);
        String address = parts[2].replaceAll("\"", "");
        Gender gender = Gender.valueOf(parts[3].toUpperCase());
        String phoneNumber = parts[4];
        LocalDate dateOfLastPurchase = LocalDate.parse(parts[6], oldFormat2);
        Customer customer = new Customer(name, gender);
        customer.setDateOfBirth(dateOfBirth);
        customer.setAddress(address);
        customer.setPhoneNumber(phoneNumber);
        customer.setDateOfLastPurchase(dateOfLastPurchase);
        customer.setLastPurchases(lastPurchases);
        customers.add(customer);
    }

    void parseItemLine(String[] parts) {
        DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("d.M.yyyy H:mm:ss");
        int id = Integer.valueOf(parts[0]);
        String title = parts[1];
        int code = Integer.valueOf(parts[2]);
        String producer = parts[3];
        LocalDateTime dateOfLastUpdate = LocalDateTime.parse(parts[4], oldFormat);
        Item item = new Item(id, title);
        item.setDateOfLastUpdate(dateOfLastUpdate);
        item.setCode(code);
        item.setProducer(producer);
        items.add(item);
    }

    public void showItems(List<Item> items){
        for(Item item : items){
            System.out.println(item.toString());
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
