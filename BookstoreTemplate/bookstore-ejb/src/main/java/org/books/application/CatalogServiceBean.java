package org.books.application;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.util.Tracer;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.persistence.BookDAO;

@Stateless(name = "CatalogService")
@Interceptors(Tracer.class)
public class CatalogServiceBean implements CatalogService {
    @EJB
    private BookDAO bookDAO;
    //private static final Logger LOG = Logger.getLogger(CatalogServiceBean.class.getName());

    @Override
    public BookDTO addBook(BookDTO book) throws BookAlreadyExistsException {
        //log("Adding book with isbn: " + book);
        verifyCatalog(book);
        BookDTO dto = bookDAO.find(book.getIsbn());
        if (dto != null) {
            throw new BookAlreadyExistsException();
        }        
        return bookDAO.create(book);
        
    }

    @Override
    public BookDTO findBook(String isbn) throws BookNotFoundException {
        //log("Finding book with isbn: " + isbn);
        BookDTO book = bookDAO.find(isbn);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        //log("Search books with keywords: " + keywords);
        List<BookInfo> bookInfo = new ArrayList<>();
        bookInfo = bookDAO.search(keywords);
        return bookInfo;
    }

    @Override
    public void updateBook(BookDTO book) throws BookNotFoundException {
       // log("Updateing book with isbn: " + book.getIsbn());
        verifyCatalog(book);
        BookDTO bookUpdate = bookDAO.update(book);
        if (bookUpdate == null) {
            throw new BookNotFoundException();
        }
    }

    private void log(String msg) {
       // LOG.info(this.getClass().getSimpleName() + " " + msg);
    }

    private void verifyCatalog(BookDTO book) {
        // checks on isbn
        if (book.getIsbn()==null) {
            //log("rejecting book isbn: " + book.getIsbn());
            throw new IllegalArgumentException();
        }
        if (book.getIsbn().isEmpty()) {
            //log("rejecting book isbn: " + book.getIsbn());
            throw new IllegalArgumentException();
        }
        // checks on title
        if (book.getTitle()==null) {
           // log("rejecting book title: " + book.getTitle());
            throw new IllegalArgumentException();
        }
        if (book.getTitle().isEmpty()) {
            //log("rejecting book title: " + book.getTitle());
            throw new IllegalArgumentException();
        }
        // checks on authors
        if (book.getAuthors()==null) {
           // log("rejecting book authors: " + book.getAuthors());
            throw new IllegalArgumentException();
        }
        if (book.getAuthors().isEmpty()) {
            //log("rejecting book authors: " + book.getAuthors());
            throw new IllegalArgumentException();
        }
        if (book.getPublisher()==null) {
            //log("rejecting book authors: " + book.getAuthors());
            throw new IllegalArgumentException();
        }
        if (book.getPublisher().isEmpty()) {
            //log("rejecting book authors: " + book.getAuthors());
            throw new IllegalArgumentException();
        }
        // checks on publication year
        if (book.getPublicationYear() == null) {
            //log("rejecting book publication year: " + book.getPublicationYear());
            throw new IllegalArgumentException();
        }
        // checks on binding
        if (book.getBinding() == null) {
            log("rejecting book binding: " + book.getBinding());
            throw new IllegalArgumentException();
        }
        // checks on number of pages
        if (book.getNumberOfPages() == null) {
            log("rejecting book numnber of pages: " + book.getNumberOfPages());
            throw new IllegalArgumentException();
        }
        // checks on price
        if (book.getPrice() == null) {
            log("rejecting book price: " + book.getPrice());
            throw new IllegalArgumentException();
        }
    }

}
