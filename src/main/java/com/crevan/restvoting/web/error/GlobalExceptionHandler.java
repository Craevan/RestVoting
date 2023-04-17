package com.crevan.restvoting.web.error;

import com.crevan.restvoting.error.AppException;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes attributes;

    public ResponseEntity<Map<String, Object>> appException(final AppException ex, final WebRequest request) {
        Map<String, Object> body = attributes.getErrorAttributes(request, ex.getOptions());
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }
}
