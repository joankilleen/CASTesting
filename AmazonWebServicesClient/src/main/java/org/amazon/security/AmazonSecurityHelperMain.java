package org.amazon.security;

import org.amazon.security.AmazonSecurityFactory;
import org.amazon.security.AmazonSecurityHelper;
import org.amazon.security.AmazonServiceProxy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class AmazonSecurityHelperMain {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
                AmazonSecurityHelper helper = new AmazonSecurityFactory().createHelper("ItemSearch");
            
		System.out.println("AssociateTag:   " + helper.getAssociatekey());
		System.out.println("AWSAccessKeyId: " + helper.getAWSAccessKeyId());
		System.out.println("Timestamp:      " + helper.getTimeStamp());
		System.out.println("Signature:      " + helper.getSignature());
                
                new AmazonServiceProxy().ItemSearch("Harry%20Potter", helper);
	}
        
        
}