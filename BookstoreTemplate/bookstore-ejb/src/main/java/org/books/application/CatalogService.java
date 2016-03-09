package org.books.application;

import java.util.List;
import javax.ejb.Remote;
import javax.interceptor.Interceptors;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.util.Tracer;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

@Remote

public interface CatalogService {

        /**
         * Adds a book to the catalog.
         * @param book the new book
         * @throws BookAlreadyExistsException if a book with the same ISBN number already exists
         * @throws IllegalArgumentException if isbn, title, authors, publication year, binding number of pages or price are null
         * @throws IllegalArgumentException if isbn, title, authors, publication year, binding number of pages or price are empty
         */
	public BookDTO addBook(BookDTO book) throws BookAlreadyExistsException;
        
        /**
         * Finds a book by ISBN number.
         * @param isbn the ISBN number to look for
         * @return the data of the found book
         * @throws BookNotFoundException if no book with the specified ISBN number exists
         */
	public BookDTO findBook(String isbn) throws BookNotFoundException;
        
        /**
         * Searches for books by keywords.
         * A book is included in the result list if every keyword is contained in its title, authors or publisher field.
         * @param keywords the keywords to search for
         * @return a list of matching books (may be empty) 
         */
        public List<BookInfo> searchBooks(String keywords);
        
        /**
         * Updates the data of a book.
         * @param book the data of the book to be updated
         * @throws BookNotFoundException if the book is not contained in the catalog
         * @throws IllegalArgumentException if isbn, title, authors, publication year, binding number of pages or price are null
         * @throws IllegalArgumentException if isbn, title, authors, publication year, binding number of pages or price are empty
         */
        public void updateBook(BookDTO book) throws BookNotFoundException; 
        
}
