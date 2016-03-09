package org.musicstore.persistence.repositories;

import org.musicstore.domain.repositories.BaseRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class BaseRepositoryImpl implements BaseRepository {

    @Inject
    protected EntityManager em;

    @Override
    public void persist(Object entity) {
        em.persist(entity);
        em.flush();
    }
}
