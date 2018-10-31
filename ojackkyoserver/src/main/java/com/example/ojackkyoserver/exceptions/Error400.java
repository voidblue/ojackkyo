package com.example.ojackkyoserver.exceptions;

public class Error400 extends RuntimeException{
    protected Error400(String msg){
        super(msg);
    }
}
