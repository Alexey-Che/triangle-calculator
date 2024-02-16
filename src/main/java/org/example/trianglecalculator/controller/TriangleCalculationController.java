package org.example.trianglecalculator.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.example.trianglecalculator.dto.TriangleValidationErrors;
import org.example.trianglecalculator.exception.TriangleValidateException;
import org.example.trianglecalculator.service.TriangleComputeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController("/triangle")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TriangleCalculationController {

    TriangleComputeService triangleComputeService;

    @GetMapping("/info")
    public ResponseEntity<?> getTriangleInfo(@Valid TriangleDataRequest request) {
        return ResponseEntity.ok().body(triangleComputeService.getTriangleInfo(request));
    }

    @ExceptionHandler(TriangleValidateException.class)
    public ResponseEntity<?> handleTriangleValidateException(TriangleValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TriangleValidationErrors(e.getErrors()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<?, ?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Map.of("message", "unknown path params");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<?, ?> handleConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .collect(Collectors.toMap(ConstraintViolation::getPropertyPath,
                        v -> Map.of("message", v.getMessage(), "value", String.valueOf(v.getInvalidValue()))));
    }
}
