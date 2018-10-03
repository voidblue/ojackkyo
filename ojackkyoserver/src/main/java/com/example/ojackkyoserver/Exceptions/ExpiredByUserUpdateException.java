package com.example.ojackkyoserver.Exceptions;

import io.jsonwebtoken.JwtException;

public class ExpiredByUserUpdateException extends JwtException {
    public ExpiredByUserUpdateException(){
        super("this token is expired because target user was updated");
    }
}
