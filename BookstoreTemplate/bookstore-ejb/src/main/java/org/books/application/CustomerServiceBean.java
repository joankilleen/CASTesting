/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.exception.PaymentFailedException;
import org.books.application.exception.PaymentFailedException.Code;
import org.books.data.CreditCardType;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.dto.LoginDTO;
import org.books.data.entity.LoginEntity;
import org.books.persistence.CustomerDAO;
import org.books.persistence.LoginDAO;

/**
 *
 * @author Joan
 */
@Stateless(name = "CustomerService")
//@Interceptors(org.books.application.util.Tracer.class)
public class CustomerServiceBean implements CustomerService {

    @EJB
    private LoginDAO loginDAO;
    @EJB
    private CustomerDAO customerDAO;
    private static final Logger LOG = Logger.getLogger(CustomerServiceBean.class.getName());
    private final String PATTERNCARD = "(\\d{16})";

    @Override
    public CustomerDTO registerCustomer(CustomerDTO customer, String password) throws CustomerAlreadyExistsException {
        log("registering new cutomer: " + customer);
        verifyCustomer(customer);
        if (customerDAO.findByEmail(customer.getEmail()) != null) {
            throw new CustomerAlreadyExistsException();
        }
        customer = customerDAO.create(customer);
        LoginEntity login = new LoginEntity();
        login.setPassword(password);
        login.setUserName(customer.getEmail());
        loginDAO.create(login);
        return customer;

    }

