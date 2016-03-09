package org.books.data.dto;

import org.books.data.entity.OrderEntity;
import org.books.data.entity.OrderItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.books.data.OrderStatus;


/**
 *
 * @author guthei
 */
public class OrderDTO implements Serializable {

    
    protected String number;
    protected Date date;
    protected BigDecimal amount;
    protected OrderStatus status;
    protected List<OrderItemDTO> orderItems = new ArrayList<>();
    protected CustomerDTO customer;
    protected AddressDTO address;
    protected CreditCardDTO creditCard;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public AddressDTO getAddress() {
        if (address == null) {
            address = new AddressDTO();
        }
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public CreditCardDTO getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCardDTO();
        }
        return creditCard;
    }

    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }
    
    public static OrderDTO copyEntityTODTO(OrderEntity entity){
        OrderDTO dto = new OrderDTO();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setNumber(entity.getNumber());
        dto.setStatus(entity.getStatus());
        
        CustomerDTO cust = CustomerDTO.copyEntityTODTO(entity.getCustomer());
        dto.setCustomer(cust);
        
        AddressDTO address = AddressDTO.copyEntityToDTO(entity.getAddress());
        dto.setAddress(address);
        
        CreditCardDTO card = CreditCardDTO.copyEntityToDTO(entity.getCreditCard());
        dto.setCreditCard(card);
        
        List<OrderItemDTO> list = new ArrayList<>(entity.getOrderItems().size());
        for (OrderItem item: entity.getOrderItems()){
            OrderItemDTO itemDTO = OrderItemDTO.copyEntityToDTO(item);
            list.add(itemDTO);         
        }
        dto.setOrderItems(list);
        return dto;
    }
    
    @Override
    public String toString(){
        return this.number + "  " + this.amount + " " + this.status + " " + this.date + " " + this.customer + " " + this.creditCard.toString() + " " + this.address;
    }
    

}
