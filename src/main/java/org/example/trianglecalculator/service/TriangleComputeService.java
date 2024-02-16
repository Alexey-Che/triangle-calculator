package org.example.trianglecalculator.service;

import lombok.val;
import org.example.trianglecalculator.domain.TriangleAngleType;
import org.example.trianglecalculator.domain.TriangleSideType;
import org.example.trianglecalculator.dto.*;
import org.springframework.stereotype.Service;

@Service
public class TriangleComputeService {

    public TriangleDataResponse getTriangleInfo(TriangleDataRequest request) {
        val triangleAngleType = determineTriangleAngleType(request);
        var triangleInfo = TriangleDataResponse.builder()
                .area(computeArea(request))
                .angleType(triangleAngleType);

        if (triangleAngleType == TriangleAngleType.RIGHT) {
            triangleInfo.rightTriangleInfo(computeRightTypeTriangleInfo(request));
        }

        triangleInfo.sideType(defineTriangleSideType(request))
                .medians(computeTriangleMedians(request))
                .bisectors(computeTriangleBisectors(request))
                .heights(computeTriangleHeights(request))
                .inscribedCircle(computeInscribedCircleOfTriangleArea(request))
                .circumscribedCircle(computeCircumscribedCircleOfTriangleArea(request));

        return triangleInfo.build();
    }

    private double computeArea(TriangleDataRequest triangle) {
        double s = computeHalfPerimeter(triangle);
        return Math.sqrt(s * (s - triangle.getSideAB()) * (s - triangle.getSideBC()) * (s - triangle.getSideAC()));
    }

    private double computePerimeter(TriangleDataRequest triangle) {
        return triangle.getSideAB() + triangle.getSideBC() + triangle.getSideAC();
    }

    private double computeHalfPerimeter(TriangleDataRequest triangle) {
        return computePerimeter(triangle) / 2.0;
    }

    private TriangleSideType defineTriangleSideType(TriangleDataRequest triangle) {
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

    private TriangleAngleType determineTriangleAngleType(TriangleDataRequest triangle) {
        if (triangle.getAngleA() == 90 || triangle.getAngleB() == 90 || triangle.getAngleC() == 90) {
            return TriangleAngleType.RIGHT;
        } else if (triangle.getAngleA() > 90 || triangle.getAngleB() > 90 || triangle.getAngleC() > 90) {
            return TriangleAngleType.OBTUSE;
        } else {
            return TriangleAngleType.ACUTE;
        }
    }

    private TriangleMedianData computeTriangleMedians(TriangleDataRequest triangle) {
        val medianA = computeMedian(triangle.getSideAB(), triangle.getSideAC(), triangle.getSideBC());
        val medianB = computeMedian(triangle.getSideAB(), triangle.getSideBC(), triangle.getSideAC());
        val medianC = computeMedian(triangle.getSideAC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleMedianData(medianA, medianB, medianC);
    }

    private double computeMedian(double side1, double side2, double oppositeSide) {
        return Math.sqrt((2 * Math.pow(side1, 2) + 2 * Math.pow(side2, 2) - Math.pow(oppositeSide, 2)) / 4);
    }

    private TriangleBisectorData computeTriangleBisectors(TriangleDataRequest triangle) {
        val bisectorA = computeTriangleBisector(triangle.getAngleA(), triangle.getSideBC(), triangle.getSideAC());
        val bisectorB = computeTriangleBisector(triangle.getAngleB(), triangle.getSideAB(), triangle.getSideAC());
        val bisectorC = computeTriangleBisector(triangle.getAngleC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleBisectorData(bisectorA, bisectorB, bisectorC);
    }

    private double computeTriangleBisector(double angle, double side1, double side2) {
        return (2 * side1 * side2 * Math.cos(Math.toRadians(angle / 2))) / (side1 + side2);
    }

    private TriangleHeightData computeTriangleHeights(TriangleDataRequest triangle) {
        val area = computeArea(triangle);
        val heightA = computeTriangleHeight(area, triangle.getSideBC());
        val heightB = computeTriangleHeight(area, triangle.getSideAC());
        val heightC = computeTriangleHeight(area, triangle.getSideAB());

        return new TriangleHeightData(heightA, heightB, heightC);
    }

    private double computeTriangleHeight(double area, double oppositeSide) {
        return (2 * area) / oppositeSide;
    }

    private double computeInscribedCircleOfTriangleArea(TriangleDataRequest triangle) {
        val halfPerimeter = computeHalfPerimeter(triangle);
        val area = computeArea(triangle);
        val radius = area / halfPerimeter;

        return Math.PI * Math.pow(radius, 2);
    }

    private double computeCircumscribedCircleOfTriangleArea(TriangleDataRequest triangleDataRequest) {
        val radius = triangleDataRequest.getSideAB() / (2 * Math.sin(Math.toRadians(triangleDataRequest.getAngleA())));

        return Math.PI * Math.pow(radius, 2);
    }

    private RightTypeTriangleData computeRightTypeTriangleInfo(TriangleDataRequest triangle) {
        var rightTypeTriangleData = new RightTypeTriangleData();
        setLegsAndHypotenuse(triangle, rightTypeTriangleData);

        rightTypeTriangleData.setSinDegrees(computeSinDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setSinRadians(computeSinDegrees(rightTypeTriangleData));

        rightTypeTriangleData.setCosDegrees(computeCosDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setCosRadians(computeCosRadian(rightTypeTriangleData));

        rightTypeTriangleData.setTgDegrees(computeTangentDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setTgRadians(computeTangentRadian(rightTypeTriangleData));
        return null;
    }

    private double computeSinDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getOppositeLeg() / rightTriangleData.getHypotenuse();
    }

    private double computeSinRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeSinDegrees(rightTriangleData));
    }

    private double computeCosDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getAdjacentLeg() / rightTriangleData.getHypotenuse();
    }

    private double computeCosRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeCosDegrees(rightTriangleData));
    }

    private double computeTangentDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getOppositeLeg() / rightTriangleData.getAdjacentLeg();
    }

    private double computeTangentRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeSinRadian(rightTriangleData));
    }

    private void setLegsAndHypotenuse(TriangleDataRequest triangle, RightTypeTriangleData rightTriangleData) {
        if (triangle.getAngleA() == 90) {
            rightTriangleData.setAdjacentLeg(triangle.getSideAB());
            rightTriangleData.setOppositeLeg(triangle.getSideAC());
            rightTriangleData.setHypotenuse(triangle.getSideBC());
        } else if (triangle.getAngleB() == 90) {
            rightTriangleData.setAdjacentLeg(triangle.getSideBC());
            rightTriangleData.setOppositeLeg(triangle.getSideAB());
            rightTriangleData.setHypotenuse(triangle.getSideAC());
        } else {
            rightTriangleData.setAdjacentLeg(triangle.getSideBC());
            rightTriangleData.setOppositeLeg(triangle.getSideAC());
            rightTriangleData.setHypotenuse(triangle.getSideAB());
        }
    }

}
