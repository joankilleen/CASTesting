/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.data.CreditCardType;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.dto.CustomerDTO;
import org.books.util.MessageFactory;

/**
 *
 * @author Joan
 */
@Named("newCustomerBean")
@SessionScoped
public class NewCustomerBean implements Serializable{
    
    @Inject
    private AccountBean account;
    @EJB
    private CustomerService customerService;
    private CustomerDTO customer = null;
    private AddressDTO address = null;
    private CreditCardDTO creditCard = null;
    private CreditCardType type;
    private String number;
    private int expirationMonth;
    private int expirationYear;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private static final Logger LOG = Logger.getLogger(NewCustomerBean.class.getName());

    private final static String CUSTOMER_ALREADY_EXISTS_ID = "org,books.presentation.CUSTOMER_ALREADY_EXISTS";
    private final static String CUSTOMER_SUCCESSFULLY_CREATED_ID = "org,books.presentation.CUSTOMER_SUCCESSFULLY_CREATED";
    public CreditCardType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }
    

    public void setCountry(String country) {
        this.country = country;
    }
    
    public String register() {
        creditCard = new CreditCardDTO(type, number, expirationMonth, expirationYear);
        address = new AddressDTO(street, city, postalCode, country);
        customer = new CustomerDTO("", email, firstName, lastName, address, creditCard);
        LOG.info(NewCustomerBean.class.getName() + ": adding new customer " + customer.toString());
        try {
            customer = customerService.registerCustomer(customer, password);
        }
        catch(CustomerAlreadyExistsException ex){
            MessageFactory.error(CUSTOMER_ALREADY_EXISTS_ID);
            return null;
        }
        account.setAuthCustomer(null);
        account.setPassword(password);
        account.setEmail(email);
        return account.login();
    }
    
    public String cancel(){
        return "login";
    }
}
