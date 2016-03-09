package org.bookstore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class AppTest {

    WebDriver driver;

    @Before
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "/Users/jbandi/Temp/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void testApp() throws InterruptedException {

        driver.get("http://localhost:8080/musicstore/faces/index.xhtml");

        WebElement searchInput = driver.findElement(By.name("sidebar_form:q"));
        searchInput.sendKeys("Hells Bells");

        WebElement searchButton = driver.findElement(By.name("sidebar_form:search"));
        searchButton.click();

        Thread.sleep(100);

        List<WebElement> albums = driver.findElements(By.xpath("//ul[@id='album-list']/li"));

        assertThat(albums.size(), is(1));

        driver.quit();
    }
}
