/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderItemDTO;

/**
 *
 * @author Joan
 */
@Stateless(name = "MailService")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MailBean implements MailService {

    @Resource(name="mail/bookstore") private Session mailSession;
    private static final String XMAILER = "Bookstore Mailer";
    @Asynchronous
    public void sendShippingMail(OrderDTO order){
        Message message = new MimeMessage(mailSession);   
        try {
            // Set the message's subject
            Date timeStamp = new Date();
            message.setSubject("order " + order.getNumber() + " has been shipped");
            buildMessageBody(message, order);
            message.setHeader("X-Mailer", XMAILER);
            buildRecipient(message, order);
            message.setSentDate(timeStamp);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex ) {
            throw new EJBException(ex);
        }

     }
    public void sendProcessingMail(OrderDTO order){
        Message message = new MimeMessage(mailSession);   
        try {
            // Set the message's subject
            Date timeStamp = new Date();
            message.setSubject("order " + order.getNumber()+ " is being processed");
            buildMessageBody(message, order);
            message.setHeader("X-Mailer", XMAILER);
            buildRecipient(message, order);
            message.setSentDate(timeStamp);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex ) {
            throw new EJBException(ex);
        }
    }
    public void buildMessageBody(Message message, OrderDTO dto) throws MessagingException{
        StringBuilder text = new StringBuilder();
        text.append("Contents of order " + dto.getNumber() + ":\n");
        for (OrderItemDTO item: dto.getOrderItems()){
            text.append(item.toString());
            text.append('\n');
        }
        message.setText(text.toString());
        
    }
    public void buildRecipient(Message message, OrderDTO order) throws MessagingException, UnsupportedEncodingException{
        message.setRecipient(RecipientType.TO,
                         new InternetAddress(
                         order.getCustomer().getEmail(),
                         order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName()));
    }
}
