package com.example.ojackkyoserver.Exceptions;

public class NoPermissionException extends Throwable {
    public NoPermissionException(){
        super("You do not have permission");
    }
}
