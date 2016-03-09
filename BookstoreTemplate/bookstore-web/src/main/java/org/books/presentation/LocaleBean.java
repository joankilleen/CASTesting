/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author Joan
 */
@Named("localeBean")
@SessionScoped
public class LocaleBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(LocaleBean.class.getName());
    
    private Locale locale = null;
    private List<SelectItem> countries = new ArrayList<>(); //country display name, country code

    
    public List<SelectItem> getCountries() {
        
        LOG.info("getting countries");
        if (countries.isEmpty()){
            loadCountries();
        }
        return countries;
    }

    public Locale getLocale() {
        if (locale != null) {
            return locale;
        }
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public String changeLocale(String strLocale) {
        if (strLocale.equals("de")) {
            locale = new Locale("de", "CH");
        }
        if (strLocale.equals("en")) {
            locale = new Locale("en", "US");
        }
        if (locale != null) {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }
        // load the country names for the selected locale
        loadCountries();
        return null;
    }

    private void loadCountries() {       
        String[] arrayCountryCodes = Locale.getISOCountries();
        for (int i=0; i < arrayCountryCodes.length; i++) {
            
                Locale nextLocale = new Locale("", arrayCountryCodes[i]);
                SelectItem nextItem = new SelectItem(arrayCountryCodes[i], nextLocale.getDisplayCountry(getLocale()));
                countries.add(nextItem);
	}
        
    }
    

}
