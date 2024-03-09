package com.example.demo.application.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class RestError {
    public HttpStatus type;
    public String title;
    public String detail;
    public String instance;
    public String timestamp;
}