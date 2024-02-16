package org.example.trianglecalculator.controller;

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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TriangleCalculationController {

    TriangleComputeService triangleComputeService;

    @PostMapping("/info")
    public ResponseEntity<?> getTriangleInfo(@RequestBody @Valid TriangleDataRequest request) {
        return ResponseEntity.ok().body(triangleComputeService.getTriangleInfo(request));
    }

    @ExceptionHandler(TriangleValidateException.class)
    public ResponseEntity<?> handleTriangleValidateException(TriangleValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TriangleValidationErrors(e.getErrors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
