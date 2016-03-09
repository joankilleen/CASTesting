/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.musicstore.persistence.repositories;

import java.util.List;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.extension.*;
import org.jboss.arquillian.persistence.UsingDataSet;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.musicstore.persistence.entities.Album;
import org.musicstore.persistence.entities.MusicOrder;
import org.musicstore.persistence.entities.OrderItem;	 


/**
 *
 * @author Joan
 */
@RunWith(Arquillian.class)

public class MusicOrderRepositoryTest extends BaseTest {
    
    
    @Test
    
    public void shouldFindAllOrders() {
        
        List<MusicOrder> items = musicOrderRepository.getOrders();
        //assertThat(items, hasSize(greaterThan(0)));
    }
}

