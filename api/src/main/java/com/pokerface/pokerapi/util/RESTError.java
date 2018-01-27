package com.pokerface.pokerapi.util;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class RESTError extends Throwable {
    private HttpStatus statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    public RESTError() {
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        timestamp = LocalDateTime.now();
    }

    public RESTError(HttpStatus status) {
        this();
        this.statusCode = status;
    }

    public RESTError(String message) {
        this();
        this.message = message;
    }

    public RESTError(String message, String debugMessage) {
        this(message);
        this.debugMessage = debugMessage;
    }

    public RESTError(HttpStatus status, String message) {
        this(message);
        statusCode = status;
    }

    public RESTError(HttpStatus status, String message, String debugMessage) {
        this(status, message);
        this.debugMessage = debugMessage;
    }
}
