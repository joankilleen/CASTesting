package org.musicstore.domain.repositories;

import org.musicstore.domain.entities.Album;
import org.musicstore.domain.queries.AlbumQuery;

import java.util.List;

public interface AlbumRepository extends BaseRepository {
    public Album getAlbum(Long id);
    public List<Album> getAlbums(AlbumQuery albumQuery);
}
