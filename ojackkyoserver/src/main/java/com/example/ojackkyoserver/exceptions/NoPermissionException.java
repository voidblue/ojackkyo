package com.example.ojackkyoserver.exceptions;

public class NoPermissionException extends Error403 {
    public NoPermissionException(){
        super("You do not have permission");
    }
}
