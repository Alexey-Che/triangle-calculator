package org.example.trianglecalculator.service;

import org.example.trianglecalculator.dto.RightTypeTriangleData;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.example.trianglecalculator.exception.TriangleValidateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TriangleValidationService {

    public static final double MEASUREMENT_ERROR = 0.1;

    public void validateTriangleDataRequest(TriangleDataRequest request) {
        List<String> errors = new ArrayList<>();

        if (!isTriangle(request)) {
            errors.add("Одна из сторон больше суммы двух других");
        }

        if (!have180Degrees(request)) {
            errors.add("Сумма углов треугольника не равна 180");
        }

        if (!errors.isEmpty()) {
            throw new TriangleValidateException(errors);
        }
    }

    public boolean isTriangleRightByPythagoreanTheorem(RightTypeTriangleData triangle) {
        return Math.pow(triangle.getHypotenuse(), 2) + MEASUREMENT_ERROR >
                Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2)
                || Math.pow(triangle.getHypotenuse(), 2) + MEASUREMENT_ERROR <
                Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2);
    }

    private boolean have180Degrees(TriangleDataRequest request) {
        return request.getAngleA() + request.getAngleB() + request.getAngleC() == 180;
    }

    private boolean isTriangle(TriangleDataRequest request) {
        return request.getSideAB() + request.getSideAC() > request.getSideBC()
                && request.getSideAB() + request.getSideBC() > request.getSideAC()
                && request.getSideBC() + request.getSideAC() > request.getSideAB();
    }

    public boolean isTriangleEquilateral(TriangleDataRequest request) {
        return request.getAngleA() == request.getAngleB()
                && request.getAngleB() == request.getAngleC()
                && request.getAngleC() == request.getAngleA();

    }

}
