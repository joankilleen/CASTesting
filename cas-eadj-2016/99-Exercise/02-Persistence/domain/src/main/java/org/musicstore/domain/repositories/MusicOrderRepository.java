package org.musicstore.domain.repositories;

import org.musicstore.domain.entities.MusicOrder;

import java.util.List;

public interface MusicOrderRepository extends BaseRepository {
    
    public List<MusicOrder> getOrders();
    public MusicOrder getOrder(Long id);
    public List<MusicOrder> getOrdersByEmail(String email);
}
