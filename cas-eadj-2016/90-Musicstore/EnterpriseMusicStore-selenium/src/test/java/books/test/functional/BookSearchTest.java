package books.test.functional;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class BookSearchTest extends SeleneseTestCase {
	
    private DefaultSelenium selenium;
    
	public void setUp() throws Exception {
		 this.selenium  = new DefaultSelenium("localhost", 8099, "*googlechrome", "http://localhost:8080/");
		 this.selenium.setSpeed("1000");
	     this.selenium.start();
	}	
	
	public void testSearch() throws Exception {
		selenium.open("/musicstore/faces/index.xhtml");
		selenium.windowMaximize(); // For Demo
		selenium.type("sidebar_form:q", "Hells Bells");
		selenium.click("sidebar_form:search");
		Thread.sleep(100);
		verifyEquals("1", selenium.getXpathCount("//ul[@id='album-list']/li"));
	}
}