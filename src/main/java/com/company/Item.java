package com.company;

import java.time.LocalDateTime;

/**
 * Class {@code Item} is a specific type of sales objects
 * This class is defined to store and retrieve information about concrete product of the store
 * @author Leonid Shelest
 */
public class Item {
    private int id;
    private String title;
    private int code;
    private String producer;
    private LocalDateTime dateOfLastUpdate;

    public Item(int id, String title, int code, String producer, LocalDateTime dateOfLastUpdate) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.producer = producer;
        this.dateOfLastUpdate = dateOfLastUpdate;
    }

    public Item() {
    }

    public Item(int id, String title, int code, String producer) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.producer = producer;
    }

    public Item(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDateTime getDateOfLastUpdate() {
        return dateOfLastUpdate;
    }

    public void setDateOfLastUpdate(LocalDateTime dateOfLastUpdate) {
        this.dateOfLastUpdate = dateOfLastUpdate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", code=" + code +
                ", producer='" + producer + '\'' +
                ", dateOfLastUpdate=" + dateOfLastUpdate +
                '}';
    }
}
