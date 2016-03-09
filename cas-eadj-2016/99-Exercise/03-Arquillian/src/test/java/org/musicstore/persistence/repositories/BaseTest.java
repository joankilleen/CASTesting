/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.musicstore.persistence.repositories;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.musicstore.persistence.AlbumRepository;
import org.musicstore.persistence.MusicOrderRepository;
import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.entities.MusicOrder;
import org.musicstore.persistence.entities.OrderItem;
import org.musicstore.persistence.testhelper.AlbumBuilder;
import org.musicstore.persistence.testhelper.MusicOrderBuilder;

/**
 *
 * @author Joan
 */
public class BaseTest {
    @Inject
    AlbumBuilder albumBuilder;
    
    protected Connection con;
    protected IDatabaseConnection dbConnection;
    protected FlatXmlDataSet dataSet;
    
    private final String CONNECTION_STRING = "";
    private final String USER = "";
    private final String PASSWORD = "";
    @Inject
    UserTransaction utx;
    
    @Inject
    MusicOrderBuilder orderBuilder;
    
    @EJB 
    protected AlbumRepository albumRepository;
    
    @EJB 
    protected MusicOrderRepository musicOrderRepository;
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(AlbumRepository.class,
                        MusicOrderRepository.class,
                        Album.class, AlbumBuilder.class,
                        MusicOrder.class, OrderItem.class, MusicOrderBuilder.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    /*@Deployment
    public static Archive<?> createDeploymentPackage() {
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Dept.class.getPackage())
                .addClass(HumanResourcesBean.class)
                .addAsResource("datasets/addDept-expected.xml") // to be loaded by DBUnit on the server side
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
//        System.out.println(webArchive.toString(true));
        return webArchive;
    }*/
    @Before
    public void setUp() throws Exception {
        //utx.begin();
        //albumBuilder.create().withName("Hells Bells").persist();
        //orderBuilder.create().persist();
        //utx.commit();
    }
    
}
