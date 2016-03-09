/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amazon.security;

/**
 *
 * @author Joan
 */
public class AmazonSecurityHelper {
    protected static final String associatekey = "test0e5d-20";
    protected String AWSAccessKeyId = "AKIAIYFLREOYORYNAQTQ";
    protected String timeStamp = "";
    protected String operation = "";
    protected String signature = "";
    protected static final String SECRET_KEY = "taadPslXjp3a2gmthMgP369feVy32A32eM9SqkVP";
    protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    protected static final String MAC_ALGORITHM = "HmacSHA256";

    public String getAssociatekey() {
        return associatekey;
    }

    public String getAWSAccessKeyId() {
        return AWSAccessKeyId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getOperation() {
        return operation;
    }

    public String getSignature() {
        return signature;
    }
    
}
