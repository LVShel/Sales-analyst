package com.company.utils.InitStrategy;

import com.company.Customer;
import com.company.utils.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Home on 13.08.2017.
 */
public class CustomerInitStrategy implements InitStrategy<Customer> {

    @Override
    public String getFileName() {
        return "src/Customers.csv";
    }

    @Override
    public Customer parseLine(String[] parts) {
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

        return customer;
    }
}

