/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amazon.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.amazon.security.AmazonSecurityFactory;
import org.amazon.security.AmazonSecurityHelper;
import org.w3c.dom.Node;

/**
 *
 * @author Joan
 */
public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String SECURITY_NAMESPACE = "http://security.amazonaws.com/doc/2007-01-01/";
    private static final String NAMESPACE = "http://webservices.amazon.com/AWSECommerceService/2011-08-01";
    private static final String NS = "ns";
    private static final String END_POINT = "https://webservices.amazon.com/onca/soap?Service=AWSECommerceService";
    private static final String AWS_ACCESS_KEY = "AWSAccessKeyId";
    private static final String TIMESTAMP_TAG = "Timestamp";
    private static final String SIGNATURE_TAG = "Signature";
    private static final String ASSOCIATE_TAG = "AssociateTag";
    private static final String AWS_PREFIX = "AWS";
    private static final String OPERATION_PREFIX = "oper";
    private static final String OPERATION_NAMESPACE = "http://webservices.amazon.com/AWSECSCommerce/onca/soap";

    @Override
    public Set<QName> getHeaders() {
        final QName securityHeader = new QName(SECURITY_NAMESPACE, "AWS");

        final Set<QName> headers = new HashSet<QName>();
        headers.add(securityHeader);

        return null;

    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (!outbound) {
                return true;
            }
            // determine the operation

            SOAPMessage message = context.getMessage();
            SOAPBody body = message.getSOAPBody();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader sh = envelope.getHeader();

            Node firstChild = body.getFirstChild();
            String operation = firstChild.getLocalName();

            // create the security credentials
            AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper(operation);

            // add the credentials to the header
            sh.addNamespaceDeclaration(AWS_PREFIX, SECURITY_NAMESPACE);

            SOAPHeaderElement accessKey = sh.addHeaderElement(new QName(SECURITY_NAMESPACE, AWS_ACCESS_KEY, AWS_PREFIX));
            accessKey.addTextNode(helper.getAWSAccessKeyId());

            // add timestamp
            SOAPHeaderElement timestamp = sh.addHeaderElement(new QName(SECURITY_NAMESPACE, TIMESTAMP_TAG, AWS_PREFIX));
            timestamp.addTextNode(helper.getTimeStamp());

            // add signature
            SOAPHeaderElement signature = sh.addHeaderElement(new QName(SECURITY_NAMESPACE, SIGNATURE_TAG, AWS_PREFIX));
            signature.addTextNode(helper.getSignature());
            
            //System.out.println("Message after adding header:");
            //message.writeTo(System.out);

        } catch (SOAPException ex) {
            Logger.getLogger(AmazonSecurityHandler.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (IOException ex) {
            Logger.getLogger(AmazonSecurityHandler.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {

        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}
