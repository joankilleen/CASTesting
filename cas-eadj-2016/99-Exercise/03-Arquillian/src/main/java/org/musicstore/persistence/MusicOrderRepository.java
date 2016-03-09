package org.musicstore.persistence;

import java.util.ArrayList;
import org.musicstore.persistence.entities.MusicOrder;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MusicOrderRepository{
    @PersistenceContext(unitName = "musicstore")
    private EntityManager em;

    
    
    public MusicOrder getOrder(Long id) {
        return em.find(MusicOrder.class, id);
    }

    
    public List<MusicOrder> getOrders(){
        List<MusicOrder> resultList = em.createNamedQuery("MusicOrder.findAll").getResultList();
        return resultList;
    }

    public List<MusicOrder> getOrdersByEmail(String email){
        // TODO: Implement the data access logic
        List<MusicOrder> resultList = new ArrayList<>();
        return resultList;
    }
    
    public void persist(MusicOrder order){
        em.persist(this);
        em.flush();
    }
}
