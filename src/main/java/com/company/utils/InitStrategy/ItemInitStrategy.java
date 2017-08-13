package com.company.utils.InitStrategy;

import com.company.Item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Home on 13.08.2017.
 */
public class ItemInitStrategy implements InitStrategy<Item> {

    @Override
    public String getFileName() {
        return "src/Items.csv";
    }

    @Override
    public Item parseLine(String[] parts) {
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

        return item;
    }
}