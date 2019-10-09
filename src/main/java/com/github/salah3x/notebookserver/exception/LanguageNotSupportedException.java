package com.github.salah3x.notebookserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LanguageNotSupportedException extends RuntimeException {

    public LanguageNotSupportedException(String message) {
        super(message);
    }
}
