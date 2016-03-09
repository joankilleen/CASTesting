/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.data.CreditCardType;
import org.books.data.dto.CreditCardDTO;
import org.books.data.entity.CreditCard;
import org.books.util.MessageFactory;

/**
 *
 * @author Joan
 */
@FacesValidator(CreditCardNumberValidator.VALIDATOR_ID)
public class CreditCardNumberValidator implements Validator, StateHolder {
    public static final String VALIDATOR_ID="org.books.presentation.CreditCardNumberValidator";
    private final String PATTERNCARD = "(\\d{16})";
    private final String INVALID_CREDITCARD_FORMAT = "org.books.presentation.INVALID_CREDITCARD_FORMAT";
    private String cardTypeId = null;
    private boolean transientValue = false;
    private static final Logger LOG = Logger.getLogger(CreditCardNumberValidator.class.getName());
    
    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    @Override
    public Object saveState(FacesContext context) {
        return cardTypeId;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        cardTypeId = (String)state;
    }

    @Override
    public boolean isTransient() {
        return transientValue;
    }
    @Override 
    public void setTransient(boolean transientValue){
        this.transientValue = transientValue;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        LOG.info("credit card validator");
        String input = (String) value;
        if (!input.matches(PATTERNCARD)) {
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, INVALID_CREDITCARD_FORMAT);
            throw new ValidatorException(message);
        }
        UIInput ccType = (UIInput)component.findComponent(cardTypeId);
        CreditCardType type = (CreditCardType)ccType.getValue();
        String inputValue = (String) value;
        char firstChar = inputValue.charAt(0);
        char secondChar = inputValue.charAt(1);
        
        if (type.equals(CreditCardType.MasterCard)){
            // check that value if 51, 52, 53, 54
            if (firstChar=='5'){
                if (secondChar=='1' || secondChar=='2' || secondChar=='3' || secondChar=='4'  )
                    return;
            }        
        }
        if (type.equals(CreditCardType.Visa)){
            // check that value if 51, 52, 53, 54
            if (firstChar=='4'){
                    return;
            }        
        }
        FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, INVALID_CREDITCARD_FORMAT);
            throw new ValidatorException(message);
        
        
    }
    
}
