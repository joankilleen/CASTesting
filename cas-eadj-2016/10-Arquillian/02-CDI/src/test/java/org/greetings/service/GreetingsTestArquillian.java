package org.greetings.service;

// tag::all[]
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class) // <1>
public class GreetingsTestArquillian {

  @Deployment // <2>
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class) // <3>
                .addClasses(Greetings.class,
                        GreetingsGenerator.class,
                        RandomGreetings.class,
                        RandomGreetingsGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @EJB
  Greetings greetings; // <4>

  @Test
  public void shouldGetARandomMessage() {
    String message = greetings.generatesGreeting(1); // <5>
    assertThat(message, containsString("John Smith"));
  }
}
// end::all[]
