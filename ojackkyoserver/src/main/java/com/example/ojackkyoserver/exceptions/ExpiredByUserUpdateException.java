package com.example.ojackkyoserver.exceptions;

import io.jsonwebtoken.JwtException;

public class ExpiredByUserUpdateException extends Error400 {
    public ExpiredByUserUpdateException(){
        super("this token is expired because target user was updated");
    }
}
