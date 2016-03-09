/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import org.books.data.entity.BookEntity;
import org.books.data.Binding;

/**
 *
 * @author Joan
 */
public class BookDTO implements Serializable {     
    private String isbn;
    private String title;
    private String authors;
    private String publisher;
    private Integer publicationYear;
    private Binding binding;

    private Integer numberOfPages;
    private BigDecimal price;

    public BookDTO() {
    }

    public BookDTO(String isbn, String title, String authors, String publisher,
            Integer publicationYear, Binding binding, Integer numberOfPages, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.binding = binding;
        this.numberOfPages = numberOfPages;
        this.price = price;
    }

    
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @Override
    public String toString(){
        return this.isbn + " " + this.title + " " + this.authors + " " + this.publisher + this.price;
    }
    public static BookDTO copyEntitytoDTO(BookEntity entity){
        return new BookDTO(entity.getIsbn(), entity.getTitle(), entity.getAuthors(), 
                        entity.getPublisher(), entity.getPublicationYear(), 
                        entity.getBinding(), entity.getNumberOfPages(), entity.getPrice());
    }
}

