package org.example.trianglecalculator.service;

import org.example.trianglecalculator.dto.RightTypeTriangleData;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.example.trianglecalculator.exception.TriangleValidateException;
import org.springframework.stereotype.Service;

@Service
public class TriangleValidationService {

    public void validateTriangleDataRequest(TriangleDataRequest request) {
        if (!isTriangle(request)) {
            throw new TriangleValidateException("Одна из сторон меньше суммы двух других");
        }

        if (!have180Degrees(request)) {
            throw new TriangleValidateException("Сумма углов треугольника не равна 180");
        }
    }

    public boolean isTriangleRightByPythagoreanTheorem(RightTypeTriangleData triangle) {
        return Math.pow(triangle.getHypotenuse(), 2) == Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2);
    }

    public boolean have180Degrees(TriangleDataRequest request) {
        return request.getAngleA() + request.getAngleB() + request.getAngleC() == 180;
    }

    public boolean isTriangle(TriangleDataRequest request) {
        return request.getSideAB() + request.getSideAC() > request.getSideBC()
                && request.getSideAB() + request.getSideBC() > request.getSideAC()
                && request.getSideBC() + request.getSideAC() > request.getSideAB();
    }
}
