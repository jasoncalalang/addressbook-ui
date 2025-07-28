package com.ibm.test.addressbook.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("testBean")
@RequestScoped
public class TestBean implements Serializable {
    
    private String message = "Hello, Jakarta Faces!";
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}