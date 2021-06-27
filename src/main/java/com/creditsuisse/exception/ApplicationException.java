package com.creditsuisse.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ApplicationException extends RuntimeException {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationException.class);

    public abstract String getUserMessage();

    public ApplicationException(String message) {
        super(message);
        LOG.error(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        LOG.error(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
        LOG.error(cause.getMessage(), cause);
    }
}
