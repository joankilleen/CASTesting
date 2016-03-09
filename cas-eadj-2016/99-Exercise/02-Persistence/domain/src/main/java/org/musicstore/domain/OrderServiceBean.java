package org.musicstore.domain;

import org.musicstore.domain.entities.Album;
import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.domain.entities.OrderItem;
import org.musicstore.domain.repositories.MusicOrderRepository;

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

    @Inject
    private PriceCalculator priceCalculator;

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

        double finalAmount = priceCalculator.calculatePrice(currentOrder);
        getCurrentOrder().setFinalAmount(finalAmount);
    }

    @Override
    public void submitCurrentOrder() {
        currentOrder.setFinalAmount(currentOrder.getTotalAmount());
        orderRepository.persist(currentOrder);
    }
}
