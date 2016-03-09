/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.*;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.util.MessageFactory;

/**
 *
 * @author guthei
 */
@Named("catalogBean")
@SessionScoped
public class CatalogBean implements Serializable {
   
    @EJB
    private  CatalogService catalogService;
    private BookInfo selectedBook;
    private String isbn;
    private List <BookInfo> books;
    private static final String NO_BOOK_FOUND_ID = "org.books.presentation.NO_BOOK_FOUND";

    public List<BookInfo> getBooks() {
        return books;
    }

    public void setBooks(List<BookInfo> books) {
        this.books = books;
    }
    private String keywords;


    public BookDTO getSelectedBook() {
        BookDTO bookSelected = null;
        try{
            bookSelected = catalogService.findBook(selectedBook.getIsbn());
        }
        catch (BookNotFoundException e){
            MessageFactory.error(NO_BOOK_FOUND_ID);
        }
        return bookSelected;
    }

    public void setSelectedBook(BookInfo selectedBook) {
        this.selectedBook = selectedBook;
    }
    
    public CatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }
 

    public void setBooks(ArrayList<BookInfo> books) {
        this.books = books;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

 
    
    public String searchBook(){
        
        books = catalogService.searchBooks(this.keywords);
        if (books.isEmpty()) {
            MessageFactory.info(NO_BOOK_FOUND_ID);
            return null;
        }
        return null;
    }
    
    public String selectBook(BookInfo book){
        this.selectedBook = book;
        return "bookDetails";
    }
}
