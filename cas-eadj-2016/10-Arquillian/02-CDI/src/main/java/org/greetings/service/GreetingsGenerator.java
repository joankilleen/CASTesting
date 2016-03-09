package org.greetings.service;

// tag::main[]
public interface GreetingsGenerator {

  String generate(String name);
  void addGreeting(String greeting);
}
// end::main[]
