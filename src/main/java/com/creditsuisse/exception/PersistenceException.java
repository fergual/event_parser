package com.creditsuisse.exception;

public class PersistenceException extends ApplicationException {

    String userMessage = "";

    @Override
    public String getUserMessage() {
        return this.userMessage;
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }


}
