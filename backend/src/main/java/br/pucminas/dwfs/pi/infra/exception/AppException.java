package br.pucminas.dwfs.pi.infra.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_HTTP_STATUS = 500;
    private static final String DEFAULT_MESSAGE = "An error occured";

    private int httpStatus;

    public AppException() {
        this(DEFAULT_HTTP_STATUS, DEFAULT_MESSAGE, null);
    }

    public AppException(int httpStatus) {
        this(httpStatus, DEFAULT_MESSAGE, null);
    }

    public AppException(String message) {
        this(DEFAULT_HTTP_STATUS, message, null);
    }

    public AppException(Throwable cause) {
        this(DEFAULT_HTTP_STATUS, DEFAULT_MESSAGE, cause);
    }

    public AppException(int httpStatus, String message) {
        this(httpStatus, message, null);
    }

    public AppException(int httpStatus, Throwable cause) {
        this(httpStatus, DEFAULT_MESSAGE, cause);
    }

    public AppException(String message, Throwable cause) {
        this(DEFAULT_HTTP_STATUS, message, cause);
    }

    public AppException(int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}