/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.books.data.CreditCardType;
import org.books.data.OrderStatus;
import org.books.data.dto.BookInfo;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.Address;
import org.books.data.entity.BookEntity;
import org.books.data.entity.CreditCard;
import org.books.data.entity.CustomerEntity;
import org.books.data.entity.OrderEntity;
import org.books.data.entity.OrderItem;
import org.books.persistence.exception.BookEntityNotFound;
import org.books.persistence.exception.CustomerEntityNotFound;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

public class OrderDAOTest extends BaseTest {

    private OrderEntity newOrder = new OrderEntity();

    @Test
    public void testCreate() {
        newOrder.setAddress(new Address("Bernweg", "Bern", "3011", "CH"));
        newOrder.setCreditCard(new CreditCard(CreditCardType.MasterCard, "400000000000010", 2, 2020));
        newOrder.setNumber("1000");
        newOrder.setStatus(OrderStatus.accepted);
        newOrder.setDate(new java.sql.Date(System.currentTimeMillis()));
        newOrder.setAmount(new BigDecimal("10.25"));

        // Add a CustomerEntity
        orderDAO.setEntityManager(em);
        customerDAO.setEntityManager(em);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        CustomerEntity customer = em.find(CustomerEntity.class, 1013L);
        transaction.commit();
        em.clear();
        assertNotNull(customer);
        newOrder.setCustomer(customer);

        // Add OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        transaction = em.getTransaction();
        transaction.begin();
        BookEntity book = em.find(BookEntity.class, 1002L);
        transaction.commit();
        em.clear();
        assertNotNull(book);
        orderItem.setBook(book);
        orderItem.setQuantity(1);
        orderItem.setPrice(new BigDecimal("10.2"));
        orderItems.add(orderItem);
        newOrder.setOrderItems(orderItems);

        // Create a new OrderEntity
        transaction = em.getTransaction();
        transaction.begin();
        newOrder = orderDAO.create(newOrder);
        transaction.commit();
        assertNotNull(newOrder);
        log("New Order created: " + newOrder.getId() + " Order number: " + newOrder.getNumber()
                + " Order Date: " + newOrder.getDate() + " Amount: " + newOrder.getAmount() 
                + " FirstName: " + newOrder.getCustomer().getFirstName() +  " LastName: " + newOrder.getCustomer().getLastName());
    }

    @Test(dependsOnMethods = "testSearchNumber")
    public void testUpdate() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        CustomerEntity customer = em.find(CustomerEntity.class, 1020L);
        transaction.commit();
        em.clear();
        assertNotNull(customer);

        transaction = em.getTransaction();
        transaction.begin();
        newOrder.setCustomer(customer);
        newOrder = orderDAO.update(newOrder);
        transaction.commit();
        em.clear();
        log("Order updated: " + newOrder.getId() + " LastName: " + newOrder.getCustomer().getLastName()
                + " City: " + newOrder.getCustomer().getAddress().getCity());
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() {
        long id = 1020L;
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        OrderEntity order = em.find(OrderEntity.class, id);
        transaction.commit();
        em.clear();

        transaction = em.getTransaction();
        transaction.begin();
        orderDAO.delete(order);
        transaction.commit();
        em.clear();
        OrderEntity order1 = em.find(OrderEntity.class, id);
        assertTrue(order1 == null);
    }

    @Test(dependsOnMethods = "testCreate")
    public void testSearch() {
        String custNr = "6014";
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        CustomerDTO customer =customerDAO.find(custNr);
        transaction.commit();
        em.clear();

        transaction = em.getTransaction();
        transaction.begin();
        List<OrderInfo> orders = orderDAO.search(customer, 14);
        assertTrue(orders.size() == 1);
        transaction.commit();
        em.clear();

        for (OrderInfo order : orders) {
            log("order found: " + order.getNumber() + " " + order.getDate());
        }

        transaction = em.getTransaction();
        transaction.begin();
        orders = orderDAO.search(customer, 2014);
        assertTrue(orders.size() == 1);
        transaction.commit();
        em.clear();

        transaction = em.getTransaction();
        transaction.begin();
        orders = orderDAO.search(customer, 2015);
        assertTrue(orders.size() == 0);
        transaction.commit();
        em.clear();

    }

    @Test(dependsOnMethods = "testSearch")
    public void testSearchNumber() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        OrderDTO dto = null;
        dto = orderDAO.find("202020");
        transaction.commit();
        em.clear();
        assertTrue(dto != null);
        
        // search for nonexistent order number
        transaction = em.getTransaction();
        transaction.begin();
        dto = orderDAO.find("101010");
        transaction.commit();
        em.clear();
        assertTrue(dto == null);
    }
    @Test
    public void testFindMaxNumber(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        String maxNumber = orderDAO.findMaxNumber();  
        transaction.commit();
        em.clear();
    }
    @Test
    public void testPlaceOrder() throws BookEntityNotFound, CustomerEntityNotFound{
        String custNr = "6019";
        OrderItemDTO item1 = new OrderItemDTO();
        OrderItemDTO item2 = new OrderItemDTO();
        List<OrderItemDTO> list = new ArrayList<>();
        BookInfo book1 = new BookInfo();
        book1.setIsbn("0596158025");
        book1.setPrice(new BigDecimal("54.99"));
        item1.setBook(book1);
        item1.setQuantity(10);
        list.add(item1);
        
        BookInfo book2 = new BookInfo();
        book2.setIsbn("144936134X");
        book2.setPrice(new BigDecimal("39.99"));
        item2.setBook(book2);
        item2.setQuantity(100);
        list.add(item2);
        
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        OrderDTO newOrder1 = orderDAO.placeOrder(custNr, list);
        transaction.commit();
        em.clear();
        
        assertFalse(newOrder==null);
        List<OrderItemDTO> itemsOrdered = newOrder1.getOrderItems();
        log("number of items ordered: " + itemsOrdered.size());
        assertTrue(itemsOrdered.size()==2);
        
    }
    @Test
    public void testPlaceOrderNoCustomer() throws BookEntityNotFound {
        String custNr = "XXXX";
        OrderItemDTO item1 = new OrderItemDTO();
        List<OrderItemDTO> list = new ArrayList<>();
        BookInfo book1 = new BookInfo();
        book1.setIsbn("0596158025");
        book1.setPrice(new BigDecimal("54.99"));
        item1.setBook(book1);
        item1.setQuantity(10);
        list.add(item1);
        
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            OrderDTO newOrder1 = orderDAO.placeOrder(custNr, list);
        }
        catch(CustomerEntityNotFound ex){
            transaction.rollback();
            em.clear();
            return;
        }
        fail("Expected CustomerEntityNotFound exception");
    }
    
    @Test
    public void testPlaceOrderNoBook() throws CustomerEntityNotFound {
        String custNr = "6018";
        OrderItemDTO item1 = new OrderItemDTO();
        List<OrderItemDTO> list = new ArrayList<>();
        BookInfo book1 = new BookInfo();
        book1.setIsbn("XXXXXXXX");
        book1.setPrice(new BigDecimal("54.99"));
        item1.setBook(book1);
        item1.setQuantity(10);
        list.add(item1);
        
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            OrderDTO newOrder1 = orderDAO.placeOrder(custNr, list);
        }
        catch(BookEntityNotFound ex){
            transaction.rollback();
            em.clear();
            return;
        }
        fail("Expected BookEntityNotFound exception");
    }       
    
    
}
