package ru.edme.exception;

public class EmptyResponseException extends RetryableRemoteServiceException {
    public EmptyResponseException(String message) { super(message); }
    public EmptyResponseException(String message, Throwable cause) { super(message, cause); }
}