/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.CreditCardType;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.BookInfo;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderItemDTO;
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
public class CustomerServiceIT extends BaseIT {
    
    public CustomerServiceIT() {
    }
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CustomerService";
    private static CustomerService customerService;
    
    @Override
    public void lookupService() throws Exception{
        Context jndiContext = new InitialContext();
	customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }
    @Test
    public void registerCustomerTest(){
        CustomerDTO customer = new CustomerDTO();
        customer.setAddress(new AddressDTO("Meriedweg 23", "Thun", "1234", "CH"));
        customer.setCreditCard(new CreditCardDTO(CreditCardType.MasterCard, "1111222233334444", 9, 2016));
        customer.setEmail("mary.smith@info.org");
        customer.setFirstName("Mary");
        customer.setLastName("Smith");
        customer.setNumber("2025");
        try {
            customerService.registerCustomer(customer, "secret");
        }
        catch(CustomerAlreadyExistsException ex){
            fail("customer should not exist");
        }
        
        // try to register the customer again. It should fail the 2nd time.
        try {
            customerService.registerCustomer(customer, "secret");
        }
        catch(CustomerAlreadyExistsException ex){
            return;
        }
        fail("customer should already exist!");
    }
    
    @Test
    public void authenticateCustomerTest() throws CustomerNotFoundException, InvalidPasswordException{
        CustomerDTO customer = null;
        
            customer = customerService.findCustomerByEmail("alice@example.org");
        
            log("***Authentication Test***** " + customer.getEmail() + " " + "1234");
            customerService.authenticateCustomer(customer.getEmail(), "1234");
            log("Authentication successful");
        
        
        
    }
    @Test(dependsOnMethods = "registerCustomerTest")
    public void customerNotFoundTest(){
        CustomerDTO customer = null;
        try {        
            customer = customerService.findCustomerByEmail("mary.smith@info.org");
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(CustomerServiceIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            customerService.authenticateCustomer("", "secret");
        }
        catch (InvalidPasswordException ex){
            ex.printStackTrace();
            fail("Authentication failed");
        }
        catch (CustomerNotFoundException ex){
            // correct behaviour as email does not exist
        }
    }
    @Test(dependsOnMethods = "registerCustomerTest")
    public void invalidPasswordTest(){
        CustomerDTO customer = null;
        try {        
            customer = customerService.findCustomerByEmail("mary.smith@info.org");
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(CustomerServiceIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            customerService.authenticateCustomer(customer.getEmail(), "wrong password");
        }
        catch (CustomerNotFoundException ex){
            ex.printStackTrace();
            fail("Authentication failed");
        }
        catch (InvalidPasswordException ex){
            // correct behaviour as password is incorrect
        }
    }
    @Test
    public void changePasswordTest(){
        try {
            customerService.changePassword("ruth@example.org", "password change");
        }
        catch (CustomerNotFoundException ex){
            ex.printStackTrace();
            fail("change of password failed");
        }
        // verify that passowrd has been changed
        try {
            customerService.authenticateCustomer("ruth@example.org", "password change");
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail("change of password failed");
        }   
    }
    @Test
    public void changePasswordCustomerDoesntExist(){
        try {
            customerService.changePassword("nobody@example.org", "password change");
        }
        catch (CustomerNotFoundException ex){
            return;          
        }
        fail("exception CustomerNotFoundException should have been thrown!");
    }
    
    @Test 
    public void findCustomerByEmail(){
        // verify that existing customer IS found
        try {
            customerService.findCustomerByEmail("alice@example.org");         
        }
        catch (CustomerNotFoundException ex){
            ex.printStackTrace();
            fail("customer should have been found!");
        }
        // verify that nonexistent customer is NOT found
        try {
            customerService.findCustomerByEmail("nobody@example.org");         
        }
        catch (CustomerNotFoundException ex){
            return;
            
        }
        fail("customer should not have been found!");
    }
    @Test
    public void searchCustomer(){
        // verify that existing surname IS Found
        List<CustomerInfo> list = customerService.searchCustomers("gisler");
        assertTrue(list.size() == 1);
        for (CustomerInfo ci: list){
            log("Customer Info surname found: " + ci.getEmail() + " " + ci.getFirstName() + " " + ci.getLastName());
        }
        // verify that existing first name IS Found
        list = customerService.searchCustomers("james");
        assertTrue(list.size() == 1); 
        for (CustomerInfo ci: list){
            log("Customer Info first name found: " + ci.getEmail() + " " + ci.getFirstName() + " " + ci.getLastName());
        }
        // verify that nonexistent name is NOT found
        list = customerService.searchCustomers("nosuchname");
        assertTrue(list.size() == 0);
        
        // verify that name token or partial name is found
        list = customerService.searchCustomers("ANN");
        assertTrue(list.size() > 0); 
        for (CustomerInfo ci: list){
            log("Customer Info name token found: " + ci.getEmail() + " " + ci.getFirstName() + " " + ci.getLastName());
        }
    }
    @Test 
    public void verifyCustomerTest(){
        log("************* verifying customer data *********************");
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setAddress(new AddressDTO("Meriedweg 23", "Thun", "1234", "CH"));
        customer1.setCreditCard(new CreditCardDTO(CreditCardType.MasterCard, "1111222233334444", 9, 2016));
        customer1.setEmail("peter.jones@softa.co.uk");
        customer1.setFirstName("");
        customer1.setLastName("Jones");
        log("customer: " + customer1);
        // first name empty
        try {
            customerService.registerCustomer(customer1, "secret");
        }
        catch(EJBException ex){ 
            if (ex.getCausedByException().getClass().equals(IllegalArgumentException.class)){
                return;
            }
            ex.printStackTrace();
            fail("wrong cause");
            return;
        }
        catch (CustomerAlreadyExistsException ex){
            ex.printStackTrace();
            fail("wrong exception");
            return;
        }
        fail("customer should not have been registered");
    }
    @Test
    public void findCustomerByNumber(){
        log("test findCustomerByNumber");
        try {
            CustomerDTO customer = customerService.findCustomer("6015");
        } catch (CustomerNotFoundException ex) {
            fail("customer 6015 not found!");
        }
        // search for a customer number that doea not exist
        
    }
    @Test(expectedExceptions = CustomerNotFoundException.class)
    public void findCustomerNumberNotFound() throws CustomerNotFoundException {
        CustomerDTO customer = customerService.findCustomer("99999");
    }
    @Test
    public void updateCustomerAddress(){
        log("test update customer address:");
        CustomerDTO customer = null;
        try {
            customer = customerService.findCustomer("6020");
        } catch (CustomerNotFoundException ex) {
            fail("customer 6020 not found!");
        }
        AddressDTO address = new AddressDTO("Alpenstrasse", "Bern", "3013", "CH");
        customer.setAddress(address);
        
        try {
            customerService.updateCustomer(customer);
        } catch (CustomerNotFoundException ex) {
            fail("customer should have been successfully updated!");
        } catch (CustomerAlreadyExistsException ex) {
            fail("customer should have been successfully updated!");
        }
        try {
            customer = customerService.findCustomer("6020");
        } catch (CustomerNotFoundException ex) {
            fail("customer 6020 not found!");
        }
        log("customer 6020 address updated: " + customer.getAddress());
    }
    
    @Test(dependsOnMethods ="updateCustomerAddress")
    public void updateCustomerCreditcard(){
        log("test update customer credit card:");
        CustomerDTO customer1 = null;
        try {
            customer1 = customerService.findCustomer("6016");
        } catch (CustomerNotFoundException ex) {
            fail("customer 6012 not found!");
        }
        log("customer as in DB: " + customer1);
        CreditCardDTO card = new CreditCardDTO(CreditCardType.MasterCard, "5199888877774444", 6, 2020);
        customer1.setCreditCard(card);   
        log("customer ready for update: " + customer1);
        try {
            customerService.updateCustomer(customer1);
        } catch (CustomerNotFoundException ex) {
            fail("customer should have been successfully updated!");
        } catch (CustomerAlreadyExistsException ex) {
            fail("customer should have been successfully updated!");
        }
        try {
            customer1 = customerService.findCustomer("6016");
        } catch (CustomerNotFoundException ex) {
            fail("customer 6016 not found!");
        }
        log("customer 6016 updated: " + customer1);
        assert(customer1.getCreditCard().getNumber().equals(card.getNumber()));
        assert(customer1.getCreditCard().getType().equals(card.getType()));
    }
   
    @Test
    public void updateCustomerEmail(){
        log("test update customer email: ");
        CustomerDTO customer1 = null;
        String custNumber = "6019";
        try {
            log("searching: " + custNumber);
            customer1 = customerService.findCustomer(custNumber);
            log("customer found: " + customer1);
        } catch (CustomerNotFoundException ex) {
            fail("customer not found!");
        }
        customer1.setEmail("superman");
        try {
            customerService.updateCustomer(customer1);
        } catch (CustomerNotFoundException ex) {
            fail("customer should have been successfully updated!");
        } catch (CustomerAlreadyExistsException ex) {
            fail("customer should have been successfully updated!");
        }
    }
    @Test
    public void testValidateCreditCard() throws PaymentFailedException{
        CreditCardDTO creditCard = new CreditCardDTO(CreditCardType.MasterCard, "5200123412341234", 1, 2017);
        customerService.validateCreditCard(creditCard);
        
    }
    @Test(expectedExceptions = {PaymentFailedException.class})
    public void testValidateCreditCardInvalidNumber() throws PaymentFailedException{
        CreditCardDTO creditCard = new CreditCardDTO(CreditCardType.MasterCard, "520012341234123", 0, 2017);
        customerService.validateCreditCard(creditCard);
        
    }
    @Test(expectedExceptions = {PaymentFailedException.class})
    public void testValidateCreditCardExpired() throws PaymentFailedException{
        CreditCardDTO creditCard = new CreditCardDTO(CreditCardType.MasterCard, "5200123412341234", 1, 2015);
        customerService.validateCreditCard(creditCard);
        
    }
    @Test(expectedExceptions = {PaymentFailedException.class})
    public void testValidateCreditCardInvalidMonth() throws PaymentFailedException{
        CreditCardDTO creditCard = new CreditCardDTO(CreditCardType.MasterCard, "5200123412341234", 13, 2017);
        customerService.validateCreditCard(creditCard);
        
    }
    @Test(expectedExceptions = {EJBException.class} )
    public void testInvalidCreditCard()throws CustomerNotFoundException, 
                                CustomerAlreadyExistsException, BookNotFoundException, PaymentFailedException{
        CustomerDTO customer = customerService.findCustomer("6012");
        CreditCardDTO card = new CreditCardDTO(CreditCardType.MasterCard, "XXXXX", 1, 2017);
        customer.setCreditCard(card);
        
        customerService.updateCustomer(customer);
        
             
    }
    @Test
    public void testInvalidEmailPassword() throws CustomerAlreadyExistsException{
        log("************* verifying customer data *********************");
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setAddress(new AddressDTO("Meriedweg 23", "Thun", "1234", "CH"));
        customer1.setCreditCard(new CreditCardDTO(CreditCardType.MasterCard, "1111222233334444", 9, 2016));
        customer1.setEmail("");
        customer1.setFirstName("peter");
        customer1.setLastName("Jones");
        log("customer: " + customer1);
        // first name empty
        try {
            customerService.registerCustomer(customer1, null);
        }
        catch(EJBException ex){ 
            if (ex.getCausedByException().getClass().equals(IllegalArgumentException.class)){
                log("Success:  IllegalArgumentException *********************");
                return;
            }
            ex.printStackTrace();
            fail("wrong cause");
            return;
        }
        
    }
}
