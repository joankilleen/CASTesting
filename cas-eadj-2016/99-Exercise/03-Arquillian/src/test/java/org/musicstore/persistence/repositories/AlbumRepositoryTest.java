package org.musicstore.persistence.repositories;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.musicstore.persistence.AlbumRepository;
import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.testhelper.AlbumBuilder;

import java.util.List;
import java.util.logging.Logger;
import org.jboss.arquillian.persistence.UsingDataSet;

@RunWith(Arquillian.class)
//@UsingDataSet("datasets/musicstore.xml")
public class AlbumRepositoryTest extends BaseTest {
    private static final Logger LOG = Logger.getLogger(AlbumRepositoryTest.class.getName());
    
    @Test
    @UsingDataSet("musicstore.xml")
    public void shouldFindAlbumById() {
        LOG.info("Test shouldFindAlbumById");
        List<Album> albums = albumRepository.getAlbums();
        for (Album album: albums){
            System.out.println("Album: " + album.getName() + " " + album.getId());
        }
        //assertThat(albums, hasSize(greaterThan(0)));

        //Album album = albumRepository.getAlbum(albums.get(0).getId());
        /*assertThat(album, is(notNullValue()));
        for (Album albumNext: albums){
            System.out.println("Album: " + albumNext.getId() + " " + albumNext.getName());
        }*/
 
    }
}

