package com.example.ojackkyoserver.exceptions;

public class InvalidLoginException extends Error401 {
    public InvalidLoginException(){
        super("id and password was not matched");
    }
}
