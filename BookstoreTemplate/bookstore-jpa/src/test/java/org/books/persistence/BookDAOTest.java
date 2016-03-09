/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package org.books.persistence;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.books.data.Binding;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.BookEntity;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;
/**
 *
 * @author Joan
 */
public class BookDAOTest extends BaseTest {
    
    
    @Test
    public void testCreate(){
        BookEntity newBook = new BookEntity();
        newBook.setAuthors("Killeen and Gut");
        newBook.setBinding(Binding.Unknown);
        newBook.setIsbn("12345");
        newBook.setNumberOfPages(500);
        newBook.setPrice(new BigDecimal("100.99"));
        newBook.setTitle("Dummies guide to Enterprise Applications");
        newBook.setPublicationYear(2015);
        newBook.setPublisher("Addison Wesley");
        EntityTransaction t = em.getTransaction();
        t.begin();
        newBook = bookDAO.create(newBook);
        t.commit();
        assertNotNull(newBook);
        log("New Book created: " + newBook.getId() + " " + newBook.getIsbn());
    }
    
    @Test
    public void buildCriteriaQuery() {
        bookDAO.setEntityManager(em);
        List<BookInfo> result = bookDAO.search("Expert");
        assertTrue(result.size() > 0);  
        int i = 0;
        for (BookInfo bookInfo : result) {
            i++;
            log("Book found: " + i + " " + bookInfo.getTitle() + " " + bookInfo.getIsbn() + " " + bookInfo.getPrice());
        }
        result = bookDAO.search("");
        log("books found: " + result.size() + " " + "keywords: " + "");
    }
    @Test(dependsOnMethods = "buildCriteriaQuery")
    public void find(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        
        BookDTO book = bookDAO.find("1430249269");
        transaction.commit();
        em.clear();
        assertNotNull(book);  
        log("Book found: " + book.getIsbn()  + " " + book.getTitle());     
        
    }
    @Test(dependsOnMethods = "find")
    public void testUpdate(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        BookDTO book = bookDAO.find("1430249269");
        book.setPrice(new BigDecimal("105.99"));
        book.setTitle("EJB for Dummies 2nd Edition");
        book.setPublisher("Oracle Publications");
        book = bookDAO.update(book);
        transaction.commit();
        em.clear();
        log("Book updated: " +  book.getTitle() + " " + book.getPublisher() + " " + book.getPrice());
        
    }
    @Test(dependsOnMethods = "testUpdate")
    public void testDelete(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        BookDTO book = bookDAO.find("1430249269");
        bookDAO.delete(book);
        transaction.commit();
        em.clear();
        
        book = bookDAO.find("1430249269");
        assertTrue(book==null);      
    }
    
}
