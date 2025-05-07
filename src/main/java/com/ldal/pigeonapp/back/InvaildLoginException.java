package com.ldal.pigeonapp.back;

public class InvaildLoginException extends RuntimeException
{
    public InvaildLoginException(String message) {
        super(message);
    }
}
