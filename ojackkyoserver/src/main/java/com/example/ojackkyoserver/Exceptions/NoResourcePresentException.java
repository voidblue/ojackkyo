package com.example.ojackkyoserver.Exceptions;

public class NoResourcePresentException extends Throwable {
    public NoResourcePresentException() {
        super("target resource(s) is not present");
    }
}
