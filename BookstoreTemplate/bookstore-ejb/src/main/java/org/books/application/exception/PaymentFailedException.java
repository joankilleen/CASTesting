/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author Joan
 */
@ApplicationException(rollback=true)
public class PaymentFailedException extends Exception {

    public enum Code {

        INVALID_CREDIT_CARD, CREDIT_CARD_EXPIRED, PAYMENT_LIMIT_EXCEEDED;
    }
    private final Code code;

    public PaymentFailedException(Code code) {
        super("credit card error: " + code);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
