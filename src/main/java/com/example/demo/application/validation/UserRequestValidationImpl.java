package com.example.demo.application.validation;

import com.example.demo.application.dto.request.UserRequest;
import com.example.demo.application.exception.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRequestValidationImpl implements ConstraintValidator<UserRequestValidation, UserRequest> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final List<FieldMessage> fieldMessages = new ArrayList<>();

    @Override
    public void initialize(UserRequestValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRequest userRequest, ConstraintValidatorContext constraintValidatorContext) {
        validateDateOfBirth(userRequest);

        return processValidationResults(constraintValidatorContext);
    }

    private void validateDateOfBirth(UserRequest userRequest) {
        try {
            LocalDate.parse(userRequest.getDateOfBirth(), formatter);
        } catch (Exception e) {
            log.error("Failed to process dateOfBirth: " + userRequest.getDateOfBirth());
            var fieldMessage = FieldMessage.builder()
                    .fieldName("dateOfBirth")
                    .message("Invalid Date! Expected Format: dd/mm/yyyy")
                    .build();
            this.fieldMessages.add(fieldMessage);
        }
    }

    private boolean processValidationResults(ConstraintValidatorContext context) {
        for (FieldMessage e : fieldMessages) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return fieldMessages.isEmpty();
    }
}
