package org.example.trianglecalculator.service;

import lombok.val;
import org.example.trianglecalculator.domain.TriangleAngleType;
import org.example.trianglecalculator.domain.TriangleSideType;
import org.example.trianglecalculator.dto.TriangleBisectorData;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.example.trianglecalculator.dto.TriangleHeightData;
import org.example.trianglecalculator.dto.TriangleMedianData;
import org.springframework.stereotype.Service;

@Service
public class TriangleCalculationService {


    public double computeArea(TriangleDataRequest triangle) {
        double s = (triangle.getSideAB() + triangle.getSideBC() + triangle.getSideAC()) / 2.0;
        return Math.sqrt(s * (s - triangle.getSideAB()) * (s - triangle.getSideBC()) * (s - triangle.getSideAC()));
    }

    public double computePerimeter(TriangleDataRequest triangle) {
        return triangle.getSideAB() + triangle.getSideBC() + triangle.getSideAC();
    }

    public TriangleSideType defineTriangleSideType(TriangleDataRequest triangle) {
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

    public TriangleAngleType determineTriangleAngleType(TriangleDataRequest triangle) {
        if (triangle.getAngleA() == 90 || triangle.getAngleB() == 90 || triangle.getAngleC() == 90) {
            return TriangleAngleType.RIGHT;
        } else if (triangle.getAngleA() > 90 || triangle.getAngleB() > 90 || triangle.getAngleC() > 90) {
            return TriangleAngleType.OBTUSE;
        } else {
            return TriangleAngleType.ACUTE;
        }
    }

    public TriangleMedianData computeTriangleMedians(TriangleDataRequest triangle) {
        val medianA = computeMedian(triangle.getSideAB(), triangle.getSideAC(), triangle.getSideBC());
        val medianB = computeMedian(triangle.getSideAB(), triangle.getSideBC(), triangle.getSideAC());
        val medianC = computeMedian(triangle.getSideAC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleMedianData(medianA, medianB, medianC);
    }

    public double computeMedian(double side1, double side2, double oppositeSide) {
        return Math.sqrt((2 * Math.pow(side1, 2) + 2 * Math.pow(side2, 2) - Math.pow(oppositeSide, 2)) / 4);
    }

    public TriangleBisectorData computeTriangleBisectors(TriangleDataRequest triangle) {
        val bisectorA = computeTriangleBisector(triangle.getAngleA(), triangle.getSideBC(), triangle.getSideAC());
        val bisectorB = computeTriangleBisector(triangle.getAngleB(), triangle.getSideAB(), triangle.getSideAC());
        val bisectorC = computeTriangleBisector(triangle.getAngleC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleBisectorData(bisectorA, bisectorB, bisectorC);
    }

    private double computeTriangleBisector(double angle, double side1, double side2) {
        return (2 * side1 * side2 * Math.cos(Math.toRadians(angle / 2))) / (side1 + side2);
    }

    public TriangleHeightData computeTriangleHeights(TriangleDataRequest triangle) {
        val area = computeArea(triangle);
        val heightA = computeTriangleHeight(area, triangle.getSideBC());
        val heightB = computeTriangleHeight(area, triangle.getSideAC());
        val heightC = computeTriangleHeight(area, triangle.getSideAB());
        return new TriangleHeightData(heightA, heightB, heightC);
    }

    private double computeTriangleHeight(double area, double oppositeSide) {
        return (2 * area) / oppositeSide;
    }
}
