package org.books.data.dto;

import org.books.data.entity.CustomerEntity;
import java.io.Serializable;

public class CustomerDTO implements Serializable {
    
    
    private String email;
    private String firstName;
    private String lastName;
    private AddressDTO address;
    private CreditCardDTO creditCard;
    private String number;

    public CustomerDTO() {
    }

    public CustomerDTO(String number, String email, String firstName, String lastName, AddressDTO address, CreditCardDTO creditCard) {
        
        this.number = number;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.creditCard = creditCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AddressDTO getAddress() {
        if (address == null) {
            address = new AddressDTO();
        }
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CreditCardDTO getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCardDTO();
        }
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }
    @Override
    public String toString(){
        return this.number + " " + this.email + " " + this.firstName + " " + this.lastName + " " + this.address.toString() + " " + this.creditCard.toString();
    }
    
    public static CustomerDTO copyEntityTODTO(CustomerEntity entity){
        AddressDTO address = AddressDTO.copyEntityToDTO(entity.getAddress());
        
        CreditCardDTO creditcard = CreditCardDTO.copyEntityToDTO(entity.getCreditCard());
        return new CustomerDTO(entity.getNumber(), entity.getEmail(), entity.getFirstName(), entity.getLastName(), 
                address, creditcard);
    }
}
