/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates  
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.books.data.dto.BookInfo;

/**
 *
 * @author guthei
 */

@Named("shoppingCart")
@SessionScoped
public class ShoppingCart implements Serializable {

    private static final Logger logger = Logger.getLogger(ShoppingCart.class.getName());
    private final List<CartItem> items = new ArrayList<CartItem>();

    public String getTotalPrice() {
        double totalPriceCalc = 0;
        for (CartItem item : items) {
            double nextPrice = item.getBook().getPrice().doubleValue();
            totalPriceCalc += item.getQuantity() * nextPrice;
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(totalPriceCalc);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clearShoppingCart() {
        items.clear();
    }

    public void addItem(BookInfo bookInfo) {
        logger.info("item being added " + bookInfo.getIsbn());
        CartItem itemFound = null;
        for (CartItem item : items) {
            if (item.getBook().getIsbn().equals(bookInfo.getIsbn())) {
                itemFound = item;
                break;
            }
        }
        if (itemFound != null) {

            itemFound.setQuantity(itemFound.getQuantity() + 1);
            logger.info("quantity being increased " + itemFound.getBook().getIsbn() + " " + itemFound.getQuantity());
        } else {
            CartItem newCartItem = new CartItem(bookInfo);
            items.add(newCartItem);
        }
    }

    public void deleteItem(CartItem item) {
        items.remove(item);
        logger.info("new item size " + items.size());
    }

    public int getSize() {
        int totalQuantity = 0;
        for (CartItem item : items) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
}
