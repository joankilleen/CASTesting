package org.books.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import org.books.data.entity.OrderItem;


/**
 *
 * @author guthei
 */
public class OrderItemDTO implements Serializable {

    
    protected Integer quantity;
    protected BookInfo book;
    protected BigDecimal price;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BookInfo getBook() {
        return book;
    }

    public void setBook(BookInfo book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public static OrderItemDTO copyEntityToDTO(OrderItem entity){
       OrderItemDTO dto = new OrderItemDTO();
       dto.setBook(new BookInfo(entity.getBook()));
       dto.setPrice(entity.getPrice());
       dto.setQuantity(entity.getQuantity());
       return dto;
    }
    
    @Override
    public String toString(){
        StringBuilder bookString = new StringBuilder();
        bookString.append(book.getIsbn() + " ");
        bookString.append(book.getTitle() + " ");
        return bookString + " " + this.price + " " + this.quantity;
    }

}
