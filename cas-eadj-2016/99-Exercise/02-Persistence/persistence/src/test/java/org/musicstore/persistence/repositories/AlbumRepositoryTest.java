package org.musicstore.persistence.repositories;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.musicstore.domain.entities.Album;
import org.musicstore.persistence.testhelper.AlbumBuilder;
import org.musicstore.persistence.testhelper.TestEnvironment;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@RunWith(CdiRunner.class)
public class AlbumRepositoryTest {

    private String persistenceUnitName = TestEnvironment.inMemory().getPersistenceUnitName();

    @Inject
    AlbumRepositoryImpl albumRepository;

    @Inject
    AlbumBuilder albumBuilder;

    @Produces
    @ProducesAlternative
    EntityManager createEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        return emf.createEntityManager();
    }

    @Before
    public void setUp(){

    }

    @Test
    public void AlbumRepository_should_return_album_by_id() {
        Album albumFixture = albumBuilder.create().withName("Hells Bells").persist();

        Album album = albumRepository.getAlbum(albumFixture.getId());

        Assert.assertNotNull(album);
    }
}
