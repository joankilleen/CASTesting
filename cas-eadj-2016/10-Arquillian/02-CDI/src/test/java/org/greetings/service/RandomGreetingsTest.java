package org.greetings.service;

// tag::all[]
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class) // <1>
public class RandomGreetingsTest {

  @Deployment // <2>
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class)
                .addClasses(GreetingsGenerator.class,
                        RandomGreetings.class,
                        RandomGreetingsGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Inject
  @RandomGreetings
  GreetingsGenerator greetingsGenerator; // <3>

  // tag::inseq[]
  @Test
  // end::all[]
  @InSequence(1) // <1>
  // tag::all[]
  public void shouldGenerateAGreeting() {
    String greeting = greetingsGenerator.generate("John Smith");
    assertThat(greeting, anyOf(containsString("Hello"), containsString("Welcome, ")));
  }

  @Test
  // end::all[]
  @InSequence(2)
  // tag::all[]
  public void shouldAddNewGreetingsMessages() {
    greetingsGenerator.addGreeting("A pleasure, ");
    assertThat(((RandomGreetingsGenerator)greetingsGenerator)
            .greetings.size(), is(3));
  }
  // end::inseq[]
}
// end::all[]
