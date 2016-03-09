package org.greetings.service;

// tag::initial[]
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
// <1>
// tag::fixed[]
public class Greetings {

  @Inject // <2>
  // end::initial[]
  @RandomGreetings // <1>
  // tag::initial[]
  GreetingsGenerator greetingsGenerator;

  // ....
  // end::fixed[]
  public String generatesGreeting(int userId) {
    String fullName = findFullNameById(userId); // <3>
    return greetingsGenerator.generate(fullName);
  }

  // end::initial[]
  private String findFullNameById(int id) {
    return "John Smith";
  }
  // tag::initial[]
  // tag::fixed[]
}
// end::initial[]
// end::fixed[]
