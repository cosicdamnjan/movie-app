package com.example.movieapp.service.impl;

import com.example.movieapp.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    @Override
    public <T> void validate(T entity) {

        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            for (ConstraintViolation violation : violations) {
                log.warn(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }
    }
}
