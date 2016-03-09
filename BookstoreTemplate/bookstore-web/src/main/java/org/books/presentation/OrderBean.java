/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.CustomerService;
import org.books.application.OrderService;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.CreditCardType;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.data.dto.AddressDTO;
import org.books.data.dto.CreditCardDTO;
import org.books.data.dto.CustomerDTO;
import org.books.util.MessageFactory;

/**
 *
 * @author guthei
 */
@Named("orderBean")
@SessionScoped
public class OrderBean implements Serializable {

    private static final String NO_ORDERS_ID = "org.books.presentation.NO_ORDERS";
    private static final String CUSTOMER_NOT_FOUND_ID = "org.books.presentation.CUSTOMER_NOT_FOUND";
    private static final String CREDIT_CARD_LIMIT_EXCEEDED_ID = "org.books.presentation.CREDIT_CARD_LIMIT_EXCEEDED";
    private static final String CREDIT_CARD_EXPIRED_ID = "org.books.presentation.CREDIT_CARD_EXPIRED";
    private static final String ORDER_NOT_FOUND_ID = "org.books.presentation.ORDER_NOT_FOUND";
    private static final String NO_BOOK_FOUND_ID = "org.books.presentation.NO_BOOK_FOUND";
    private static final String ORDER_ALREADY_SHIPPED_ID = "org.books.presentation.ORDER_ALREADY_SHIPPED";
    @Inject
    AccountBean accountBean;
    @Inject
    private ShoppingCart shoppinCart;
    @EJB
    private CustomerService customerService;
    @EJB
    private OrderService orderService;
    private CustomerDTO customer = null;
    private AddressDTO address = null;
    private CreditCardDTO creditCard = null;
    private String year;
    private OrderDTO orderDTO;
    private List<OrderItemDTO> orderDTOs;
    private List<OrderInfo> orderInfos;

    @PostConstruct
    public void init() {
        try {
            customer = customerService.findCustomer(accountBean.getAuthCustomer().getNumber());
            address = new AddressDTO();
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(OrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public AddressDTO getAddress() {
        address.setCity(customer.getAddress().getCity());
        address.setCountry(customer.getAddress().getCountry());
        address.setPostalCode(customer.getAddress().getPostalCode());
        address.setStreet(customer.getAddress().getStreet());
        return address;
    }

    public CreditCardDTO getCreditCard() {
        creditCard = customer.getCreditCard();
        return creditCard;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCustomerName() {
        StringBuilder custName = new StringBuilder(customer.getFirstName());
        custName.append(' ');
        custName.append(customer.getLastName());
        return custName.toString();
    }

    public String getCustomerCreditCardType() {
        CreditCardType type = customer.getCreditCard().getType();
        switch (type) {
            case MasterCard:
                return "MasterCard";
            default:
                return "Visa";
        }
    }

    public String getCustomerCreditCardNumber() {
        String input = customer.getCreditCard().getNumber();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ");
            }

            result.append(input.charAt(i));
        }
        return result.toString();
    }

    public String getCustomerCreditCardExpiry() {
        String month = customer.getCreditCard().getExpirationMonth().toString();
        StringBuilder expiry = new StringBuilder();
        expiry.append(month);
        expiry.append('/');
        Integer year = (customer.getCreditCard().getExpirationYear()) % 100;
        expiry.append(year.toString());
        return expiry.toString();

    }

    public String createOrder() {
        try {
            this.orderDTO = orderService.placeOrder(customer.getNumber(), createOrderItems(shoppinCart.getItems()));
            shoppinCart.clearShoppingCart();
        } catch (PaymentFailedException ex) {
            if (ex.getCode() == PaymentFailedException.Code.PAYMENT_LIMIT_EXCEEDED) {
                MessageFactory.error(CREDIT_CARD_LIMIT_EXCEEDED_ID);
            } else  {
                MessageFactory.error(CREDIT_CARD_EXPIRED_ID);
            } 
        }
        catch(CustomerNotFoundException ex) {
                MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
        } 
        catch (BookNotFoundException ex) {
                MessageFactory.error(NO_BOOK_FOUND_ID);
        }
        
        return null;
    }

    private List<OrderItemDTO> createOrderItems(List<CartItem> cartItem) {
        this.orderDTOs = new ArrayList<>();
        for (int i = 0; i < cartItem.size(); i++) {
            OrderItemDTO orderItem = new OrderItemDTO();
            orderItem.setBook(cartItem.get(i).getBook());
            orderItem.setPrice(cartItem.get(i).getBook().getPrice());
            orderItem.setQuantity(cartItem.get(i).getQuantity());
            orderDTOs.add(orderItem);
        }
        return orderDTOs;
    }

    public String listOrders() {
        try {
            this.orderInfos = orderService.searchOrders(customer.getNumber(), Integer.parseInt(getYear()));
            if (this.orderInfos.size() == 0) {
                MessageFactory.error(NO_ORDERS_ID);
            }
        } catch (CustomerNotFoundException ex) {
                MessageFactory.error(CUSTOMER_NOT_FOUND_ID);
        }
        
        return null;
    }

    public String selectOrder(OrderInfo order) {
        try {
            this.orderDTO = orderService.findOrder(order.getNumber());
            return "orderDetails";
        } catch (OrderNotFoundException ex) {
            Logger.getLogger(AccountBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getTotalPrice() {
        double totalPriceCalc = 0;

        for (int i = 0; i < this.orderDTO.getOrderItems().size(); i++) {
            double nextPrice = this.orderDTO.getOrderItems().get(i).getBook().getPrice().doubleValue();
            totalPriceCalc += this.orderDTO.getOrderItems().get(i).getQuantity() * nextPrice;
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(totalPriceCalc);
    }

    public String cancelOrder(String orderNumber) {
        try {
            orderService.cancelOrder(orderNumber);
            listOrders();
        } catch (OrderNotFoundException ex) {
                MessageFactory.error(ORDER_NOT_FOUND_ID);
        }
        catch (OrderAlreadyShippedException ex) {
                MessageFactory.error(ORDER_ALREADY_SHIPPED_ID);
        }
        return "accountDetails"; 
    }
}
