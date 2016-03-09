package org.musicstore.domain;



import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.entities.MusicOrder;

import java.util.List;


public interface OrderService {
    public MusicOrder getCurrentOrder();
    public void addAlbums(List<Album> albums);
    public void submitCurrentOrder();

}
