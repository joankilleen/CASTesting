/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.OrderStatus;
import org.books.data.dto.BookInfo;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Joan
 */
public class OrderServiceIT extends BaseIT {
    
    
    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/OrderService!org.books.application.OrderService";
    private static OrderService orderService;
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CustomerService";
    private static CustomerService customerService;
    private String newOrderNr = "";
    
    
    @Override
    public void lookupService() throws Exception{
        Context jndiContext = new InitialContext();
	orderService = (OrderService) jndiContext.lookup(ORDER_SERVICE_NAME);
        customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }
    @Test(enabled=true)
    public void findTest() throws OrderNotFoundException{
        String orderNr = "202022";
        log("order find test: " + orderNr);
        OrderDTO dto = orderService.findOrder(orderNr);
        assertTrue(dto!=null);
        log("order found: " + dto);
        List<OrderItemDTO> list = dto.getOrderItems();
        for (OrderItemDTO item: list){
            log("order item found: " + item);
        }
    }
    @Test(expectedExceptions = OrderNotFoundException.class, enabled=true )
    public void findTestNoOrder() throws OrderNotFoundException{
        String orderNr = "";
        log("order find test: " + orderNr);
        OrderDTO dto = orderService.findOrder(orderNr);   
    }
    @Test(enabled=true)
    public void searchOrder() throws CustomerNotFoundException{
        String customerNr = "6014";
        List<OrderInfo> list = orderService.searchOrders(customerNr, 14);
        for (OrderInfo item: list){
            log("order item found: " + item);
        }
    }
    @Test(expectedExceptions = CustomerNotFoundException.class, enabled=true )
    public void searchOrderNoCustomer() throws CustomerNotFoundException{
        String customerNr = "XXXXX";
        List<OrderInfo> list = orderService.searchOrders(customerNr, 14);
        
    }
    @Test(enabled=true)
    public void searchOrderNoneFound() throws CustomerNotFoundException{
        String customerNr = "6014";
        List<OrderInfo> list = orderService.searchOrders(customerNr, 16);
        assertTrue(list.size()==0);
        log("no orders found");
    }
    @Test(enabled = true)
    public void testPlaceOrder() throws BookNotFoundException, CustomerNotFoundException, PaymentFailedException, OrderNotFoundException{
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
        item2.setQuantity(10);
        list.add(item2);
        OrderDTO order1 = orderService.placeOrder(custNr, list);
        log("order created: " + order1.getNumber() );
        int orderNr1 = Integer.parseInt(order1.getNumber());
        newOrderNr = order1.getNumber();
        /*OrderDTO order2 = orderService.placeOrder(custNr, list);
        
        int orderNr2 = Integer.parseInt(order2.getNumber());
        log("order created: " + orderNr2);
        assertTrue(orderNr1+1==orderNr2);*/
        
    }
    @Test(expectedExceptions = {BookNotFoundException.class}, enabled=true)
    public void testPlaceOrderNoBook() throws BookNotFoundException, CustomerNotFoundException, PaymentFailedException, OrderNotFoundException{
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
        book2.setIsbn("XXXXXXX");
        book2.setPrice(new BigDecimal("39.99"));
        item2.setBook(book2);
        item2.setQuantity(10);
        list.add(item2);
        OrderDTO order1 = orderService.placeOrder(custNr, list);
        log("order created: " + order1.getNumber() );
        int orderNr1 = Integer.parseInt(order1.getNumber());
        OrderDTO order2 = orderService.placeOrder(custNr, list);
        int orderNr2 = Integer.parseInt(order2.getNumber());
        log("order created: " + orderNr2);
        assertTrue(orderNr1+1==orderNr2);
        
        
    }
    @Test(expectedExceptions = {CustomerNotFoundException.class}, enabled=true)
    public void testPlaceOrderNoCust() throws BookNotFoundException, CustomerNotFoundException, PaymentFailedException, OrderNotFoundException{
        String custNr = "7005";
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
        item2.setQuantity(10);
        list.add(item2);
        OrderDTO order1 = orderService.placeOrder(custNr, list);
        log("order created: " + order1.getNumber() );
        int orderNr1 = Integer.parseInt(order1.getNumber());
        /*OrderDTO order2 = orderService.placeOrder(custNr, list);
        int orderNr2 = Integer.parseInt(order2.getNumber());
        log("order created: " + orderNr2);
        assertTrue(orderNr1+1==orderNr2); */    
    }
    @Test(dependsOnMethods = {"testPlaceOrder"})
    public void testCancelOrder() throws  OrderAlreadyShippedException, OrderNotFoundException{
        orderService.cancelOrder(newOrderNr);
        OrderDTO canceledOrder = orderService.findOrder(newOrderNr);
        assert(canceledOrder.getStatus()==OrderStatus.canceled);
    }
    @Test(expectedExceptions = {OrderAlreadyShippedException.class}, enabled=true)
    public void testCancelOrderAlreadyShipped() throws  OrderAlreadyShippedException, OrderNotFoundException{
        orderService.cancelOrder("202023");
        
        
    }
    
    @Test(dependsOnMethods = {"testPlaceOrder"}, expectedExceptions = OrderNotFoundException.class, enabled=true)
    public void testCancelOrderNotFound() throws  OrderAlreadyShippedException, OrderNotFoundException{
        orderService.cancelOrder("");        
    }
    
    
}
