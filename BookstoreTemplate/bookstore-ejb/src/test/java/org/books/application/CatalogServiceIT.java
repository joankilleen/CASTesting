package org.books.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.Binding;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class CatalogServiceIT extends BaseIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CatalogService";
    private static CatalogService catalogService;
    // exists already
    private BookDTO book = new BookDTO("143024626X", "Beginning Java EE 7", "Antonio Goncalves", "Apress", 2015, Binding.Paperback, 608, new BigDecimal("49.99"));
    // doesn't exist in DB
    private static String ISBN="55555555555";
    private BookDTO book1 = new BookDTO(ISBN, "New Book", "Antonio Goncalves", "Apress", 2015, Binding.Paperback, 608, new BigDecimal("49.99"));
    @Override
    protected void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogService) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }

    // book already exists
    @Test(expectedExceptions = BookAlreadyExistsException.class)
    public void addBookAlreadyExists() throws BookAlreadyExistsException {
        catalogService.addBook(book);
    }
    
    @Test
    public void addBook() {
        try  {
            catalogService.addBook(book1);
        }
        catch (BookAlreadyExistsException ex){
            fail("book should have been created");;
        }
        // try the add the book again
        try{
            catalogService.addBook(book1);
        }
        catch (BookAlreadyExistsException ex){
            return;
        }
        fail("book shouldn't have been created as it exists already");
    }

    @Test(dependsOnMethods = "addBook")
    public void findBook() throws BookNotFoundException {
        assertNotNull(catalogService.findBook(book.getIsbn()));
    }

    @Test
    public void searchBook() {
        List<BookInfo> bookInfos = new ArrayList<>();
        bookInfos = catalogService.searchBooks("Expert");

        for (BookInfo book : bookInfos) {
            log("Books found with keywords: " + " << Expert >> " + "ISBN: " + book);
        }
    }
    
    @Test (dependsOnMethods = "addBook")
    public void updateAuthors() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setAuthors("Leonardo da Vinci");
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getAuthors().equals(book1.getAuthors()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updatePublisher() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPublisher("Pub");
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getPublisher().equals(book1.getPublisher()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updateBinding() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setBinding(Binding.Unknown);
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getBinding().equals(book1.getBinding()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updateNumberOfPages() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setNumberOfPages(100);
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getNumberOfPages().equals(book1.getNumberOfPages()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updatePrice() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPrice(new BigDecimal("99.99"));
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getPrice().equals(book1.getPrice()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updateTitle() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setTitle("updated title");
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getTitle().equals(book1.getTitle()));
    }
    @Test (dependsOnMethods = "addBook")
    public void updatePublicationYear() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPublicationYear(1900);
        catalogService.updateBook(book1);
        BookDTO changedBook = catalogService.findBook(book1.getIsbn());
        assertTrue(changedBook.getPublicationYear().equals(book1.getPublicationYear()));
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookEmptyTitle() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setTitle("");
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoTitle() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setTitle(null);
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoAuthors() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setAuthors(null);
        catalogService.updateBook(book1);
    }
    public void updateBookEmptyAuthors() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setAuthors("");
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoBinding() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setBinding(null);
        catalogService.updateBook(book1);
    }
    
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoISBN() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setIsbn(null);
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookEmptyISBN() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setIsbn("");
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoPublisher() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPublisher(null);
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookEmptyPublisher() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPublisher("");
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoPublicationYear() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPublicationYear(null);
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoNumPages() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setNumberOfPages(null);
        catalogService.updateBook(book1);
    }
    @Test (dependsOnMethods = "addBook", expectedExceptions = EJBException.class)
    public void updateBookNoPrice() throws BookNotFoundException{
        book1 = catalogService.findBook(ISBN);
        book1.setPrice(null);
        catalogService.updateBook(book1);
    }
}
