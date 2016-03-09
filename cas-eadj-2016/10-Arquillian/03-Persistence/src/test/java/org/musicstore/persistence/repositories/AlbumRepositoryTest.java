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

@RunWith(Arquillian.class)
public class AlbumRepositoryTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(AlbumRepository.class,
                        Album.class, AlbumBuilder.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    AlbumRepository albumRepository;

    @Inject
    AlbumBuilder albumBuilder;

    @Inject
    UserTransaction utx;

    @Before
    public void setUp() throws Exception {
        utx.begin();
        albumBuilder.create().withName("Hells Bells").persist();
        utx.commit();
    }

    @Test
    public void shouldFindAlbumById() {
        List<Album> albums = albumRepository.getAlbums();
        assertThat(albums, hasSize(greaterThan(0)));

        Album album = albumRepository.getAlbum(albums.get(0).getId());
        assertThat(album, is(notNullValue()));
    }
}

