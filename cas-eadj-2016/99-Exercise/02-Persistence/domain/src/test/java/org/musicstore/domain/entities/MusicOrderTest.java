package org.musicstore.domain.entities;

import org.junit.Before;
import org.junit.Test;
import org.musicstore.domain.entities.Album;
import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.domain.entities.OrderItem;

import static org.junit.Assert.assertEquals;

public class MusicOrderTest {

    private Album album1;
    private Album album2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private MusicOrder order;

    @Before
    public void setUp(){
        album1 = new Album();
        album1.setPrice(20d);
        album2 = new Album();
        album2.setPrice(10d);

        orderItem1 = new OrderItem();
        orderItem1.setAlbum(album1);
        orderItem2 = new OrderItem();
        orderItem2.setAlbum(album2);

        order = new MusicOrder();
        order.getOrderItems().add(orderItem1);
        order.getOrderItems().add(orderItem2);
    }


    @Test
    public void totalAmountShouldBeSumOfItems() throws Exception {

        assertEquals(new Double(30), order.getTotalAmount());
    }

    @Test
    public void defaultFinalAmountShouldBeTotalAmount() throws Exception {

        assertEquals(order.getTotalAmount(), order.getFinalAmount());
    }

    @Test
    public void finalAmountCanBeOverridden() throws Exception {

        order.setFinalAmount(15d);
        assertEquals(new Double(15d), order.getFinalAmount());
    }
}
