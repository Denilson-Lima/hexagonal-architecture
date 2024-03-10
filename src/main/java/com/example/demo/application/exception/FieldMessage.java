package com.example.demo.application.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldMessage {
    private String fieldName;
    private String message;
}
