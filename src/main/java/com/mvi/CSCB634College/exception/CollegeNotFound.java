package com.mvi.CSCB634College.exception;

public class CollegeNotFound extends RuntimeException {

    public CollegeNotFound() {
    }

    public CollegeNotFound(String message) {
        super(message);
    }

    public CollegeNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public CollegeNotFound(Throwable cause) {
        super(cause);
    }

    public CollegeNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
