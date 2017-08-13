package com.company.utils.InitStrategy;

import com.company.Customer;
import com.company.utils.Gender;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by Home on 13.08.2017.
 */
public class CustomerInitStrategyTest {
    @Test
    public void parseLine() throws Exception {
        CustomerInitStrategy strategy = new CustomerInitStrategy();
        String[] parts = "Chara Pastrana;26 April 1954;\"7592 College Dr.Fishers, IN 46037\";female;(815) 203-5480;\"2,4,12\";6/1/2017".replaceAll("\"", "").split(";");
        Customer customer = strategy.parseLine(parts);
        assertEquals(customer.getName(), "Chara Pastrana");
        assertEquals(customer.getDateOfBirth(), LocalDate.of(1954, 4,26));
        assertEquals(customer.getAddress(), "7592 College Dr.Fishers, IN 46037");
        assertEquals(customer.getGender(), Gender.FEMALE);
        assertEquals(customer.getLastPurchases().size(), 3);
        assertEquals(customer.getDateOfLastPurchase(), LocalDate.of(2017, 6, 1));
    }

    @Test
    public void getFileName(){
        CustomerInitStrategy strategy = new CustomerInitStrategy();
        String result = strategy.getFileName();
        assertEquals("src/Customers.csv", result);
    }

}