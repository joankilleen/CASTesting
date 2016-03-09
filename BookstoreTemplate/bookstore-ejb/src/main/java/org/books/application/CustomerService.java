/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;

/**
 *
 * @author Joan
 */
@Remote
public interface CustomerService {
    /**
	 * Registers a customer.
	 *
	 * @param customer the new customer
	 * @param password the customer's password
	 * @throws CustomerAlreadyExistsException if another customer with the same email address already exists
         * @throws IllegalArgumentException if firstname, lastname, email, address or credit card values are null
         * @throws IllegalArgumentException if firstname, lastname, email, address or credit card values are empty
	 */
    public CustomerDTO registerCustomer(CustomerDTO customer,String password) throws CustomerAlreadyExistsException;
    
    public void authenticateCustomer(String email, String password) 
            throws CustomerNotFoundException, InvalidPasswordException;
    
    public void changePassword(String email, String password) throws CustomerNotFoundException;
    
    public CustomerDTO findCustomerByEmail(String email) throws CustomerNotFoundException;
    
    public CustomerDTO findCustomer(String customerNr) throws CustomerNotFoundException;

    public List<CustomerInfo> searchCustomers(String name);
    
    
    /**
	 * updates a customer.
	 * If the email address is changed, that login will also be updated.
	 * @param customer the new customer
	 * @param password the customer's password
	 * @throws CustomerAlreadyExistsException if another customer with the same email address already exists
         * @throws CustomerNotFoundException if a customer with the same email but a different number exists
         * @throws IllegalArgumentException if firstname, lastname, email, address or credit card values are null
         * @throws IllegalArgumentException if firstname, lastname, email, address or credit card values are empty
	 */
    public void updateCustomer(CustomerDTO customer) throws CustomerNotFoundException, CustomerAlreadyExistsException;
    
    
    /**
	 * checks that a credit card is valid i.e. number format and validity of the date
	 * 
	 * @param CreditCardDTO the credit card to be validateds
	 * @throws PaymentFailedException if the credit card is not valid 
         *
	 */
    public void validateCreditCard(CreditCardDTO card) throws PaymentFailedException;
    


}
