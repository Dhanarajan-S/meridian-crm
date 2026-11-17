
package com.meridian.backend.common;

import com.meridian.backend.common.dto.ApiErrorResponse;
import com.meridian.backend.common.exception.InvalidRequestException;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NoSuchElementException.class)
        public ResponseEntity<ApiErrorResponse> handleNotFound(
                        NoSuchElementException exception,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                "Not Found",
                                exception.getMessage(),
                                request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationErrors(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {

                String message = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .distinct()
                                .collect(Collectors.joining("; "));

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation Failed",
                                message,
                                request);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiErrorResponse> handleConflict(
                        IllegalArgumentException exception,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "Conflict",
                                exception.getMessage(),
                                request);
        }

        @ExceptionHandler(InvalidRequestException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidRequest(
                        InvalidRequestException exception,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Bad Request",
                                exception.getMessage(),
                                request);
        }

        private ResponseEntity<ApiErrorResponse> buildErrorResponse(
                        HttpStatus status,
                        String error,
                        String message,
                        HttpServletRequest request) {

                ApiErrorResponse response = new ApiErrorResponse(
                                LocalDateTime.now(),
                                status.value(),
                                error,
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(status).body(response);
        }
}
