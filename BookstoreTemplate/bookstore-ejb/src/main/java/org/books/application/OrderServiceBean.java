
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigDecimal;
import java.util.Calendar;
import org.books.persistence.SequenceService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.application.exception.PaymentFailedException.Code;
import org.books.application.util.Tracer;
import org.books.data.OrderStatus;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.persistence.CustomerDAO;
import org.books.persistence.OrderDAO;
import org.books.persistence.exception.*;

/**
 *
 * @author Joan
 */
@Stateless(name = "OrderService")
public class OrderServiceBean implements OrderService, OrderServiceLocal {

    @EJB
    private OrderDAO orderDAO;
    @EJB
    private CustomerDAO customerDAO;

    @EJB
    private SequenceService sequenceService;

    @EJB
    private CustomerService customerService;

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext jmsContext;

    @Resource(lookup = "jms/orderQueue")
    private Queue orderQueue;

    private static final Logger LOG = Logger.getLogger(OrderServiceBean.class.getName());

    @Resource(name = "creditCardLimit")
    private Integer creditCardLimit;
    
    public static final String NEW_ORDER_MESSAGE = "newOrder";
    public static final String CANCEL_ORDER_MESSAGE = "cancelOrder";

    @Override
    public OrderDTO findOrder(String orderNr) throws OrderNotFoundException {
        OrderDTO order = null;
        log("finding order using number: " + orderNr);
        order = orderDAO.find(orderNr);
        if (order == null) {
            log("order not found, throwing exception: " + orderNr);
            throw new OrderNotFoundException();
        }
        return order;
    }

    private void log(String msg) {
        LOG.info(this.getClass().getSimpleName() + " " + msg);
    }

    @Override
    public List<OrderInfo> searchOrders(String customerNr, Integer year) throws CustomerNotFoundException {
        log("searching orders for customer: " + customerNr + " " + year);
        CustomerDTO customer = customerDAO.find(customerNr);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        log("customer found: " + customer);
        List<OrderInfo> list = orderDAO.search(customer, year);
        log("list found: " + list.size());

        return list;
    }

    @Override
    public OrderDTO placeOrder(String customerNr, List<OrderItemDTO> items) throws BookNotFoundException, CustomerNotFoundException, PaymentFailedException{
        OrderDTO order = null;
        CustomerDTO dto = null;

        dto = customerDAO.find(customerNr);
        if (dto == null) {
            throw new CustomerNotFoundException();
        }
        if (items.isEmpty()) {
            throw new BookNotFoundException();
        }
        validatePayment(dto.getCreditCard(), items);

        try {
            order = orderDAO.placeOrder(customerNr, items);
            log("order successfully placed with number: " + order.getNumber());
        } catch (BookEntityNotFound ex) {
            throw new BookNotFoundException();
        } catch (CustomerEntityNotFound ex) {
            throw new CustomerNotFoundException();
        } 
        sendMessage(NEW_ORDER_MESSAGE, order);
        return order;
    }

    public void checkLimit(List<OrderItemDTO> items) throws PaymentFailedException {
        BigDecimal amount = new BigDecimal("0");
        BigDecimal limit = new BigDecimal(creditCardLimit);
        for (OrderItemDTO item : items) {
            BigDecimal itemPrice = new BigDecimal(item.getBook().getPrice().toString());
            itemPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
            amount = amount.add(itemPrice);
            if (limit.compareTo(amount) < 0) {
                throw new PaymentFailedException((Code.PAYMENT_LIMIT_EXCEEDED));
            }
        }

    }

    @Override
    public void validatePayment(CreditCardDTO creditcard, List<OrderItemDTO> items) throws PaymentFailedException {
        if (creditcard == null) {
            throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
        }
        customerService.validateCreditCard(creditcard);
        checkLimit(items);

    }

    @Override
    @Interceptors(Tracer.class)
    public void cancelOrder(String orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {
        OrderDTO dto = findOrder(orderNr);
        if (dto.getStatus() == OrderStatus.shipped) {
            throw new OrderAlreadyShippedException();
        }
        dto.setStatus(OrderStatus.canceled);
        orderDAO.updateStatus(dto);
        sendMessage(CANCEL_ORDER_MESSAGE, dto);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setOrderState(OrderDTO dto, OrderStatus state) throws OrderNotFoundException {
            dto.setStatus(state);
            orderDAO.updateStatus(dto);
    }

    private void sendMessage(String messageType, OrderDTO dto){
        ObjectMessage message = null;
        try {
            message = jmsContext.createObjectMessage();
            message.setJMSType(messageType);
            message.setObject(dto);
            jmsContext.createProducer().send(orderQueue, message);
        } catch (JMSException ex) {
            log("JMS problem cancelOrder: " + ex.getMessage());
        }
    }
}
