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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.books.application.CustomerService;
import org.books.data.dto.CreditCardDTO;
import org.books.application.exception.*;
import org.books.data.CreditCardType;
import org.books.data.dto.CustomerDTO;
import org.books.util.MessageFactory;

/**
 *
 * @author Joan
 */
@Named("accountBean")
@SessionScoped
public class AccountBean implements Serializable {

    private static final String INVALID_CURRENT_PASSWORD_ID = "org.books.presentation.INVALID_CURRENT_PASSWORD";
    private static final String INVALID_PASSWORD_ID = "org.books.presentation.INVALID_PASSWORD";
    private static final String CUSTOMER_NOT_FOUND_ID = "org.books.presentation.CUSTOMER_NOT_FOUND";
    private static final String CUSTOMER_NOT_UPDATED = "org.books.presentation.CUSTOMER_NOT_UPDATED";
    private static final String CUSTOMER_ALREADY_EXISTS_ID = "org.books.presentation.CUSTOMER_ALREADY_EXISTS";
    private String email;
    private String password;
    private String currentPassword;
    private String newPassword;
    private static final Logger LOG = Logger.getLogger(AccountBean.class.getName());
    @EJB
    private CustomerService customerService;
    private CustomerDTO authCustomer = null;
    private String navigationCase = "";

    public String getNavigationCase() {
        return navigationCase;
    }

    public void setNavigationCase(String navigationCase) {
        this.navigationCase = navigationCase;
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

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String account() {
        // account link has been pressed. 
        // If the customer is not logged in, get them to login now.
        if (!isCustomerAuthenticated()) {
            return "login";
        }
        return navigate();
    }

    public String login() {
        try {
            LOG.info(AccountBean.class.getName() + ": loggine in with " + email + " " + password);
            customerService.authenticateCustomer(email, password);
            LOG.info(AccountBean.class.getName() + ": CUSTOMER authenticated: " + email + " " + password);
            authCustomer = customerService.findCustomerByEmail(email);
        }   catch (InvalidPasswordException e) {
                MessageFactory.error(INVALID_PASSWORD_ID);
                return null;
            } 
            catch (CustomerNotFoundException e){
                MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
                return null;
            }
            
        
        return navigate();

    }

    public CustomerDTO getAuthCustomer() {
        return authCustomer;
    }

    public boolean isCustomerAuthenticated() {
        return (authCustomer != null);
    }

    public String checkOut() {
        if (!this.isCustomerAuthenticated()) {
            return "login";
        }
        return navigate();
    }

    public String navigate() {
        // navigate from login page to next page
        if (navigationCase.equals("checkOut")) {
            return "orderSummary";
        }
        if (navigationCase.equals("account")) {
            return "accountDetails";
        }
        return "";

    }

    public String getCustomerName() {
        StringBuilder custName = new StringBuilder(authCustomer.getFirstName());
        custName.append(' ');
        custName.append(authCustomer.getLastName());
        return custName.toString();

    }

    public String getCustomerCreditCardType() {
        CreditCardType type = authCustomer.getCreditCard().getType();
        switch (type) {
            case MasterCard:
                return "MasterCard";
            default:
                return "Visa";
        }
    }

    public String getCustomerCreditCardNumber() {
        String input = authCustomer.getCreditCard().getNumber();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ");
            }

            result.append(input.charAt(i));
        }
        return result.toString();
    }

    public String getCustomerCreditCardExpiry() {
        String month = authCustomer.getCreditCard().getExpirationMonth().toString();
        StringBuilder expiry = new StringBuilder();
        expiry.append(month);
        expiry.append('/');
        Integer year = (authCustomer.getCreditCard().getExpirationYear()) % 100;
        expiry.append(year.toString());
        return expiry.toString();

    }

    public String change() {
        return null;
    }

    public String changePassword() {

        if (!this.currentPassword.equals(this.password)) {
            MessageFactory.error(INVALID_CURRENT_PASSWORD_ID);
            return null;
        } else {
            try {
                customerService.changePassword(this.email, this.newPassword);
                this.password = this.newPassword;
                return "accountDetails";
            } catch (CustomerNotFoundException e) {
                    MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
                    return null;
            }
        }
    }

    public String signOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/catalogSearch.xhtml?faces-redirect=true";
    }

    public String saveCustomer() {

        try {
            LOG.info(AccountBean.class.getName() + ": Email before " + authCustomer.getEmail());
            customerService .updateCustomer(authCustomer);
        } catch (CustomerNotFoundException ex) {
                MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
        } catch(CustomerAlreadyExistsException e){
                MessageFactory.error(CUSTOMER_ALREADY_EXISTS_ID);
        }
        try {
            authCustomer = customerService.findCustomer(authCustomer.getNumber());
        }
        catch (CustomerNotFoundException ex) {
                MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
        }
        LOG.info(AccountBean.class.getName() + ": Email after " + authCustomer.getEmail());
        this.email = authCustomer.getEmail();
        return navigate();
    }

    void setAuthCustomer(CustomerDTO customer) {
        this.authCustomer = customer;
    }
}
