package com.example.ojackkyoserver.exceptions;

public class Error404 extends RuntimeException{
    protected Error404(String msg){
        super(msg);
    }
}
