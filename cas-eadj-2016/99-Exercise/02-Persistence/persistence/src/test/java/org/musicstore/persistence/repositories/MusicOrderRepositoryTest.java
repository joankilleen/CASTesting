package org.musicstore.persistence.repositories;

import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ProducesAlternative;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.musicstore.domain.entities.Album;
import org.musicstore.domain.entities.MusicOrder;
import org.musicstore.persistence.testhelper.AlbumBuilder;
import org.musicstore.persistence.testhelper.TestEnvironment;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@RunWith(CdiRunner.class)
public class MusicOrderRepositoryTest {

    private static String persistenceUnitName = TestEnvironment.inMemory().getPersistenceUnitName();
    private static EntityManagerFactory emf;
    private EntityManager em;

    @Inject
    MusicOrderRepositoryImpl repository;

    @Produces
    @ProducesAlternative
    private EntityManager createEntityManager(){
        if(em == null) em = emf.createEntityManager();
        return em;
    };

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    @Before
    public void setUp() {
        em.getTransaction().begin();
    }

    @After
    public void tearDown() {
        em.getTransaction().rollback();
        em = emf.createEntityManager();
    }

    @Test
    public void shouldPersistOrder() {

        List<MusicOrder> orders = repository.getOrders();
        int orderCount = orders.size();

        repository.persist(new MusicOrder());

        orders = repository.getOrders();
        assertThat(orders.size(), is(orderCount + 1));
    }

    @Test
    public void shouldFindOrderById() {

        MusicOrder order = new MusicOrder();
        repository.persist(order);

        MusicOrder orderReloaded = repository.getOrder(order.getId());
        assertThat(orderReloaded, is(notNullValue()));
    }
}
