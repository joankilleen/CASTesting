/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderDTO;
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
public class MailBeanIT extends BaseIT {
    private static final String MAIL_BEAN_NAME = "java:global/bookstore-app/bookstore-ejb/MailService";
    private static MailService mailService;
    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/OrderService!org.books.application.OrderService";
    private static OrderService orderService;
    private String testEmail;
    
    public MailBeanIT() {
    }

    @Override
    public void lookupService() throws Exception{
        Context jndiContext = new InitialContext();
	mailService = (MailService) jndiContext.lookup(MAIL_BEAN_NAME);
        orderService = (OrderService)jndiContext.lookup(ORDER_SERVICE_NAME);
    }
    @Test
    public void sendMailTest() throws Exception{
        OrderDTO order = orderService.findOrder("202021");
        CustomerDTO customer = order.getCustomer();
        // add an email address here to test the servce
        testEmail = "joan.killeen@bluewin.ch";
        customer.setEmail(testEmail);
        order.setCustomer(customer);
        mailService.sendShippingMail(order);
        mailService.sendProcessingMail(order);
    }
    

    
}
