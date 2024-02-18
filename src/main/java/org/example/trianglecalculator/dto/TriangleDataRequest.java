package org.example.trianglecalculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TriangleDataRequest {

    private static final String SIDE_ERROR_MESSAGE = "Нужно указать длину стороны треугольника, ее значение должно быть больше 0";
    private static final String ANGLE_ERROR_MESSAGE = "Нужно указать величину угла треугольника, его значение должно быть больше 0";

    @Schema(name = "sideAB", example = "4", description = "Длина отрезка AB")
    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideAB;

    @Schema(name = "sideBC", example = "5", description = "Длина отрезка BC")
    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideBC;

    @Schema(name = "sideAC", example = "3", description = "Длина отрезка AС")
    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideAC;

    @Schema(name = "angleA", example = "90", description = "Величина угла A в градусах")
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleA;

    @Schema(name = "angleB", example = "53.13", description = "Величина угла B в градусах")
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleB;

    @Schema(name = "angleC", example = "36.87", description = "Величина угла C в градусах")
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleC;
}
