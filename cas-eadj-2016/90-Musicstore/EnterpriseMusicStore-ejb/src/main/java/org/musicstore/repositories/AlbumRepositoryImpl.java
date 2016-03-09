package org.musicstore.repositories;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.musicstore.model.entities.Album;

@Stateless(name = "AlbumRepository")
@Local(AlbumRepositoryLocal.class)
@Remote(AlbumRepository.class)
public class AlbumRepositoryImpl implements AlbumRepository, Serializable {

    @PersistenceContext(unitName = "EnterpriseMusicStore")
    private EntityManager em;

    @Override
    public List<Album> getAlbums(AlbumQuery albumQuery) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Album> criteria = cb.createQuery(Album.class);
        Root<Album> album = criteria.from(Album.class);

        final String title = albumQuery.getTitle();
        final String genreName = albumQuery.getGenreName();
        if (title != null && !"".equals(title))
            criteria.select(album).where(cb.equal(album.get("name"), title));

        else if (genreName != null && !"".equals(genreName))
            criteria.select(album).where(cb.equal(album.get("genre").get("name"), genreName));

        List<Album> albumSearchResult = em.createQuery(criteria).getResultList();
        return albumSearchResult;
    }

    @Override
    public Album getAlbum(Long id) {
        Album album = (Album) em.createNamedQuery(Album.FIND_BY_ID).setParameter("id", id).getSingleResult();
        return album;
    }
}
