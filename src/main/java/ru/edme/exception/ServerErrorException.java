package ru.edme.exception;


public class ServerErrorException extends RetryableRemoteServiceException {
    public ServerErrorException(String message) { super(message); }
    public ServerErrorException(String message, Throwable cause) { super(message, cause); }
}