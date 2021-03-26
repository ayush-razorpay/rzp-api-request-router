package com.rzp.apirouter.exception;

public class ApiRouterException extends Exception {

    public ApiRouterException() {
    }

    public ApiRouterException(String message) {
        super(message);
    }

    public ApiRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRouterException(Throwable cause) {
        super(cause);
    }

    public ApiRouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
