package com.creditsuisse.exception;

public class ProcessingException extends ApplicationException {

    String userMessage = "";

    @Override
    public String getUserMessage() {
        return this.userMessage;
    }

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingException(Throwable cause) {
        super(cause);
    }
}
