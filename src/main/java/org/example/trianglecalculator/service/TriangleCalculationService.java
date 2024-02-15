package org.example.trianglecalculator.service;

import org.example.trianglecalculator.domain.TriangleAngleType;
import org.example.trianglecalculator.domain.TriangleSideType;
import org.example.trianglecalculator.dto.TriangleData;
import org.example.trianglecalculator.dto.TriangleMedianData;
import org.springframework.stereotype.Service;

@Service
public class TriangleCalculationService {

    public double calculateArea(double sideAB, double sideBC, double sideAC) {
        double s = (sideAB + sideBC + sideAC) / 2.0;
        return Math.sqrt(s * (s - sideAB) * (s - sideBC) * (s - sideAC));
    }

    public double calculatePerimeter(TriangleData triangle) {
        return triangle.getSideAB() + triangle.getSideBC() + triangle.getSideAC();
    }

    public TriangleSideType defineTriangleSideType(TriangleData triangle) {
        if (triangle.getSideAB() != triangle.getSideBC()
                && triangle.getSideBC() != triangle.getSideAC()
                && triangle.getSideAB() != triangle.getSideAC()) {
            return TriangleSideType.SCALENE;
        } else if (triangle.getSideAB() == triangle.getSideBC()
                && triangle.getSideBC() == triangle.getSideAC()
                && triangle.getSideAB() == triangle.getSideAC()) {
            return TriangleSideType.EQUILATERAL;
        } else {
            return TriangleSideType.ISOSCELES;
        }
    }

    public TriangleAngleType determineTriangleAngleType(TriangleData triangle) {
        if (triangle.getAngleA() == 90 || triangle.getAngleB() == 90 || triangle.getAngleC() == 90) {
            return TriangleAngleType.RIGHT;
        } else if (triangle.getAngleA() > 90 || triangle.getAngleB() > 90 || triangle.getAngleC() > 90) {
            return TriangleAngleType.OBTUSE;
        } else {
            return TriangleAngleType.ACUTE;
        }
    }

    public TriangleMedianData calculateTriangleMedians() {
        return null;
    }

    public double calculateMedian(double side1, double side2, double oppositeSide) {
        return Math.sqrt((2 * Math.pow(side1, 2) + 2 * Math.pow(side2, 2) - Math.pow(oppositeSide, 2)) / 4);
    }


}
