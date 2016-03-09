package org.musicstore.integrationtests;

import javax.naming.Context;
import org.junit.Before;
import org.junit.Test;
import org.musicstore.Greeter;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.Assert.assertEquals;

public class EjbGreeterTest {

    private static final String JNDI_NAME_BASE = "java:global/bookstore-app/bookstore-ejb/";
    InitialContext jndi;

    @Before
    public void setUp() throws NamingException {

//        jndi = JBossUtil.createInitialContext();
        jndi = new InitialContext();
    }

    @Test
    public void hello() throws NamingException {
//        Greeter greeter = (Greeter) jndi.lookup("ejb:EnterpriseMusicStore/EnterpriseMusicStoreEJB//GreeterService!org.musicstore.Greeter");
        Greeter greeter = (Greeter) jndi.lookup("java:global/EnterpriseMusicStore/EnterpriseMusicStore-ejb/GreeterService");

        assertEquals("Greetings from EJB!", greeter.getMessage());
    }


}
