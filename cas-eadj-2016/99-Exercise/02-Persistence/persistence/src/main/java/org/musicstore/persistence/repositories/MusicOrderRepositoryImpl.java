package org.musicstore.persistence.repositories;

import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.domain.repositories.MusicOrderRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Named
public class MusicOrderRepositoryImpl extends BaseRepositoryImpl implements MusicOrderRepository {

    public List<MusicOrder> getOrders(){
        List<MusicOrder> resultList = em.createNamedQuery("MusicOrder.findAll").getResultList();
        return resultList;
    }

    @Override
    public MusicOrder getOrder(Long id) {
        return em.find(MusicOrder.class, id);
    }

    public List<MusicOrder> getOrdersByEmail(String email) {

        // TODO: Implement the data access logic
        List<MusicOrder> resultList = new ArrayList<>();
        return resultList;
    }
}
