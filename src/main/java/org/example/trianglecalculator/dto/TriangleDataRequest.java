package org.example.trianglecalculator.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TriangleDataRequest {

    private static final String SIDE_ERROR_MESSAGE = "Нужно указать длину стороны треугольника, ее значение должно быть больше 0";
    private static final String ANGLE_ERROR_MESSAGE = "Нужно указать величину угла треугольника, его значение должно быть больше 0";

    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideAB;
    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideBC;
    @Positive(message = SIDE_ERROR_MESSAGE)
    double sideAC;
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleA;
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleB;
    @Positive(message = ANGLE_ERROR_MESSAGE)
    double angleC;
}
