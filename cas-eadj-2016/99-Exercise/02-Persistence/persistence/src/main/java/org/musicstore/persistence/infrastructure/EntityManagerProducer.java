package org.musicstore.persistence.infrastructure;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {
    @PersistenceContext(unitName = "EnterpriseMusicStore")
    @Produces
    private EntityManager em;
}
