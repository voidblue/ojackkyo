package com.example.ojackkyoserver.exceptions;

public class NoResourcePresentException extends Error403 {
    public NoResourcePresentException() {
        super("target resource(s) is not present");
    }
}
