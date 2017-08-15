package com.company.utils.InitStrategy;

import com.company.Customer;
import com.company.utils.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class {@code CustomerInitStrategy} is a specific type of sales objects
 * This class is able to retrieve customer's data from related CSV file and initialize a customer
 * as an object
 * @see com.company.utils.InitStrategy.InitStrategy
 * @author Leonid Shelest
 */
public class CustomerInitStrategy implements InitStrategy<Customer> {

    /**
     * Returns name of CSV file containing information about {@code Customer}
     * Keep in mind the relative file path will be retrieved
     * @return String value of CSV file name
     */
    @Override
    public String getFileName() {
        return "src/Customers.csv";
    }

    /**
     Returns an initialized {@code Customer} object with values retrieved while parsing a line of text file
     @return {@code Customer}
     */
    @Override
    public Customer parseLine(String[] line) {
        DateTimeFormatter oldFormat1 = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.US);
        DateTimeFormatter oldFormat2 = DateTimeFormatter.ofPattern("M/d/yyyy");
        List<Integer> lastPurchases = new ArrayList<>();
        String[] subParts = line[5].replaceAll("\"", "").split(",");
        for(int i = 0 ; i<subParts.length ; i++)
            lastPurchases.add(Integer.parseInt(subParts[i]));
        String name = line[0];
        LocalDate dateOfBirth = LocalDate.parse(line[1], oldFormat1);
        String address = line[2].replaceAll("\"", "");
        Gender gender = Gender.valueOf(line[3].toUpperCase());
        String phoneNumber = line[4];
        LocalDate dateOfLastPurchase = LocalDate.parse(line[6], oldFormat2);
        Customer customer = new Customer(name, gender);
        customer.setDateOfBirth(dateOfBirth);
        customer.setAddress(address);
        customer.setPhoneNumber(phoneNumber);
        customer.setDateOfLastPurchase(dateOfLastPurchase);
        customer.setLastPurchases(lastPurchases);

        return customer;
    }
}

