package org.musicstore.domain;

import org.musicstore.domain.repositories.MusicOrderRepository;

import org.musicstore.domain.entities.Album;
import org.musicstore.domain.entities.MusicOrder;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

public class PriceCalculator implements Serializable {

    public static final double STANDARD_DISCOUNT_FACTOR = 0.9;
    public static final double EXTRA_DISCOUNT_FACTOR = 0.8;

    MusicOrderRepository musicOrderRepository;

    @Inject
    public PriceCalculator(MusicOrderRepository musicOrderRepository) {
        this.musicOrderRepository = musicOrderRepository;
    }

    public double calculatePrice(MusicOrder currentOrder) {

        // TODO: Use the musicOrderRepository to find out if there are already orders for the given customer (email)
        // TODO: Apply the extra discount only if there is already an order (=> returning customer)
        return currentOrder.getTotalAmount() * STANDARD_DISCOUNT_FACTOR;
    }
}
