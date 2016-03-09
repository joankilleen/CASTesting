package org.books.persistence;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.books.data.dto.OrderInfo;
import org.books.data.entity.CustomerEntity;
import org.books.data.entity.OrderEntity;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import javax.persistence.TemporalType;
import org.books.data.OrderStatus;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderItemDTO;
import org.books.data.entity.BookEntity;
import org.books.data.entity.OrderItem;
import org.books.persistence.exception.BookEntityNotFound;
import org.books.persistence.exception.CustomerEntityNotFound;

@Stateless
public class OrderDAO extends GenericDAO<OrderEntity> {
    @EJB 
    private SequenceService sequenceService;
    @EJB
    private BookDAO bookDAO;
    @EJB 
    private CustomerDAO customerDAO;

    public OrderDTO find(String number) {
        log("searching order by number email " + number);
        OrderEntity entity = queryOrder(number, LockModeType.NONE);
        if (entity==null) return null; 
        return OrderDTO.copyEntityTODTO(entity);
    
    }
    public OrderEntity queryOrder(String number, LockModeType lockMode){
        OrderEntity entity = null;
        TypedQuery<OrderEntity> query = this.entityManager.createNamedQuery("Order.FindByNumber", OrderEntity.class);
        query.setLockMode(lockMode);
        query.setParameter("number", number);
        try{
            entity = query.getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
        return entity;
    }

    public List<OrderInfo> search(CustomerDTO customer, Integer year) {
        List<OrderInfo> orders = null;
        
        String strYear = year.toString();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (strYear.length()==2){
            df = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        }
               
        String strFrom = strYear + "/01/01 00:00:00";
        String strTo = strYear + "/12/31 23:59:59";
        
        Date dateFrom = null;
        Date dateTo = null;
        try {
            dateFrom = df.parse(strFrom);
            dateTo = df.parse(strTo);
        } catch (ParseException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TypedQuery<OrderInfo> q = entityManager.createNamedQuery("Order.FindByYearAndCustomer", OrderInfo.class);
        q.setParameter("cust_num", customer.getNumber());
        q.setParameter("date_from", dateFrom, TemporalType.DATE);
        q.setParameter("date_to", dateTo, TemporalType.DATE);
        return q.getResultList();

    }
    
    public String findMaxNumber(){
        TypedQuery<String> q = entityManager.createNamedQuery("Order.FindMaxNumber", String.class);
        String maxNumber = q.getSingleResult();
        return maxNumber;
    }
    public OrderDTO placeOrder(String customerNr, List<OrderItemDTO> items) throws CustomerEntityNotFound, BookEntityNotFound{
        OrderEntity entity = new OrderEntity();  
        TypedQuery<CustomerEntity> q = entityManager.createNamedQuery("CustomerEntity.FindByNumber", CustomerEntity.class);
        CustomerEntity customer = customerDAO.queryCustomer(customerNr);
        if (customer == null) throw new CustomerEntityNotFound();
        entity.setCustomer(customer);
        entity.setAddress(customer.getAddress());
        entity.setCreditCard(customer.getCreditCard());
        String orderNr = sequenceService.getNewOrderNumber();
        log("New order Nr: " + entity);
        entity.setNumber(orderNr);
        entity.setDate(new java.sql.Date(System.currentTimeMillis()));
        BigDecimal amount = new BigDecimal("0");
        List<OrderItem> list = new ArrayList<OrderItem>(items.size());
        for (OrderItemDTO item: items){
            OrderItem itemEntity = new OrderItem();
            BookEntity bookEntity = bookDAO.find(item.getBook());
            if (bookEntity == null){
                throw new BookEntityNotFound(item.getBook());
            }
            itemEntity.setBook(bookEntity);
            itemEntity.setPrice(bookEntity.getPrice());
            itemEntity.setQuantity(item.getQuantity());
            list.add(itemEntity);
            BigDecimal itemPrice = new BigDecimal(bookEntity.getPrice().toString());
            itemPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
            log("itemPrice for order: " + itemPrice + " " + orderNr);
            amount = amount.add(itemPrice);
        }
        entity.setAmount(amount);
        entity.setOrderItems(list);
        entity.setStatus(OrderStatus.accepted);
        super.create(entity);
        log("New order created: " + entity);
        return OrderDTO.copyEntityTODTO(entity);
    }
    public SequenceService getSequenceService() {
        return sequenceService;
    }

    public void setSequenceService(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }
    public OrderDTO updateStatus(OrderDTO order){
        OrderEntity entity = queryOrder(order.getNumber(), LockModeType.PESSIMISTIC_WRITE);
        if (entity==null) return null;
        entity.setStatus(order.getStatus());
        entity = super.update(entity);
        return OrderDTO.copyEntityTODTO(entity);
    }
    
    
    
    
}
