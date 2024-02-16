package org.example.trianglecalculator.exception;

public class QuietException extends RuntimeException {

    public QuietException() {
    }

    public QuietException(String message) {
        super(message);
    }

    public QuietException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuietException(Throwable cause) {
        super(cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
