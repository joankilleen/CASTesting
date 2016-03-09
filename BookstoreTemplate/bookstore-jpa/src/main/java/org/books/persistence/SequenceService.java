/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author Joan
 */
@Remote
public interface SequenceService {
    public String getNewOrderNumber();
    public String getNewCustomerNumber();
}
