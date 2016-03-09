/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.books.data.dto.LoginDTO;
import org.books.data.entity.LoginEntity;
import org.books.persistence.LoginDAO;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author guthei
 */
public class LoginDAOTest extends BaseTest {
    private LoginDTO newLogin = new LoginDTO();

    @Test
    public void testCreate() {
        newLogin.setPassword("1234");
        newLogin.setUserName("muster1@swiss.ch");
        loginDAO.setEntityManager(em);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        newLogin = loginDAO.create(newLogin);
        transaction.commit();
        assertNotNull(newLogin);
        log("New Login created: " + newLogin.getUserName() + " " + newLogin.getPassword());
    }

    @Test(dependsOnMethods = "testCreate")
    private void testFind() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        String email = "mickey@mouse";
        // Find a login entity by email
        LoginDTO login1 = loginDAO.find("alice@example.org");
        transaction.commit();
        em.clear();
        assertFalse(login1 == null);
        log("Login data found: " + login1.getUserName() + " " + login1.getPassword());
        transaction = em.getTransaction();
        transaction.begin();
        // search for login entity with incorrect name
        
        login1 = loginDAO.find(email);
        transaction.commit();
        em.clear();
        
        assert(login1==null);
    }

    @Test(dependsOnMethods = "testFind")
    public void testUpdate() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        LoginDTO login = new LoginDTO();
        login.setUserName("alice@example.org");
        login.setPassword("1111");
        login = loginDAO.update(login);
        transaction.commit();
        em.clear();
        log("Login updated: " + login.getUserName() + " " + login.getPassword());
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() {
        long id = 1009L;
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        LoginDTO login = loginDAO.find("james777@example.en");
        transaction.commit();
        em.clear();
 
        transaction = em.getTransaction();
        transaction.begin();
        loginDAO.delete(login);
        transaction.commit();
        em.clear();
        LoginEntity login1 = em.find(LoginEntity.class, id);
        assertTrue(login1 == null);
    }
}
