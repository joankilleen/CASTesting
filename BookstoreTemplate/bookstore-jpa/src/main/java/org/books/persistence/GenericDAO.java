package org.books.persistence;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAO<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(GenericDAO.class.getName());

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public T create(T entity) {
        log("adding entity: " + entity.getClass().toString());
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    public T find(Class<T> type, long id) {
        log("find entity with ID: " + type.getClass().toString() + " id: " + id);
        return entityManager.find(type, id);
    }

    public  T update(T entity) {
        log("update entity: " + entity.getClass().toString());
        return entityManager.merge(entity);
    }

    public void delete(T entity) {
        log("remove entity: " + entity.getClass().toString());
        entity = entityManager.merge(entity);
        entityManager.remove(entity);
    }

    protected void log(String msg) {
        logger.info(this.getClass().getSimpleName() + ": " + msg);
    }
}
