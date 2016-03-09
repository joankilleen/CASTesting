/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.util.MessageFactory;

/**
 *
 * @author Joan
 */
@FacesValidator(EmailValidator.VALIDATOR_ID)
public class EmailValidator implements Validator{
    public static final String VALIDATOR_ID="org.books.presentation.EmailValidator";
    public static final String INVALID_EMAIL_FORMAT_ID="org.books.presentation.EmailValidator.INVALID_EMAIL_FORMAT";
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        String input = (String) value;
        boolean valid = false;
        
        if (input.contains("@") && input.contains(".")){
            int posAt = input.indexOf('@');
            int posDot = input.lastIndexOf('.');
            if (posDot > posAt){
                valid=true;
            }
        }      
        if (!valid){
            FacesMessage message = MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, INVALID_EMAIL_FORMAT_ID);
            throw new ValidatorException(message);
        }
    }
}
