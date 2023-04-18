package com.crevan.restvoting.web.error;

import com.crevan.restvoting.error.AppException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes attributes;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> appException(final AppException ex, final WebRequest request) {
        log.error("Application Exception", ex);
        Map<String, Object> body = attributes.getErrorAttributes(request, ex.getOptions());
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
                                                             final HttpHeaders headers, final HttpStatusCode statusCode,
                                                             final WebRequest request) {
        log.error("Exception", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
