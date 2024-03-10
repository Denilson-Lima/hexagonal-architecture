package com.example.demo.application.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class ExceptionsDetails {
    protected HttpStatus type;
    protected String title;
    protected String detail;
    protected String instance;
    protected String timestamp;
}
