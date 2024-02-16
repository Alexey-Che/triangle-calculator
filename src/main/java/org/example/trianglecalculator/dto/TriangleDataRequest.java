package org.example.trianglecalculator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TriangleDataRequest {

    @Positive
    @NotNull
    double sideAB;
    @Positive
    @NotNull
    double sideBC;
    @Positive
    @NotNull
    double sideAC;
    @Positive
    @NotNull
    double angleA;
    @Positive
    @NotNull
    double angleB;
    @Positive
    @NotNull
    double angleC;
}
