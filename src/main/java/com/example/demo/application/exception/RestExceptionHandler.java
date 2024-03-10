package com.example.demo.application.exception;

import com.example.demo.domain.exception.UserAlreadyExistsException;
import com.example.demo.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    final Map<Class<? extends RuntimeException>, HttpStatus> exceptionMap = new HashMap<>();

    public RestExceptionHandler() {
        super();
        exceptionMap.put(EmptyStackException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionMap.put(UserAlreadyExistsException.class, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatorExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String fileNames = ex.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(", ", "[", "]"));
        var response = ExtendedExceptionDetails.builder()
                .type(HttpStatus.BAD_REQUEST)
                .title(ex.getClass().getSimpleName())
                .detail(String.join(" ", "Fields", fileNames, "violated request data integrity"))
                .instance(request.getRequestURI())
                .timestamp(formatter.format(Instant.now()))
                .build();

        List<FieldMessage> errors = new ArrayList<>();

        for (FieldError error : ex.getFieldErrors()) {
            var fieldMessage = FieldMessage.builder()
                    .fieldName(error.getField())
                    .message(error.getDefaultMessage())
                    .build();
            errors.add(fieldMessage);
        }

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleExceptions(RuntimeException ex, HttpServletRequest request) {
        var httpStatus = httpStatusCodeForException(ex);
        return new ResponseEntity<>(BasicExceptionDetails.builder()
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
