package org.books.persistence;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.data.entity.Address;
import org.books.data.entity.CreditCard;
import org.books.data.entity.CustomerEntity;
@Stateless
public class CustomerDAO extends GenericDAO<CustomerEntity> {

    @EJB
    private SequenceService sequenceService;
    public CustomerDTO findByEmail(String email) {
        log("searching customer with email " + email);
        TypedQuery<CustomerEntity> query = entityManager.createNamedQuery("CustomerEntity.FindByEmail", CustomerEntity.class);
        query.setParameter("email", email);
        CustomerEntity entity =  null;
        try{
            entity = query.getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
        return CustomerDTO.copyEntityTODTO(entity);
    }
    public CustomerDTO find(String number){
        CustomerEntity entity = queryCustomer(number); 
        if (entity==null) return null;
        return CustomerDTO.copyEntityTODTO(entity);
    }
    public CustomerEntity queryCustomer(String number){
        TypedQuery<CustomerEntity> query = entityManager.createNamedQuery("CustomerEntity.FindByNumber", CustomerEntity.class);
        query.setParameter("number", number);
        CustomerEntity entity =  null;
        try{
            entity = query.getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
        return entity;
    }

    public List<CustomerInfo> search(String token) {
        log("searching customer firstName and lastName against token" + token);
        TypedQuery<CustomerInfo> q = entityManager.createNamedQuery("CustomerEntity.FindByNameToken", CustomerInfo.class);
        q.setParameter("nametoken", token);
        return q.getResultList();
    }
    
    public CustomerDTO create(CustomerDTO customer) {
        Address address = new Address(customer.getAddress().getStreet(), 
                                      customer.getAddress().getCity(),
                                      customer.getAddress().getPostalCode(),
                                     customer.getAddress().getCountry());
        CreditCard creditcard = new CreditCard(customer.getCreditCard().getType(),
                                                    customer.getCreditCard().getNumber(),
                                                    customer.getCreditCard().getExpirationMonth(),
                                                    customer.getCreditCard().getExpirationYear());
        String customerNumber = sequenceService.getNewCustomerNumber();
        CustomerEntity entity = new CustomerEntity(null, customerNumber, customer.getEmail(), customer.getFirstName(),
                                                    customer.getLastName(), address, creditcard);
        entity = super.create(entity);
        return CustomerDTO.copyEntityTODTO(entity);
    }
    
    
    public  CustomerDTO update(CustomerDTO customer) {
        
        CustomerDTO customerInDB = find(customer.getNumber());
        CustomerEntity entity =  find(customerInDB);
        log("update entity: " + entity.toString());
        entity.setNumber(customer.getNumber());
        entity.setEmail(customer.getEmail());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        Address address = new Address(customer.getAddress().getStreet(), 
                                      customer.getAddress().getCity(),
                                      customer.getAddress().getPostalCode(), 
                                      customer.getAddress().getCountry());
        CreditCard creditcard = new CreditCard(customer.getCreditCard().getType(), 
                                                customer.getCreditCard().getNumber(), 
                                                customer.getCreditCard().getExpirationMonth(), 
                                                customer.getCreditCard().getExpirationYear());
        entity.setAddress(address);
        entity.setCreditCard(creditcard);
        entity = super.update(entity);
        return CustomerDTO.copyEntityTODTO(entity);
    }
    
    
    
    public void delete(CustomerDTO customer) {
        CustomerEntity entity = find(customer);
        log("remove entity: " + entity.getClass().toString());
        super.delete(entity);
    }
    
    public CustomerEntity find(CustomerDTO customer){
        TypedQuery<CustomerEntity> query = entityManager.createNamedQuery("CustomerEntity.FindByEmail", CustomerEntity.class);
        query.setParameter("email", customer.getEmail());
        return query.getSingleResult();
       
    }
    
    public String findMaxNumber(){
        TypedQuery<String> q = entityManager.createNamedQuery("CustomerEntity.FindMaxNumber", String.class);
        String maxNumber = q.getSingleResult();
        return maxNumber;
    }

    public SequenceService getSequenceService() {
        return sequenceService;
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }
    
    
}
