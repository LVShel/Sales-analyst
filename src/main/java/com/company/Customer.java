package com.company;

import com.company.utils.Gender;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Home on 06.08.2017.
 */
public class Customer {
    private int id;
    private String name;
    private LocalDate DateOfBirth;
    private String address;
    private Gender gender;
    private String phoneNumber;
    private List<Integer> lastPurchases;
    private LocalDate dateOfLastPurchase;
    private static int numberOfCustomers = 0;

    public Customer() {
    }

    public Customer(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.id = ++numberOfCustomers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getLastPurchases() {
        return lastPurchases;
    }

    public void setLastPurchases(List<Integer> lastPurchases) {
        this.lastPurchases = lastPurchases;
    }

    public LocalDate getDateOfLastPurchase() {
        return dateOfLastPurchase;
    }

    public void setDateOfLastPurchase(LocalDate dateOfLastPurchase) {
        this.dateOfLastPurchase = dateOfLastPurchase;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }
}
