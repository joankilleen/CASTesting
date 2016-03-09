/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.musicstore.persistence.testhelper;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.entities.MusicOrder;
import org.musicstore.persistence.entities.OrderItem;

/**
 *
 * @author Joan
 */
public class MusicOrderBuilder {
    @PersistenceContext(unitName = "musicstore")
    private EntityManager em;
    
    private MusicOrder order;
    private OrderItem item;
    
    public MusicOrderBuilder create(){
        this.order = new MusicOrder();
        this.item = new OrderItem();
        Album  album = em.find(Album.class, 1L);
        item.setAlbum(album);
        List<OrderItem> list = new ArrayList<>();
        list.add(item);
        
        order.setOrderItems(list);
        order.setCity("Zurich");
        order.setCountry("CH");
        order.setStreet("ss");
        order.setEmail("toni@example.de");
        order.setFirstName("toni");
        
        return this;
    }
    /*
    private Album album;

    public AlbumBuilder create(){
        this.album = new Album();
        return this;
    }

    public AlbumBuilder withName(String name){
        this.album.setName(name);
        return this;
    }
    */
    public MusicOrder persist(){
        em.persist(order);
        em.flush();
        return order;
    }
    
}
