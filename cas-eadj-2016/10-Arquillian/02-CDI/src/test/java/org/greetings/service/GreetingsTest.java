package org.greetings.service;

// tag::main[]
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.greetings.service.Greetings;
import org.junit.Test;

// end::main[]
// tag::empty[]
/**
 * First Gap
 **/
// end::empty[]
// tag:main[]
public class GreetingsTest {

  // end::main[]
  // tag::empty[]
  /**
    * Second Gap
   **/

  /**
    * Third Gap
  **/
  // end::empty[]
  // tag::main[]
  @Test // <1>
  public void shouldCalculateTotalPrice() {
    Greetings greetings = new Greetings(); // <2>
    greetings.greetingsGenerator = new RandomGreetingsGenerator(); // <3>

    String message = greetings.generatesGreeting(1); // <4>
    assertThat(message, containsString("John Smith"));
  }
}
// end::main[]
