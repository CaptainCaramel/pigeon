package com.ldal.pigeonapp.back;

public class NegativeIDException extends RuntimeException
{
    public NegativeIDException(String m) {
        super(m);
    }
}
