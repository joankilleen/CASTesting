package org.greetings.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class Greetings {

  public String getGreeting() {
        return "Hello World!";
  }
}
