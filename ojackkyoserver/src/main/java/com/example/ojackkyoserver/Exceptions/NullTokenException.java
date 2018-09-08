package com.example.ojackkyoserver.Exceptions;

public class NullTokenException extends Throwable {
    public NullTokenException(){
        super("you did not send me a token");
    }
}
