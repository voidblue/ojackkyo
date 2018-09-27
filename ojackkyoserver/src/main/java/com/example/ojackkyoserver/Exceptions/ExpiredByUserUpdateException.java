package com.example.ojackkyoserver.Exceptions;

import io.jsonwebtoken.JwtException;

public class ExpiredByUserUpdateException extends JwtException {
    public ExpiredByUserUpdateException(){
        super("token is expired");
    }
}
