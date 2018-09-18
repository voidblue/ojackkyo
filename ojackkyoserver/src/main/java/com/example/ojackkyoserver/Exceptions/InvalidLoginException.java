package com.example.ojackkyoserver.Exceptions;

public class InvalidLoginException extends Throwable {
    public InvalidLoginException(){
        super("id and password was not matched");
    }
}
