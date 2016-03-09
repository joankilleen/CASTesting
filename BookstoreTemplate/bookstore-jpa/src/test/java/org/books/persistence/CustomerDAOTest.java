/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import org.books.data.CreditCardType;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;


/**
 *
 * @author guthei
 */
public class CustomerDAOTest extends BaseTest {
    CustomerDTO newCustomer = new CustomerDTO();
    
    @Test
    public void testCreate() {
        newCustomer.setEmail("muster1@bfh.ch");
        newCustomer.setFirstName("Hans");
        newCustomer.setLastName("Meier");
        newCustomer.setAddress(new AddressDTO("Musterweg", "Bern", "3003", "CH"));
        newCustomer.setCreditCard(new CreditCardDTO(CreditCardType.MasterCard, "400000000000006", 1, 2017));
        customerDAO.setEntityManager(em);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        
        newCustomer = customerDAO.create(newCustomer);
        log("create entity: " + newCustomer.getEmail());
        transaction.commit();
        assertNotNull(newCustomer);
        log("New Customer created: " +  " Email: " + newCustomer.getEmail()+
            " FirstName: " + newCustomer.getFirstName() + " LastName: " + newCustomer.getLastName());
    }

    @Test(dependsOnMethods = "searchByNameToken")
    public void find() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        CustomerDTO customer1 = customerDAO.findByEmail("gisler@example.org");
        transaction.commit();
        em.clear();
        assertNotNull(customer1);
        log("Found customer by email: " + " Email: " + customer1.getEmail() +
            " FirstName: " + customer1.getFirstName() + " LastName: " + customer1.getLastName());
    }

    @Test(dependsOnMethods = "find")
    public void testUpdate() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        newCustomer = customerDAO.findByEmail("gisler@example.org");
        newCustomer.setFirstName("Ruedi");
        newCustomer.setAddress(new AddressDTO("Alpenweg 100", "Thun", "1015-6", "CH"));
        newCustomer.setCreditCard(new CreditCardDTO(CreditCardType.Visa, "1234123412341234", 1, 2017));
        newCustomer = customerDAO.update(newCustomer);
        transaction.commit();
        em.clear();
        log("Customer updated: " +  " Email: " + newCustomer.getEmail() +
            " FirstName: " + newCustomer.getFirstName() + " LastName: " + newCustomer.getLastName());
        newCustomer = customerDAO.findByEmail("gisler@example.org");
        assertTrue("Ruedi".equals(newCustomer.getFirstName()));
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() {
        
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        customerDAO.delete(newCustomer);
        transaction.commit();
        em.clear();
        newCustomer = customerDAO.findByEmail(newCustomer.getEmail());
        assertTrue(newCustomer==null);
    }

    @Test(dependsOnMethods = "testCreate")
    private void searchByNameToken() {
        // Find a customer by first and lastname
        EntityTransaction t = em.getTransaction();
        t.begin();
        List<CustomerInfo> customers = customerDAO.search("s");
        t.commit();
        em.clear();
        for (CustomerInfo customer : customers) {
            log("Customer found: " + customer.getFirstName() + " " + customer.getLastName());
        }
    }
    
    @Test(dependsOnMethods = "searchByNameToken")
    private void uniqueEmailTest(){
        // exists already
        
        newCustomer.setEmail("susanne@example.org");
        log("Unique Email Test: " + newCustomer.getEmail());
        newCustomer.setFirstName("Susanne");
        newCustomer.setLastName("Example");
        newCustomer.setAddress(new AddressDTO("Musterweg", "Bern", "3003", "CH"));
        newCustomer.setNumber("5000");
        newCustomer.setCreditCard(new CreditCardDTO(CreditCardType.MasterCard, "400000000000006", 1, 2017));
        customerDAO.setEntityManager(em);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            newCustomer = customerDAO.create(newCustomer);
        }
        catch(PersistenceException ex){
            log("Duplicate Email successfully caught: Constraint Violation");
            transaction.rollback();
            return;
        }
        transaction.rollback();
        fail("Creation of 2 customers with same email should not be possible!");
        
    }
    @Test (dependsOnMethods = "uniqueEmailTest")
    public void findByNumber(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        CustomerDTO customer1 = customerDAO.find("6014");
        transaction.commit();
        em.clear();
        assertNotNull(customer1);
        log("Found customer by number: " +  customer1.getNumber()+ customer1.getEmail() +
            " FirstName: " + customer1.getFirstName() + " LastName: " + customer1.getLastName());
    }
    @Test
    public void testFindMaxNumber(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        String maxNumber = customerDAO.findMaxNumber(); 
        log("max customer found: " + maxNumber);
        transaction.commit();
        em.clear();
        assertTrue(maxNumber.equals("6020"));
    }

}
