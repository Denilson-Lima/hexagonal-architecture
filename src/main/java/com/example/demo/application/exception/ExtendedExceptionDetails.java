package com.example.demo.application.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ExtendedExceptionDetails extends ExceptionsDetails {
    public List<FieldMessage> errors;
}
