package io.academia.course.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        CustomErrorDetails customErrorDetails = new CustomErrorDetails(
                new Date(), "From MethodArgumentNotValid Exception", ex.getMessage(), false, status.toString()
        );

        return new ResponseEntity<>(customErrorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        CustomErrorDetails customErrorDetails = new CustomErrorDetails(
                new Date(), "From HttpRequestMethodNotSupported Exception", ex.getMessage(), false, status.toString()
        );

        return new ResponseEntity<>(customErrorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {

        CustomErrorDetails customErrorDetails = new CustomErrorDetails(
                new Date(), "From HttpRequestMethodNotSupported Exception", ex.getMessage(), false, HttpStatus.NOT_FOUND.toString()
        );

        return new ResponseEntity<>(customErrorDetails, HttpStatus.NOT_FOUND);
    }

}
