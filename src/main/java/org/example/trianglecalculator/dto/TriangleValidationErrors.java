package org.example.trianglecalculator.dto;

import java.util.List;

public record TriangleValidationErrors(
        List<String> errors
) {
}
