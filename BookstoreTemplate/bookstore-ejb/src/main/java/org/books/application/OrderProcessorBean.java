/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.util.Tracer;
import org.books.data.OrderStatus;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.OrderDTO;


/**
 *
 * @author guthei
 */
@MessageDriven(
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue"),
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class OrderProcessorBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(OrderProcessorBean.class.getName());
    private static final int TIMER_MILLISECOND_FACTOR = 60000;

    @EJB
    private OrderServiceLocal orderServiceLocal;
    @EJB
    private MailService mailSerivce;
    @Resource(name = "timer")
    private Integer timer;
    @Resource 
    TimerService timerService;
    private TimerConfig timerConfig = null;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Interceptors(Tracer.class)
    public void onMessage(Message message) {
        try {
            ObjectMessage orderMessage = (ObjectMessage) message;
            String messageType = orderMessage.getJMSType();
            OrderDTO dto = (OrderDTO)orderMessage.getObject();
            String orderNumber =dto.getNumber();
            log("new message received as:  " + orderNumber + messageType);
            if (messageType.equals(OrderServiceBean.NEW_ORDER_MESSAGE)) {
                timerConfig = new TimerConfig(dto, false);
                timerService.createSingleActionTimer((timer * TIMER_MILLISECOND_FACTOR), timerConfig);
                orderServiceLocal.setOrderState(dto, OrderStatus.processing);
                mailSerivce.sendProcessingMail(dto);
                log("Order processing time started: " + orderNumber + " minutes: " + timer);
            } else {
                log("cancelling timer for:  " + orderNumber + messageType);
                cancelTimer(dto);
                orderServiceLocal.setOrderState(dto, OrderStatus.canceled);
            }
        } catch (Exception ex) {
            log("JMS problem receiving message");
            throw new EJBException(ex);
        }
    }

    @Timeout
    @Interceptors(org.books.application.util.Tracer.class)
    public void shipOrder(Timer timer) throws OrderNotFoundException {
        OrderDTO dto = (OrderDTO)timer.getInfo();
        orderServiceLocal.setOrderState(dto, OrderStatus.shipped);
        mailSerivce.sendShippingMail(dto);
    }
    @Interceptors(Tracer.class)
    public void cancelTimer(OrderDTO dto) {
        String timerNumber = dto.getNumber();
        for (Timer timer : timerService.getTimers()) {
            OrderDTO timerDTO = (OrderDTO)timer.getInfo();
            
            if (dto.getNumber().equals(timerNumber)) {
                log("Order canceled: " + timerNumber);
                timer.cancel();
            }
        }
    }

    

    private void log(String msg) {
        LOG.info(this.getClass().getSimpleName() + " " + msg);
    }
}
