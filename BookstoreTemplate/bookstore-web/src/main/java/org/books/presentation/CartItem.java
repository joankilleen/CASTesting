/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import org.books.data.dto.BookInfo;

/**
 *
 * @author guthei
 */
public class CartItem implements Serializable{
    
    private BookInfo book;
    private int quantity;

    CartItem(BookInfo book) {
        quantity = 1;
        this.book = book;
    }

    public BookInfo getBook() {
        return book;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getPrice(){
        
        double itemPrice = (this.book.getPrice().doubleValue())*quantity;
       
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(itemPrice);
    }
    
    
}
