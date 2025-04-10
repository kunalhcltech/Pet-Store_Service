package com.hcltech.exceptions;

public class TagNotFoundException extends RuntimeException{
    public TagNotFoundException(String message)
    {
        super(message);
    }
}
