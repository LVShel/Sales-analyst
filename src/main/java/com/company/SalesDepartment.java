package com.company;

import com.company.utils.InitStrategy.CustomerInitStrategy;
import com.company.utils.InitStrategy.InitStrategy;
import com.company.utils.InitStrategy.ItemInitStrategy;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public void initCustomers(){
        for(Object obj : initFromFile(new CustomerInitStrategy())){
            customers.add((Customer)obj);
        }
    }

    public void initItems(){
        for(Object obj : initFromFile(new ItemInitStrategy())){
            items.add((Item)obj);
        }
    }

    public <T> List<T> initFromFile(InitStrategy<T> strategy){
        List<T> list = new ArrayList<T>();
        try(CSVReader reader = new CSVReader(new FileReader(new File(strategy.getFileName())), ';', '"', 1)) {
            String[] parts;
            while ((parts = reader.readNext()) != null) {
                list.add(strategy.parseLine(parts));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void refreshStoreTables(){
        admin.recreateAllTables();
        admin.insertCustomersToDB(getCustomers());
        admin.insertItemsToDB(getItems());
        admin.insertSalesToDB(getCustomers());
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
