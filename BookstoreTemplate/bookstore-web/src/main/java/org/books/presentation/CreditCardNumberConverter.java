/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Joan
 */
@FacesConverter("org.books.presentation.CreditCardNumberConverter")
public class CreditCardNumberConverter implements Converter{
    
    @Override
    public Object getAsObject(
            FacesContext ctxt, UIComponent c, String value) {
        StringBuilder bld = new StringBuilder("");
        for (int i=0; i < value.length(); i++){
            if (value.charAt(i)!= ' ')
                bld.append(value.charAt(i));
        }
        return bld.toString();
    }
    @Override 
    public String getAsString(FacesContext ctxt, UIComponent cmpt, Object value) throws ConverterException{
       String input = (String) value;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
           if (i % 4 == 0 && i != 0) {
              result.append(" ");
            }

           result.append(input.charAt(i));
        }
        return result.toString();
    }
    
}
