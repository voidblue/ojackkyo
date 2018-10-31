package com.example.ojackkyoserver.exceptions;

public class NullTokenException extends Error401 {
    public NullTokenException(){
        super("you did not send me ArticleConverter token");
    }
}
