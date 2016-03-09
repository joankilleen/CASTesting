/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import javax.ejb.Local;
import org.books.application.exception.OrderNotFoundException;
import org.books.data.OrderStatus;
import org.books.data.dto.OrderDTO;

/**
 *
 * @author Joan
 */
@Local
public interface OrderServiceLocal {
    
    public void setOrderState(OrderDTO order, OrderStatus state) throws OrderNotFoundException ;
    
}
