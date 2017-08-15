package com.company.utils.InitStrategy;

import com.company.Item;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class {@code ItemInitStrategy} is a specific strategy of sales object initialization
 * This class is able to retrieve information about item from related CSV file and initialize this item
 * as an object
 * @see com.company.utils.InitStrategy.InitStrategy
 * @author Leonid Shelest
 */
public class ItemInitStrategy implements InitStrategy<Item> {

    /**
     * Returns name of CSV file containing information about {@code Item}
     * Keep in mind the relative file path will be retrieved
     * @return String value of CSV file name
     */
    @Override
    public String getFileName() {
        return "src/Items.csv";
    }

    /**
     Returns an initialized {@code Item} object with values retrieved while parsing a line of text file
     @return {@code Item}
     */
    @Override
    public Item parseLine(String[] line) {
        DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("d.M.yyyy H:mm:ss");
        int id = Integer.valueOf(line[0]);
        String title = line[1];
        int code = Integer.valueOf(line[2]);
        String producer = line[3];
        LocalDateTime dateOfLastUpdate = LocalDateTime.parse(line[4], oldFormat);
        Item item = new Item(id, title);
        item.setDateOfLastUpdate(dateOfLastUpdate);
        item.setCode(code);
        item.setProducer(producer);
        return item;
    }
}