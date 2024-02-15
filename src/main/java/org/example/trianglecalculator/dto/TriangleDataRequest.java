package org.example.trianglecalculator.dto;

import lombok.Data;

@Data
public class TriangleDataRequest {

    double sideAB;
    double sideBC;
    double sideAC;

    double angleA;
    double angleB;
    double angleC;
}
