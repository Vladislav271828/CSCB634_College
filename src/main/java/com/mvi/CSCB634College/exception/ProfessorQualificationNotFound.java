package com.mvi.CSCB634College.exception;

public class ProfessorQualificationNotFound extends RuntimeException {
    public ProfessorQualificationNotFound() {
    }

    public ProfessorQualificationNotFound(String message) {
        super(message);
    }

    public ProfessorQualificationNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfessorQualificationNotFound(Throwable cause) {
        super(cause);
    }

    public ProfessorQualificationNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
