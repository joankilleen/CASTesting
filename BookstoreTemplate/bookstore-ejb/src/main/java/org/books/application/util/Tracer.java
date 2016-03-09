package org.books.application.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Joan
 */
public class Tracer {
   
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm"); 
    
    @AroundInvoke
    @AroundTimeout
    public Object trace(final InvocationContext invocation) throws Exception{
        
        try {
            Date date = new Date(System.currentTimeMillis());
            System.out.println("TRACER at: " + sdf.format(date) + " " + invocation.getMethod().getName());
            Object[] parameters = invocation.getParameters();
            if (parameters.length > 0){
                System.out.println("TRACER: " +  Arrays.toString(parameters));
            }
            return invocation.proceed( );
        }
        finally {
        
        }
            
    }/*
    public void log(String msg){
        LOG.fine("TRACER " + msg);
    }*/
    
}

