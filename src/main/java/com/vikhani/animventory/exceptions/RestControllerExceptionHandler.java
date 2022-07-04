package com.vikhani.animventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class RestControllerExceptionHandler {
    private static final String ERROR_CODE_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String EXCEPTION_KEY = "exception";
    private static final String TIMESTAMP_KEY = "timestamp";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object processValidationError(NotFoundException ex) {
        return generateExceptionRepresentation(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(NameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Object processLookupError(NameExistsException ex) {
        return generateExceptionRepresentation(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(UserLockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    @ResponseBody
    public Object processLockError(UserLockedException ex) {
        return generateExceptionRepresentation(HttpStatus.LOCKED, ex);
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object processLockError(InvalidInputException ex) {
        return generateExceptionRepresentation(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processLoginError(AuthenticationException ex) {
        return generateExceptionRepresentation(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processAccessDeniedError(AccessDeniedException ex) {
        return generateExceptionRepresentation(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processUnauthorisedError(HttpClientErrorException.Unauthorized ex) {
        return generateExceptionRepresentation(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object processAccessDeniedError(HttpClientErrorException.Forbidden ex) {
        return generateExceptionRepresentation(HttpStatus.FORBIDDEN, ex);
    }

    private Map<String, String> generateExceptionRepresentation(HttpStatus status, Exception ex) {
        Map<String, String> exRepresentation = new LinkedHashMap();
        exRepresentation.put(TIMESTAMP_KEY, new Date().toString());
        exRepresentation.put(ERROR_CODE_KEY, String.valueOf(status.value()));
        exRepresentation.put(EXCEPTION_KEY, ex.getClass().getCanonicalName());
        exRepresentation.put(MESSAGE_KEY, ex.getMessage());

        return exRepresentation;
    }
}

