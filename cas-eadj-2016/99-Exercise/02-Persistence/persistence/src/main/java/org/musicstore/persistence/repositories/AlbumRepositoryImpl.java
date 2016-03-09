package org.musicstore.persistence.repositories;

import org.musicstore.domain.repositories.AlbumRepository;
import org.musicstore.domain.entities.Album;
import org.musicstore.domain.queries.AlbumQuery;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.List;


@Stateless
@Named
public class AlbumRepositoryImpl extends BaseRepositoryImpl implements AlbumRepository {

    @Override
    public Album getAlbum(Long id) {
        Album album = (Album) em.createNamedQuery(Album.FIND_BY_ID).setParameter("id", id).getSingleResult();
        return album;
    }

    @Override
    public List<Album> getAlbums(AlbumQuery albumQuery) {
        return null;
    }
}
