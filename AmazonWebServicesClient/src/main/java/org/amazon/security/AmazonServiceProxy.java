/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amazon.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 *
 * @author Joan
 */
public class AmazonServiceProxy {

    private static final String SECURITY_NAMESPACE = "http://security.amazonaws.com/doc/2007-01-01/";
    private static final String NAMESPACE = "http://webservices.amazon.com/AWSECommerceService/2011-08-01";
    private static final String NS = "ns";
    private static final String END_POINT = "https://webservices.amazon.com/onca/soap?Service=AWSECommerceService";
    private static final String AWS_ACCESS_KEY = "AWSAccessKeyId";
    private static final String TIMESTAMP_TAG = "Timestamp";
    private static final String SIGNATURE_TAG = "Signature";
    private static final String ASSOCIATE_TAG = "AssociateTag";
    private static final String AWS_PREFIX = "aws";
    private static final String OPERATION_PREFIX = "oper";
    private static final String OPERATION_NAMESPACE = "http://webservices.amazon.com/AWSECSCommerce/onca/soap";

    public void ItemSearch(String keywords, AmazonSecurityHelper helper) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage request = messageFactory.createMessage();
            SOAPPart part = request.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            envelope.addNamespaceDeclaration(NS, NAMESPACE);

            SOAPHeader sh = request.getSOAPHeader();
            sh.addNamespaceDeclaration(AWS_PREFIX, SECURITY_NAMESPACE);

            SOAPHeaderElement accessKey = sh.addHeaderElement(envelope.createName(AWS_ACCESS_KEY, AWS_PREFIX, SECURITY_NAMESPACE));
            accessKey.addTextNode(helper.getAWSAccessKeyId());

            // add timestamp
            SOAPHeaderElement timestamp = sh.addHeaderElement(envelope.createName(TIMESTAMP_TAG, AWS_PREFIX, SECURITY_NAMESPACE));
            timestamp.addTextNode(helper.getTimeStamp());

            // add signature
            SOAPHeaderElement signature = sh.addHeaderElement(envelope.createName(SIGNATURE_TAG, AWS_PREFIX, SECURITY_NAMESPACE));
            signature.addTextNode(helper.getSignature());
            
            SOAPBody body = request.getSOAPBody();

            SOAPElement bodyElement = body.addChildElement("ItemSearch");
            bodyElement.addNamespaceDeclaration(OPERATION_PREFIX, OPERATION_NAMESPACE);
            
            SOAPElement accessElement = bodyElement.addChildElement(AWS_ACCESS_KEY, OPERATION_PREFIX);
            accessElement.addTextNode(helper.getAWSAccessKeyId());
            
            SOAPElement associateElement = bodyElement.addChildElement(ASSOCIATE_TAG, OPERATION_PREFIX);
            associateElement.addTextNode(helper.getAssociatekey());
            SOAPElement requestElement = bodyElement.addChildElement("Request", OPERATION_PREFIX);
            SOAPElement searchIndex = requestElement.addChildElement("SearchIndex", OPERATION_PREFIX);
            searchIndex.addTextNode("Books");
            SOAPElement keywordsElement = requestElement.addChildElement("Keywords", OPERATION_PREFIX);
            keywordsElement.addTextNode(keywords);
            request.writeTo(System.out);
            
             SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
             SOAPConnection connection = connectionFactory.createConnection();
             SOAPMessage response = connection.call(request, END_POINT);
             connection.close();
               
             System.out.println();
             System.out.println();
             body = response.getSOAPBody();
             response.writeTo(System.out);
             
             
        } catch (IOException | SOAPException ex) {
            Logger.getLogger(AmazonServiceProxy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
