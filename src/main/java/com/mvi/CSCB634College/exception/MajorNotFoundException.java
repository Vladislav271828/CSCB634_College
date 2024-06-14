package com.mvi.CSCB634College.exception;

public class MajorNotFoundException extends RuntimeException{
    public MajorNotFoundException() {
    }

    public MajorNotFoundException(String message) {
        super(message);
    }

    public MajorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MajorNotFoundException(Throwable cause) {
        super(cause);
    }

    public MajorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
