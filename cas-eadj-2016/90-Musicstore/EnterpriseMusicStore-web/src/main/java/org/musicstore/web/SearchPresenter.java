package org.musicstore.web;

import org.musicstore.model.entities.Album;
import org.musicstore.repositories.AlbumQuery;
import org.musicstore.repositories.AlbumRepository;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class SearchPresenter implements Serializable {

    @Inject private AlbumRepository albumRepository;
    private List<Album> result;

    private String title;
    private String genreName;

    // @PostConstruct
    public void performSearch(){
        AlbumQuery albumQuery = new AlbumQuery();
        albumQuery.setTitle(title);
        albumQuery.setGenreName(genreName);

        result = albumRepository.getAlbums(albumQuery);
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
        this.title = "";
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
        this.genreName = "";
    }

    public List<Album> getResult(){
        return result;
    }
}

