package ru.edme.exception;

public class RetryableRemoteServiceException extends RemoteServiceException {
    public RetryableRemoteServiceException(String message) { super(message); }
    public RetryableRemoteServiceException(String message, Throwable cause) { super(message, cause); }
}