package com.example.ojackkyoserver.exceptions;

public class MalFormedResourceException extends Error400{
    public MalFormedResourceException(){
        super("Malformed Resource");
    }
}
