package org.musicstore.persistence;

import org.musicstore.persistence.entities.Album;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AlbumRepository {

    @PersistenceContext(unitName = "musicstore")
    private EntityManager em;

    public List<Album> getAlbums(){
        return em.createNamedQuery(Album.FIND_ALL).getResultList();
    }

    public Album getAlbum(Long albumId) {
        return em.find(Album.class, albumId);
    }
}
