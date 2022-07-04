package com.vikhani.animventory.exceptions;

public class NameExistsException extends RuntimeException {
    public NameExistsException() {
        super();
    }

    public NameExistsException(String message) {
        super(message);
    }

    public NameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameExistsException(Throwable cause) {
        super(cause);
    }

    protected NameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
