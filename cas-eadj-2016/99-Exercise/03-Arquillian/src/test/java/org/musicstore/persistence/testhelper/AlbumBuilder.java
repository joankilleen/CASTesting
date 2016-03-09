package org.musicstore.persistence.testhelper;


import org.musicstore.persistence.entities.Album;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AlbumBuilder {

    @PersistenceContext(unitName = "musicstore")
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
        em.persist(album);
        em.flush();
        return album;
    }

}
