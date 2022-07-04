package com.vikhani.animventory.exceptions;

import org.springframework.security.authentication.LockedException;

public class UserLockedException extends LockedException {
    public UserLockedException(String message) {
        super(message);
    }

    public UserLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
