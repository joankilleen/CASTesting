/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import javax.ejb.Remote;
import org.books.data.dto.OrderDTO;

/**
 *
 * @author Joan
 */
@Remote
public interface MailService {
    public void sendShippingMail(OrderDTO order);
    public void sendProcessingMail(OrderDTO order);
}
