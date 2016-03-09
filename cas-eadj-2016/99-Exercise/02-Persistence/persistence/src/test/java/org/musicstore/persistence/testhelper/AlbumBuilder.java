package org.musicstore.persistence.testhelper;


import org.musicstore.domain.entities.Album;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class AlbumBuilder {

    @Inject
    private EntityManager em;
    private Album album;

    public AlbumBuilder create(){
        this.album = new Album();
        return this;
    }

    public AlbumBuilder withName(String name){
        this.album.setName(name);
        return this;
    }

    public Album persist(){
        em.getTransaction().begin();
        em.persist(album);
        em.flush();
        em.getTransaction().commit();

        return album;
    }

}
