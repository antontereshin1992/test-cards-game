package com.cardgames.demo.exceprion;

public class NotFoundException extends BusinessRuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
