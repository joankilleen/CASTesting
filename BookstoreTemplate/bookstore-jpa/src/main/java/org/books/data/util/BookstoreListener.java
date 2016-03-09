/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.util;

import java.util.logging.Logger;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 *
 * @author Joan
 */
public class BookstoreListener {

    private static final Logger LOG = Logger.getLogger(BookstoreListener.class.getName());
    
    @PreRemove
    public void preRemove(Object entity){
        log("removing", entity);
    }
    @PostRemove
    public void postRemove(Object entity){
        log("removed", entity);
    }
    @PreUpdate   
    public void preUpdate(Object entity){
        log("updating", entity);
    }
    @PostUpdate
    public void postUpdate(Object entity){
        log("updated", entity);
    }
    
    @PrePersist
    public void prePersist(Object entity){
        log("persisting", entity);
    }
    @PostPersist
    public void postPersist(Object entity){
        log("persisted", entity);
    }
    @PostLoad
    public void postLoad(Object entity){
        log("loaded", entity);
    }
    
    public void log(String event, Object entity){
        LOG.info(event+ ": " + entity.getClass().getSimpleName() + " " + entity.toString());
    }
    
    
}
