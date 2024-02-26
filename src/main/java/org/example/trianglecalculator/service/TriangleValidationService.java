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

    /**
     * Проверяет параметры треугольника по критериям существования
     * @param request {@link TriangleDataRequest} значения длин сторон и углов треугольника
     */
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

    /**
     * Проверка прямоугольного треугольника по теореме Пифагора
     * @param triangle {@link TriangleDataRequest} значения длин сторон
     * @return соответствие прямоугольного треугольника теореме Пифагора
     */
    public boolean isTriangleRightByPythagoreanTheorem(RightTypeTriangleData triangle) {
        return Math.pow(triangle.getHypotenuse(), 2) - MEASUREMENT_ERROR <
                Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2)
                && Math.pow(triangle.getHypotenuse(), 2) + MEASUREMENT_ERROR >
                Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2);
    }

    /**
     * Проверка суммы углов в треугольнике
     * @param request {@link TriangleDataRequest} значения углов в градусах
     * @return соответствие сумме в 180 градусов
     */
    private boolean have180Degrees(TriangleDataRequest request) {
        return request.getAngleA() + request.getAngleB() + request.getAngleC() == 180;
    }

    /**
     * Проверка, что ни одна из сторон в треугольнике не больше суммы двух других
     * @param request {@link TriangleDataRequest} значения длин сторон
     * @return соответствие того, что ни одна из сторон в треугольнике не больше суммы двух других
     */
    private boolean isTriangle(TriangleDataRequest request) {
        return request.getSideAB() + request.getSideAC() > request.getSideBC()
                && request.getSideAB() + request.getSideBC() > request.getSideAC()
                && request.getSideBC() + request.getSideAC() > request.getSideAB();
    }

    /**
     * Проверка равностороннего треугольника
     * @param request {@link TriangleDataRequest} значения длин сторон
     * @return равенство всех сторон треугольника
     */
    public boolean isTriangleEquilateral(TriangleDataRequest request) {
        return request.getAngleA() == request.getAngleB()
                && request.getAngleB() == request.getAngleC()
                && request.getAngleC() == request.getAngleA();

    }

}