    @Override
    public void authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {
        log("authenticating customer " + email + " " + password);
        LoginDTO login = loginDAO.find(email);
        if (login == null) {
            throw new CustomerNotFoundException();
        }
        log("login found: " + login.getUserName() + " " + login.getPassword());
        if (!password.equals(login.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {
        log("changing password: " + email + " " + password);
        LoginDTO login = new LoginDTO();
        login.setPassword(password);
        login.setUserName(email);
        login = loginDAO.update(login);
        if (login == null) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public CustomerDTO findCustomerByEmail(String email) throws CustomerNotFoundException {
        log("finding customer using email address: " + email);
        CustomerDTO customer = customerDAO.findByEmail(email);
        if (customer == null) {
            log("customer not found, throwing exception: " + email);
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    @Override
    public List<CustomerInfo> searchCustomers(String name) {

        List<CustomerInfo> list = customerDAO.search(name);
        return list;
    }

    @Override
    public CustomerDTO findCustomer(String customerNr) throws CustomerNotFoundException {
        log("finding customer using number: " + customerNr);

        CustomerDTO customer = null;
        customer = customerDAO.find(customerNr);
        if (customer == null) {
            log("customer not found, throwing exception: " + customerNr);
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
        // check that a customer with this number exists in the DB. If not throw CustomerNotFoundException.
        CustomerDTO customerInDB = findCustomer(customer.getNumber());
        log("updating customer: " + customer);
        // find the existing login object
        LoginDTO loginInDB = loginDAO.find(customerInDB.getEmail());
        if (loginInDB == null) {
            throw new CustomerNotFoundException();
        }
        log("updating customer login found: " + loginInDB.getUserName());

        // Check whether the email is being updated
        if (customer.getEmail().equals(customerInDB.getEmail()) && customer.getNumber().equals(customerInDB.getNumber())) {
            // the email is not being changed. Just verify the parameters, update and exit

            log("verifying customer parameters: " + customer.toString());
            this.verifyCustomer(customer);
            // if customer is being updated it must have a number
            if (customer.getNumber() == null) {
                log("rejecting cutomer number: " + customer);
                throw new IllegalArgumentException();
            }
            if (customer.getNumber().isEmpty()) {
                log("rejecting cutomer number: " + customer);
                throw new IllegalArgumentException();
            }
            CustomerDTO updatedCustomer = customerDAO.update(customer);
            log("updated customer: " + customer);
            return;
        }

        // The email is being changed. Check that it doesn't exist already in the DB
        CustomerDTO customerWithNewEmail = customerDAO.findByEmail(customer.getEmail());
        if (customerWithNewEmail != null) {
            throw new CustomerAlreadyExistsException();
        }

        // check that all parameters are correct before doing the update. If not OK, a runtime exception will be thrown.
        this.verifyCustomer(customer);

        // delete the old login and create a new one with the new email address and old password      
        LoginDTO newLogin = new LoginDTO();
        newLogin.setUserName(customer.getEmail());
        newLogin.setPassword(loginInDB.getPassword());
        log("deleting login and creating new login: " + newLogin);
        loginDAO.delete(loginInDB);
        loginDAO.create(newLogin);

        // finally update the customer
        CustomerDTO updatedCustomer = customerDAO.update(customer);
        log("updated customer: " + customer);
    }

    private void log(String msg) {
        LOG.info(this.getClass().getSimpleName() + " " + msg);
    }

    private void verifyCustomer(CustomerDTO customer) {
        // checks on first name, last name, email
        log("verifying customer: " + customer);

        if (customer.getFirstName() == null) {
            log("rejecting cutomer first name: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getFirstName().isEmpty()) {
            log("rejecting cutomer first name: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getLastName() == null) {
            log("rejecting cutomer last name: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getLastName().isEmpty()) {
            log("rejecting cutomer last name: " + customer);
            throw new IllegalArgumentException();
        }

        if (customer.getEmail() == null) {
            log("rejecting cutomer email: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getEmail().isEmpty()) {
            log("rejecting cutomer email: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getAddress() == null) {
            log("rejecting cutomer address: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getAddress().getStreet() == null) {
            log("rejecting cutomer address: " + customer);
            throw new IllegalArgumentException();
        }
        // address checks on street and country
        if (customer.getAddress().getStreet().isEmpty()) {
            log("rejecting cutomer address street: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getAddress().getCountry() == null) {
            log("rejecting cutomer address country: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getAddress().getCountry().isEmpty()) {
            log("rejecting cutomer address country: " + customer);
            throw new IllegalArgumentException();
        }

        // credit card checks
        if (customer.getCreditCard() == null) {
            log("rejecting cutomer credit card: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getCreditCard().getNumber() == null) {
            log("rejecting cutomer credit card number: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getCreditCard().getNumber().isEmpty()) {
            log("rejecting cutomer credit card number: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getCreditCard().getType() == null) {
            log("rejecting cutomer credit card type: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getCreditCard().getExpirationMonth() == null) {
            log("rejecting cutomer credit card month: " + customer);
            throw new IllegalArgumentException();
        }
        if (customer.getCreditCard().getExpirationYear() == null) {
            log("rejecting cutomer credit card year: " + customer);
            throw new IllegalArgumentException();
        }
        try {
            validateCreditCard(customer.getCreditCard());
        } catch (PaymentFailedException ex) {
            throw new IllegalArgumentException();
        }

    }

    public void validateCreditCard(CreditCardDTO card) throws PaymentFailedException {
        checkDate(card);
        checkCardNumber(card);
    }

    private void checkDate(CreditCardDTO card) throws PaymentFailedException {
        Calendar calendar = Calendar.getInstance();
        if (card.getExpirationYear() < calendar.get(Calendar.YEAR)) {
            throw new PaymentFailedException(Code.CREDIT_CARD_EXPIRED);
        }
        if (card.getExpirationYear() == calendar.get(Calendar.YEAR)) {
            if (card.getExpirationMonth() < calendar.get(Calendar.MONTH)) {
                throw new PaymentFailedException(Code.CREDIT_CARD_EXPIRED);
            }
        }
        if (card.getExpirationMonth() > 12 || card.getExpirationMonth() < 1) {
            throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
        }

    }

    public void checkCardNumber(CreditCardDTO card) throws PaymentFailedException {
        String input = card.getNumber();
        if (!input.matches(PATTERNCARD)) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
        CreditCardType type = card.getType();
        char firstChar = input.charAt(0);
        char secondChar = input.charAt(1);

        if (type.equals(CreditCardType.MasterCard)) {
            // check that value if 51, 52, 53, 54
            if (firstChar == '5') {
                if (secondChar == '5' || secondChar == '6' || secondChar == '7' || secondChar == '8' || secondChar == '9') {
                    throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
                }
            }
        }
        if (type.equals(CreditCardType.Visa)) {
            if (firstChar != '4') {
                throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
            }
        }
    }
}
