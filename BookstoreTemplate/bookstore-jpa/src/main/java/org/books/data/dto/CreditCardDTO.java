package org.books.data.dto;

import java.io.Serializable;
import org.books.data.CreditCardType;
import org.books.data.entity.CreditCard;

public class CreditCardDTO implements Serializable {

/*    public enum Type {

        MasterCard, Visa
    } */
    private CreditCardType type;
    private String number;
    private Integer expirationMonth;
    private Integer expirationYear;

    public CreditCardDTO() {
    }

    public CreditCardDTO(CreditCardType type, String number, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CreditCardDTO(CreditCardDTO other) {
        this.type = other.type;
        this.number = other.number;
        this.expirationMonth = other.expirationMonth;
        this.expirationYear = other.expirationYear;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    @Override
    public String toString() {
        return this.number + " " + this.type + " " + this.expirationMonth + " " + this.expirationYear;
    }
    public static CreditCardDTO copyEntityToDTO(CreditCard entity){
        CreditCardDTO dto = new CreditCardDTO((CreditCardType)entity.getType(),
                                                    entity.getNumber(),
                                                    entity.getExpirationMonth(),
                                                    entity.getExpirationYear());
        return dto;
    }
}
