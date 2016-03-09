package org.musicstore.domain.entities;

import javax.persistence.*;

@Entity
public class Album {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private double price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
