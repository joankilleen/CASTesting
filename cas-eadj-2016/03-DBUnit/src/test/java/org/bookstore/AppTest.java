package org.bookstore;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.bookstore.entities.Genre;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class AppTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private IDatabaseTester databaseTester;

    @Before
    public void setUp() throws Exception {
        // Init database
        emf = Persistence.createEntityManagerFactory("EnterpriseMusicStoreTest");
        em =  emf.createEntityManager();

        // Set up database to initial state
        databaseTester  = new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:test", "sa", "");

        IDataSet inputDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResourceAsStream("testdata/genres_setup.xml"));
        ReplacementDataSet dataSet = new ReplacementDataSet(inputDataSet);
        dataSet.addReplacementObject("[NULL]", null);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    @Test
    public void shouldPersistGenre() throws Exception {

        List resultList = em.createQuery("select g from Genre g").getResultList();
        assertThat(resultList.size(), is(1));

        em.getTransaction().begin();

        Genre genre = new Genre();
        genre.setName("Rock");
        em.persist(genre);

        em.flush();
        em.getTransaction().commit();

        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();

        ITable actualTable = databaseDataSet.getTable("GENRE");
        ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[]{"id"});

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResourceAsStream("testdata/genres_expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("GENRE");

        Assertion.assertEquals(expectedTable, filteredTable);
    }
}
