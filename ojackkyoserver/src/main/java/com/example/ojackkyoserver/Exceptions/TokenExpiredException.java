package com.example.ojackkyoserver.Exceptions;

public class TokenExpiredException extends Throwable {
    public TokenExpiredException(){
        super("token is expired");
    }
}
