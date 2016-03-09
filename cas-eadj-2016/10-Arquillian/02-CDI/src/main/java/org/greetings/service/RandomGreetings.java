package org.greetings.service;

// tag::main[]
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier // <1>
@Retention(RUNTIME) // <2>
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface RandomGreetings {
}
// end::main[]