package com.crevan.restvoting.errors;

import lombok.Getter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AppException extends ResponseStatusException {

    private final ErrorAttributeOptions options;

    public AppException(final HttpStatus status, final String message, final ErrorAttributeOptions options) {
        super(status, message);
        this.options = options;
    }
}
