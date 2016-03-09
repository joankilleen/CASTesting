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
import org.amazon.webservice.soap.ItemLookup;
import org.amazon.webservice.soap.ItemLookupRequest;
import org.amazon.webservice.soap.ItemLookupResponse;
import org.amazon.webservice.soap.ItemSearch;
import org.amazon.webservice.soap.ItemSearchRequest;
import org.amazon.webservice.soap.ItemSearchResponse;
import org.amazon.webservice.soap.Items;

/**
 *
 * @author Joan
 */
public class AmazonWebServiceItemLookupClient {

    public static void main(String[] arge) {

        /* <?xml version="1.0" encoding="UTF-8"?>
         <soapenv:Envelope 
         xmlns:ns="http://webservices.amazon.com/AWSECommerceService/2011-08-01">
         <soapenv:Header
         xmlns:aws="http://security.amazonaws.com/doc/2007-01-01/">
         <aws:AWSAccessKeyId>AKIAIYFLREOYORYNAQTQ</aws:AWSAccessKeyId>
         <aws:Timestamp>2016-01-27T17:29:54Z</aws:Timestamp>
         <aws:Signature>8CHtzvEFWqoNAzI17QjU9n6vk9fmoR6PM4+Lx2WVZ0c=</aws:Signature>
         </soapenv:Header>
         <soapenv:Body>
         <ns:ItemLookup>
         <ns:AssociateTag>test0e5d-20</ns:AssociateTag>
         <ResponseGroup>ItemAttributes</ResponseGroup>
         <SearchIndex>Books</SearchIndex>
         <IdType>ISBN</IdType>
         <ns:Request>
         <ns:ItemId>076243631x</ns:ItemId>
         </ns:Request>
         </ns:ItemLookup>
         </soapenv:Body>
         </soapenv:Envelope>*/
        
        AWSECommerceService service = new AWSECommerceService();
        AWSECommerceServicePortType awseServicePort = service.getAWSECommerceServicePort();

        AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper("ItemLookup");
        ItemLookup itemLookup = new ItemLookup();

        ItemLookupRequest lookupRequest = createLookupRequest("1430272252");

        itemLookup.setAssociateTag(helper.getAssociatekey());
        itemLookup.getRequest().add(lookupRequest);

        System.out.println(itemLookup);

        ItemLookupResponse lookupResponse = awseServicePort.itemLookup(itemLookup);
        if (!lookupResponse.getItems().isEmpty()) {
            printResults(lookupResponse);
        }else{
            System.out.println("no Book found!");
        }
    }

    public static ItemLookupRequest createLookupRequest(String Isbn) {
        ItemLookupRequest searchRequest = new ItemLookupRequest();
        searchRequest.setSearchIndex("Books");
        searchRequest.setIdType("ISBN");
        searchRequest.getItemId().add(Isbn);
        searchRequest.getResponseGroup().add("ItemAttributes");
        return searchRequest;
    }

    public static void printResults(ItemLookupResponse lookupResponse) {
        List<Items> listItems = lookupResponse.getItems();

        for (Iterator<Items> it = listItems.iterator(); it.hasNext();) {
            Items items = it.next();
            List<Item> listITEM = items.getItem();
            for (Item item : listITEM) {
                ItemAttributes attributes = item.getItemAttributes();
                String formattedPrice = "no price info";
                if (attributes.getListPrice() != null) {
                    formattedPrice = attributes.getListPrice().getFormattedPrice();
                }
                System.out.println("Next attributes: " + attributes.getISBN() + " | " + attributes.getTitle() + " | " + attributes.getPublisher()
                      + " | " + formattedPrice + " | " + attributes.getAuthor());
            }

        }
    }

}
