/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amazon.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * The factory AmazonSecurityFactory computes the security parameters needed in requests of
 Amazon's Product Advertising API.
 */
public class AmazonSecurityFactory {

    public AmazonSecurityHelper createHelper(String operation){ 
            AmazonSecurityHelper helper = new AmazonSecurityHelper();
            helper.operation = operation;
        try {
            
            DateFormat dateFormat = new SimpleDateFormat(AmazonSecurityHelper.TIMESTAMP_FORMAT);
            helper.timeStamp = dateFormat.format(Calendar.getInstance().getTime());
            
            Mac mac = Mac.getInstance(AmazonSecurityHelper.MAC_ALGORITHM);
            SecretKey key = new SecretKeySpec(AmazonSecurityHelper.SECRET_KEY.getBytes(), AmazonSecurityHelper.MAC_ALGORITHM);
            try {
                mac.init(key);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(AmazonSecurityFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] data = mac.doFinal((operation + helper.timeStamp).getBytes());
            helper.signature = DatatypeConverter.printBase64Binary(data);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AmazonSecurityFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return helper;
    }
}
