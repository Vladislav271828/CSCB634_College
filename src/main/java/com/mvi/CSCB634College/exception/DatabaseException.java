package com.mvi.CSCB634College.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
