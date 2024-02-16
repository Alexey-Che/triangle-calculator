package org.example.trianglecalculator.exception;

public class QuietException extends RuntimeException {

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
