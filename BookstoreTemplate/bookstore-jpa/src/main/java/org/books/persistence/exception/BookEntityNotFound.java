/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.exception;

import javax.ejb.ApplicationException;
import org.books.data.dto.BookInfo;

/**
 *
 * @author Joan
 */
@ApplicationException(rollback=true)
public class BookEntityNotFound extends Exception{
    private BookInfo bookInfo;

public  BookEntityNotFound(BookInfo bookInfo){
    this.bookInfo = bookInfo;
}  
    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
    
}
