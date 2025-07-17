package ru.edme.exception;

public class ClientErrorException extends RemoteServiceException {
    public ClientErrorException(String message) { super(message); }
    public ClientErrorException(String message, Throwable cause) { super(message, cause); }
}