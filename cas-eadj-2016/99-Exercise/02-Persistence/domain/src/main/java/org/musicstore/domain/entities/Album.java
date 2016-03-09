package org.musicstore.domain.entities;

import org.musicstore.domain.repositories.AlbumRepository;

import javax.persistence.*;

// Defining named queries on the entity leaks persistence concerns into the domain...
@NamedQueries({
        @NamedQuery(name = Album.FIND_ALL, query = "SELECT a FROM Album a"),
        @NamedQuery(name = Album.FIND_BY_ID, query = "SELECT a FROM Album a WHERE a.id = :id")})
@Entity
public class Album {

    public static final String FIND_ALL = "Album.findAll";
    public static final String FIND_BY_ID = "Album.findById";

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
