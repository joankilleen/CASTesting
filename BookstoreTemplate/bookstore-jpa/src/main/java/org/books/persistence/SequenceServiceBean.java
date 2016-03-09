/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import static javax.ejb.LockType.WRITE;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Joan
 */
@Singleton(name="SequenceService") @Startup
public class SequenceServiceBean implements SequenceService {
    @Resource(name = "startOrderNumber")
    private Integer startOrderNumber;

    @Resource(name = "startCustomerNumber")
    private Integer startCustomerNumber;
    
    @EJB 
    private OrderDAO orderDAO;
    @EJB
    private CustomerDAO customerDAO;
    
    private int newOrderNumber;
    private int newCustomerNumber;
    
    private static final Logger LOG = Logger.getLogger(SequenceServiceBean.class.getName());
    
    @PostConstruct
    public void init(){
        String maxOrderNumber = orderDAO.findMaxNumber();
        log("max order number found: " + maxOrderNumber);
        newOrderNumber = calculateNumber(maxOrderNumber, startOrderNumber); 
        log("new order number calculated: " + newOrderNumber);
        String maxCustomerNumber = customerDAO.findMaxNumber();
        newCustomerNumber = calculateNumber(maxCustomerNumber, startCustomerNumber);
    }
    private int calculateNumber(String currentMax, Integer configuredStartNumber){
        int nextNumber = 0;
        if (currentMax==null || currentMax.isEmpty()){
            nextNumber = configuredStartNumber;
            log("new order number calculated: " + configuredStartNumber);
            return nextNumber;
        }
        int maxNumber = Integer.parseInt(currentMax);
        if (maxNumber < configuredStartNumber){
            nextNumber = configuredStartNumber;
        }
        else {
            nextNumber = maxNumber + 1;
        }
        return nextNumber;
    }
    @Lock(WRITE)
    public String getNewOrderNumber(){
        newOrderNumber++;     
        return Integer.toString(newOrderNumber-1);
    }
    public void log(String msg){
        LOG.info(this.getClass().getSimpleName() + " " + msg);
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
    
    @Lock(WRITE)
    public String getNewCustomerNumber(){
        newCustomerNumber++;     
        return Integer.toString(newCustomerNumber-1);
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // for unit testing without EJB container
    public void setNewOrderNumber(int newOrderNumber) {
        this.newOrderNumber = newOrderNumber;
    }
    // for unit testing without EJB container
    public void setNewCustomerNumber(int newCustomerNumber) {
        this.newCustomerNumber = newCustomerNumber;
    }
    
    
    
    
}
