package org.books.data.dto;

import java.io.Serializable;

public class CustomerInfo implements Serializable{
    private String number;
    private String email;
    private String firstName;
    private String lastName;

    public CustomerInfo() {
    }

    public CustomerInfo(String number, String firstName, String lastName, String email) {
        this.number= number;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CustomerInfo(CustomerDTO customer) {
        this.email = customer.getEmail();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString(){
        return this.number + " " + this.email + " " + this.firstName + " " + this.lastName;
    }
}
