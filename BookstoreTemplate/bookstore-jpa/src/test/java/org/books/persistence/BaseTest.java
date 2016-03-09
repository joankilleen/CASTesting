/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Joan
 */
public class BaseTest {
    
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected Connection con;
    protected IDatabaseConnection dbConnection;
    protected FlatXmlDataSet dataSet;
    
    protected static final String PERSISTENCE_UNIT = "test";
    protected static final String USER = "app";
    protected static final String PASSWORD = "app";
    protected static final String CONNECTION_STRING = "jdbc:derby:memory:test";
    protected static final Logger LOG = Logger.getLogger(BaseTest.class.getName());
    
    BookDAO bookDAO = new BookDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    LoginDAO loginDAO = new LoginDAO();
    OrderDAO orderDAO = new OrderDAO();
    SequenceServiceBean sequenceServiceBean = new SequenceServiceBean();
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeClass
    public void setup() throws Exception {
        
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        em = emf.createEntityManager();
        con = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        dbConnection = new DatabaseConnection(con);
        InputStream strm = BaseTest.class.getClassLoader().getResourceAsStream("bookstore.xml");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        dataSet = builder.build(strm);
        
        bookDAO.setEntityManager(em);
        customerDAO.setEntityManager(em);
        loginDAO.setEntityManager(em);
        orderDAO.setEntityManager(em);
        
        customerDAO.setSequenceService(sequenceServiceBean);
        orderDAO.setSequenceService(sequenceServiceBean);
        sequenceServiceBean.setCustomerDAO(customerDAO);
        sequenceServiceBean.setOrderDAO(orderDAO);
        sequenceServiceBean.setNewCustomerNumber(30000);
        sequenceServiceBean.setNewCustomerNumber(40000);
        orderDAO.setBookDAO(bookDAO);
        orderDAO.setCustomerDAO(customerDAO);
    }
    
    public void setUpCustomers() throws Exception{
        
    }

    
    @AfterClass
    public void tearDownClass() throws Exception {
        if (con != null && !con.isClosed()) {
            con.close();
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        emf.getCache().evictAll();
        DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);

    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        
    }
    public void log(String msg){
        LOG.info(this.getClass().getSimpleName() + ": " + msg);
    }
    
}

