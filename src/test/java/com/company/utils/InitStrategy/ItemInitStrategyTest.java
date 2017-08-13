package com.company.utils.InitStrategy;

import com.company.Item;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by Home on 13.08.2017.
 */
public class ItemInitStrategyTest {
    @Test
    public void getFileName() throws Exception {
        ItemInitStrategy strategy = new ItemInitStrategy();
        String result = strategy.getFileName();
        assertEquals("src/Items.csv", result);
    }

    @Test
    public void parseLine() throws Exception {
        ItemInitStrategy strategy = new ItemInitStrategy();
        String[] parts = "1;cauliflower;14;Smokey Bones;10.06.2017 22:50:41".split(";");
        Item item = strategy.parseLine(parts);
        assertEquals(item.getTitle(), "cauliflower");
        assertEquals(item.getCode(), 14);
        assertEquals(item.getId(), 1);
        assertEquals(item.getProducer(), "Smokey Bones");
        assertEquals(item.getDateOfLastUpdate(), LocalDateTime.of(2017, 6, 10, 22,50,41));
    }

}