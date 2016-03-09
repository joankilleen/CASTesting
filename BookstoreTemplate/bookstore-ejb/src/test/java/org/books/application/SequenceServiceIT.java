/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.persistence.SequenceService;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Joan
 */
public class SequenceServiceIT extends BaseIT{
    
    public SequenceServiceIT() {
    }

    private static final String SEQUENCE_SERVICE_NAME = "java:global/bookstore-app/bookstore-jpa/SequenceService";
    private static SequenceService sequenceService;
    
    @Override
    public void lookupService() throws Exception{
        Context jndiContext = new InitialContext();
	sequenceService = (SequenceService) jndiContext.lookup(SEQUENCE_SERVICE_NAME);
    }
    @Test
    public void testGetNumber(){
        String nextOrder = sequenceService.getNewOrderNumber();
        String nextOrderPlusOne = sequenceService.getNewOrderNumber();
        log("next order, next order plus one: " +  nextOrder + " " + nextOrderPlusOne);
    }
}
