package org.bookstore;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class AppTest {

    @Test
    public void shouldMatch() {
        App app = new App();
        assertThat(app.greet(), is("Hello World!"));
    }

    @Test
    public void shouldNotMatch() {
        App app = new App();
        assertThat(app.greet(), is(not("Hello World")));
    }

    @Test
    public void shouldNotBeNull() {
        App app = new App();
        assertThat(app.greet(), is(notNullValue()));
    }

    @Test
    public void shouldAlsoNotBeNull() {
        App app = new App();
        assertThat(app.greet(), is(not(nullValue())));
    }

    @Test
    public void shouldReallyNotBeNull() {
        App app = new App();
        assertThat(app.greet(), is(not(not(notNullValue()))));
    }

    @Test
    public void shouldMatchAll() {
        App app = new App();
        assertThat(app.greet(), is(allOf(notNullValue(), instanceOf(String.class))));
    }

    @Test
    public void shouldMatchAny() {
        App app = new App();
        assertThat(app.greet(), is(anyOf(notNullValue(), instanceOf(Integer.class))));
    }

    @Test
    public void shouldNotMatchAll() {
        App app = new App();
        assertThat(app.greet(), is(not(allOf(notNullValue(), instanceOf(Integer.class)))));
    }

}
