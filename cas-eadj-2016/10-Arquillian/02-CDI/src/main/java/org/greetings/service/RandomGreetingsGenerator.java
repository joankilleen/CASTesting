package org.greetings.service;

// tag::main[]

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RandomGreetings // <1>
@ApplicationScoped // <2>
public class RandomGreetingsGenerator implements GreetingsGenerator {

  // tag::staticMsg[]
  static List<String> greetings = new ArrayList<>(Arrays.asList("Hello ", "Welcome, "));
  // end::staticMsg[]
  private Random random = new Random();

  @Override
  public String generate(String name) { // <3>
    int prefixIndex = random.nextInt(2);
    return greetings.get(prefixIndex) + name;
  }

  // tag::staticMsg[]
  @Override
  public void addGreeting(String greeting) {
    this.greetings.add(greeting);
  }
  // end::staticMsg[]
}
// end::main[]
