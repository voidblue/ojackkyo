package com.example.ojackkyoserver.Exceptions;

import io.jsonwebtoken.JwtException;

public class NullTokenException extends JwtException {
    public NullTokenException(){
        super("you did not send me a token");
    }
}
