package com.glycemic.core.handler;

import com.glycemic.core.util.Error;
import com.glycemic.core.util.ErrorHandleType;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Error> constraintViolationHandler(ConstraintViolationException exception) {
        String errorMessage = new ArrayList<>(exception.getConstraintViolations()).getFirst().getMessage();
        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.CONSTRAINT_VIOLATION, errorMessage, exception.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {ClassCastException.class})
    public ResponseEntity<Error> classCastHandler(ClassCastException exception) {
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorHandleType.CLASS_CAST_EXCEPTION, "Some server error occurred.", exception.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(value = {SerializationException.class})
    public ResponseEntity<Error> serializationHandler(ClassCastException exception) {
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorHandleType.SERIALIZATION_EXCEPTION, "Some server error occurred when getting response.", exception.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Error> sqlIntegrityConstraintViolationHandler(SQLIntegrityConstraintViolationException exception) {
        String errorMessage = "Check your values. You entered duplicated value on unique column.";
        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.DATA_INTEGRITY_CONSTRAINT_VIOLATION, errorMessage, exception.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
    public ResponseEntity<Error> invalidDataAccessApiUsageHandler(InvalidDataAccessApiUsageException exception) {
        String errorMessage = "You entered wrong nested values.";
        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.INVALID_DATA_ACCESS_API_USAGE, errorMessage, "Check your object key and values.", LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<Error> sqlHandler(SQLException exception) {
        String errorMessage = "You entered wrong or invalid parameters values.";
        Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.SQL_EXCEPTION, errorMessage, exception.getLocalizedMessage(), LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

        return ResponseEntity.badRequest().body(error);
    }
}
