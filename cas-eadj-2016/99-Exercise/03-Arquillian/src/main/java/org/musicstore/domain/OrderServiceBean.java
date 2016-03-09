package org.musicstore.domain;



import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.entities.MusicOrder;
import org.musicstore.persistence.entities.OrderItem;
import org.musicstore.persistence.MusicOrderRepository;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Stateful
@Remote(OrderService.class)
public class OrderServiceBean implements OrderService, Serializable {

    @Inject
    private MusicOrderRepository orderRepository;

    
    private MusicOrder currentOrder;

    @Override
    public MusicOrder getCurrentOrder() {

        if (currentOrder == null)
            currentOrder = new MusicOrder();

        return currentOrder;
    }

    public void setCurrentOrder(MusicOrder musicOrder){
        currentOrder = musicOrder;
    }

    @Override
    public void addAlbums(List<Album> albums) {

        for(Album album : albums) {
            OrderItem orderItem = new OrderItem();
            orderItem.setAlbum(album);
            currentOrder.getOrderItems().add(orderItem);
        }

    }

    @Override
    public void submitCurrentOrder() {
        currentOrder.setFinalAmount(currentOrder.getTotalAmount());
        orderRepository.persist(currentOrder);
    }
}
