/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amazon.client;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.amazon.security.AmazonSecurityFactory;
import org.amazon.security.AmazonSecurityHelper;
import javax.xml.ws.BindingProvider;
import org.amazon.webservice.soap.AWSECommerceService;
import org.amazon.webservice.soap.AWSECommerceServicePortType;
import org.amazon.webservice.soap.Errors;
import org.amazon.webservice.soap.Item;
import org.amazon.webservice.soap.ItemAttributes;
import org.amazon.webservice.soap.ItemSearch;
import org.amazon.webservice.soap.ItemSearchRequest;
import org.amazon.webservice.soap.ItemSearchResponse;
import org.amazon.webservice.soap.Items;

/**
 *
 * @author Joan
 */
public class AmazonWebServiceClient {

    public static void main(String[] arge) {

        /*<?xml version="1.0" encoding="UTF-8"?>
         <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://webservices.amazon.com/AWSECommerceService/2011-08-01">
         <soapenv:Header xmlns:aws="http://security.amazonaws.com/doc/2007-01-01/">
         <aws:AWSAccessKeyId>AKIAIYFLREOYORYNAQTQ</aws:AWSAccessKeyId>
         <aws:Timestamp>2016-01-27T17:44:11Z</aws:Timestamp>
         <aws:Signature>MnD4wPfTKrvRQyxPfzWqqWcNjDmkoGD6eSzAFeVLrrw=</aws:Signature>
         </soapenv:Header>
         <soapenv:Body>
         <ns:ItemSearch>
         <ns:AssociateTag>test0e5d-20</ns:AssociateTag>
         <ns:Request>
         <ns:Title>JPA</ns:Title>
         <ns:Publisher>Apress</ns:Publisher>
         <ns:SearchIndex>Books</ns:SearchIndex>
         <ns:ResponseGroup>ItemAttributes</ns:ResponseGroup>
         </ns:Request>
         </ns:ItemSearch>
         </soapenv:Body>
         </soapenv:Envelope>*/
        AWSECommerceService service = new AWSECommerceService();
        AWSECommerceServicePortType awseServicePort = service.getAWSECommerceServicePort();
        /*((BindingProvider) awseServicePort).getRequestContext().put(
         BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
         "http://distsys.ch:8080/contactbook/soap");*/

        AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper("ItemSearch");
        ItemSearch itemSearch = new ItemSearch();
        
        String keywords = "Beginning Java Apress";
        ItemSearchRequest searchRequest = createSearchRequest(keywords, new BigInteger("1"));

        itemSearch.setAssociateTag(helper.getAssociatekey());
        itemSearch.getRequest().add(searchRequest);
        
        System.out.println(itemSearch);
        
        ItemSearchResponse searchResponse = awseServicePort.itemSearch(itemSearch);
        System.out.println("Search Response items: " + searchResponse.getItems().size() );
        
        
        int totalPages = printResults(searchResponse);
        for (int i=2; i<totalPages && i<11; i++){
            Integer intI = new Integer(i);
            System.out.println("going to request page " + intI);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(AmazonWebServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            searchRequest = createSearchRequest(keywords, new BigInteger(intI.toString()));
            AmazonSecurityHelper newHelper = new AmazonSecurityFactory().createHelper("ItemSearch");
            ItemSearch newItemSearch = new ItemSearch();
            newItemSearch.setAssociateTag(newHelper.getAssociatekey());
            newItemSearch.getRequest().add(searchRequest);
        
            searchResponse = awseServicePort.itemSearch(newItemSearch);
            
            printResults(searchResponse);
        }
        
        
    }
    public static ItemSearchRequest createSearchRequest(String keywords, BigInteger itemPage ){
        ItemSearchRequest searchRequest = new ItemSearchRequest();
        searchRequest.setSearchIndex("Books");
        searchRequest.setKeywords(keywords);
        searchRequest.setItemPage(itemPage);
        System.out.println("search request item page is now set to " + searchRequest.getItemPage());
        searchRequest.getResponseGroup().add("ItemAttributes");  
        

        
        return searchRequest;
    }
    public static int printResults(ItemSearchResponse searchResponse){
        List<Items> listItems = searchResponse.getItems();
        int totalPages = 0;
        int currentPage = 0;
        for (Items items: listItems) {
            totalPages = items.getTotalPages().intValue();
            
            List<Item> listITEM = items.getItem();
            for(Item item: listITEM){
                Errors errors = item.getErrors();
                if ( errors != null && errors.getError().size() > 0){
                    System.out.println(errors.getError());
                    return 11;
                }              
                ItemAttributes attributes = item.getItemAttributes();
                String formattedPrice = "no price info";
                if (attributes.getListPrice() != null){
                    formattedPrice = attributes.getListPrice().getFormattedPrice();
                    
                }
                System.out.println("Next attributes: " + attributes.getISBN() + " " + attributes.getTitle() + " " + attributes.getPublisher()
                                    + " " + formattedPrice + " " + attributes.getAuthor());
            }
            
        }
        return totalPages;
    }

}
