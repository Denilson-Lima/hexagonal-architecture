package com.example.demo.application.exception;

import com.example.demo.domain.exception.UserAlreadyExistsException;
import com.example.demo.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    final Map<Class<? extends RuntimeException>, HttpStatus> exceptionMap = new HashMap<>();

    public RestExceptionHandler() {
        super();
        exceptionMap.put(EmptyStackException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionMap.put(UserAlreadyExistsException.class, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleExceptions(RuntimeException ex, HttpServletRequest request) {
        var httpStatus = httpStatusCodeForException(ex);
        return new ResponseEntity<>(RestError.builder()
                .type(httpStatus)
                .title(ex.getClass().getSimpleName())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(formatter.format(Instant.now()))
                .build(), httpStatus);
    }

    private HttpStatus httpStatusCodeForException(RuntimeException ex) {
        return exceptionMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
