package com.glycemic.core.handler;

import com.glycemic.core.util.Error;
import com.glycemic.core.util.ErrorHandleType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        String message = ex.getAllErrors().getFirst().getDefaultMessage();

        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.ARGUMENT_NOT_VALID, message, ex.getObjectName(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Error error = new Error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorHandleType.MEDIA_TYPE_NOT_SUPPORTED, "Only supported content type is application/json", ex.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Error error = new Error(HttpStatus.METHOD_NOT_ALLOWED, ErrorHandleType.METHOD_NOT_SUPPORTED, "The method used is not valid for this request.", "Use one of the GET, POST, DELETE, PUT methods.", LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.MESSAGE_NOT_READABLE, "Your data is incorrect.", "Check your parameter(s),value(s) or syntax.", LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(@NonNull ErrorResponseException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorHandleType.SERVER_ERROR, "Your data is incorrect.", "Check your parameter(s),value(s) or syntax.", LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
