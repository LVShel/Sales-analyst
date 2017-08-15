package com.company.utils.InitStrategy;

/**
 * Class {@code InitStrategy} is a specific type of sales objects initialization service
 * This service is able to retrieve statistical data from CSV files and initialize sales
 * entities for their further analysis
 * @param <T> describes objects of sales analysis, i.e. {@code Customer}, {@code Item}
 * @author Leonid Shelest
 */
public interface InitStrategy<T> {
    /**
     * Returns name of CSV file containing information about objects of analysis
     * Keep in mind the relative file path will be retrieved
     * @return String value of CSV file name
     */
    String getFileName();

    /**
     Returns an object of sales analysis initialized with values retrieved from one line of text file
     @return Object of sales analysis

     */
    T parseLine(String[] parts);
}
