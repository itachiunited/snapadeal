package com.snapadeal.exceptions;

public class BusinessProfileException extends Exception {

    public String message;

    public BusinessProfileException(String s) {
        this.message = s;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}
