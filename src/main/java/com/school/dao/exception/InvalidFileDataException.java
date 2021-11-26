package com.school.dao.exception;

public class InvalidFileDataException extends RuntimeException {
    public InvalidFileDataException(final String message) {
        super(message);
    }
}

