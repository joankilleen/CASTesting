/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dto;

import java.io.Serializable;

/**
 *
 * @author Joan
 */
public class LoginDTO implements Serializable {

    private String userName;
    private String password;  

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } 
    @Override 
    public String toString(){
        return this.userName + " " + this.password;
    }
}
