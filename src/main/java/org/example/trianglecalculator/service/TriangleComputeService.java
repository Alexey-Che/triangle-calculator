package org.example.trianglecalculator.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.example.trianglecalculator.domain.TriangleAngleType;
import org.example.trianglecalculator.domain.TriangleSideType;
import org.example.trianglecalculator.dto.*;
import org.example.trianglecalculator.exception.TriangleValidateException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TriangleComputeService {

    private static final int RIGHT_ANGLE_DEGREES = 90;

    TriangleValidationService triangleValidationService;

    /**
     * Получение параметров треугольника
     * @param request переданный в контроллер запрос {@link TriangleDataRequest} значения длин сторон и углов
     * @return {@link TriangleDataResponse}
     * @throws TriangleValidateException если нельзя построить треугольник по заданным параметрам
     */
    public TriangleDataResponse getTriangleInfo(TriangleDataRequest request) {
        triangleValidationService.validateTriangleDataRequest(request);

        val triangleAngleType = determineTriangleAngleType(request);
        val triangleSideType = defineTriangleSideType(request);

        if (triangleSideType == TriangleSideType.EQUILATERAL
                && !triangleValidationService.isTriangleEquilateral(request)) {
            throw new TriangleValidateException(
                    List.of("углы в равностороннем треугольнике должны быть равны друг другу")
            );
        }
        var triangleInfo = TriangleDataResponse.builder()
                .area(computeArea(request))
                .perimeter(computePerimeter(request))
                .sideType(triangleSideType)
                .angleType(triangleAngleType);

        if (triangleAngleType == TriangleAngleType.RIGHT) {
            triangleInfo.rightTriangleInfo(computeRightTypeTriangleInfo(request));
        }

        triangleInfo.medians(computeTriangleMedians(request))
                .bisectors(computeTriangleBisectors(request))
                .heights(computeTriangleHeights(request))
                .inscribedCircle(computeInscribedCircleOfTriangleArea(request))
                .circumscribedCircle(computeCircumscribedCircleOfTriangleArea(request));

        return triangleInfo.build();
    }

    /**
     * Вычисление площади треугольника.
     *
     * @param triangle {@link TriangleDataRequest} значения длин сторон.
     * @return площадь треугольника.
     *
     * @implNote Площадь треугольника вычисляется по формуле Герона:
     * <pre>
     *     s = √(p * (p - a) * (p - b) * (p - c))
     * </pre>
     * где s - площадь треугольника, a, b и c - длины сторон, p - полупериметр треугольника.
     */
    private double computeArea(TriangleDataRequest triangle) {
        double s = computeHalfPerimeter(triangle);
        return Math.sqrt(s * (s - triangle.getSideAB()) * (s - triangle.getSideBC()) * (s - triangle.getSideAC()));
    }

    /**
     * Вычисление периметра треугольника.
     *
     * @param triangle {@link TriangleDataRequest} значения длин сторон.
     * @return числовое значение периметра треугольника.
     *
     * @implNote Периметр треугольника вычисляется как сумма длин всех его сторон:
     * <pre>
     *     P = a + b + c
     * </pre>
     * где P - периметр треугольника, a, b и c - длины сторон.
     */
    private double computePerimeter(TriangleDataRequest triangle) {
        return triangle.getSideAB() + triangle.getSideBC() + triangle.getSideAC();
    }

    /**
     * Вычисление половины периметра треугольника.
     *
     * @param triangle {@link TriangleDataRequest} значения длин сторон.
     * @return числовое значение половины периметра треугольника.
     *
     * @implNote Половина периметра треугольника вычисляется как половина суммы длин всех его сторон:
     * <pre>
     *     P/2 = (a + b + c) / 2
     * </pre>
     * где P/2 - половина периметра треугольника, a, b и c - длины сторон.
     */
    private double computeHalfPerimeter(TriangleDataRequest triangle) {
        return computePerimeter(triangle) / 2.0;
    }

    /**
     * Определение типа треугольника по его сторонам
     * @param triangle {@link TriangleDataRequest} значения длин сторон
     * @return {@link TriangleSideType} тип треугольника
     */
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

    /**
     * Определение типа треугольника по его углам
     * @param triangle {@link TriangleDataRequest} со значениями углов треугольника в градусах
     * @return {@link TriangleAngleType} тип треугольника
     */
    private TriangleAngleType determineTriangleAngleType(TriangleDataRequest triangle) {
        if (triangle.getAngleA() == RIGHT_ANGLE_DEGREES || triangle.getAngleB() == RIGHT_ANGLE_DEGREES
                || triangle.getAngleC() == RIGHT_ANGLE_DEGREES) {
            return TriangleAngleType.RIGHT;
        } else if (triangle.getAngleA() > RIGHT_ANGLE_DEGREES || triangle.getAngleB() > RIGHT_ANGLE_DEGREES
                || triangle.getAngleC() > RIGHT_ANGLE_DEGREES) {
            return TriangleAngleType.OBTUSE;
        } else {
            return TriangleAngleType.ACUTE;
        }
    }

    /**
     * Вычисление медиан треугольника
     * @param triangle {@link TriangleDataRequest} значения длин сторон
     * @return {@link TriangleDataRequest} числовые значения медиан треугольника
     */
    private TriangleMedianData computeTriangleMedians(TriangleDataRequest triangle) {
        val medianA = computeMedian(triangle.getSideAB(), triangle.getSideAC(), triangle.getSideBC());
        val medianB = computeMedian(triangle.getSideAB(), triangle.getSideBC(), triangle.getSideAC());
        val medianC = computeMedian(triangle.getSideAC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleMedianData(medianA, medianB, medianC);
    }

    /**
     * Вычисление длины медианы треугольника.
     *
     * @param side1 смежная сторона 1.
     * @param side2 смежная сторона 2.
     * @param oppositeSide противоположная углу сторона треугольника.
     * @return числовое значение длины медианы треугольника.
     *
     * @implNote Длина медианы треугольника вычисляется по формуле:
     * <pre>
     *     m = √((2 * a^2 + 2 * b^2 - c^2) / 4)
     * </pre>
     * где m - длина медианы треугольника, a и b - смежные стороны, c - противоположная сторона.
     */
    private double computeMedian(double side1, double side2, double oppositeSide) {
        return Math.sqrt((2 * Math.pow(side1, 2) + 2 * Math.pow(side2, 2) - Math.pow(oppositeSide, 2)) / 4);
    }

    /**
     * Вычисление биссектрис треугольника
     * @param triangle {@link TriangleDataRequest} значения длин сторон
     * @return {@link TriangleBisectorData} числовые значения биссектрис треугольника
     */
    private TriangleBisectorData computeTriangleBisectors(TriangleDataRequest triangle) {
        val bisectorA = computeTriangleBisector(triangle.getAngleA(), triangle.getSideBC(), triangle.getSideAC());
        val bisectorB = computeTriangleBisector(triangle.getAngleB(), triangle.getSideAB(), triangle.getSideAC());
        val bisectorC = computeTriangleBisector(triangle.getAngleC(), triangle.getSideBC(), triangle.getSideAB());

        return new TriangleBisectorData(bisectorA, bisectorB, bisectorC);
    }

    /**
     * Вычисление длины биссектрисы треугольника.
     *
     * @param angle угол из которого проводится биссектриса (в градусах).
     * @param side1 смежная сторона 1.
     * @param side2 смежная сторона 2.
     * @return числовое значение длины биссектрисы треугольника.
     *
     * @implNote Длина биссектрисы треугольника вычисляется по формуле:
     * <pre>
     *     b = (2 * a * c * cos(θ/2)) / (a + c)
     * </pre>
     * где b - длина биссектрисы треугольника, a и c - смежные стороны, θ - угол.
     */
    private double computeTriangleBisector(double angle, double side1, double side2) {
        return (2 * side1 * side2 * Math.cos(Math.toRadians(angle / 2))) / (side1 + side2);
    }

    /**
     * Вычисление высот треугольника
     * @param triangle {@link TriangleDataRequest} значения длин сторон
     * @return {@link TriangleHeightData} числовые значения высот треугольника
     */
    private TriangleHeightData computeTriangleHeights(TriangleDataRequest triangle) {
        val area = computeArea(triangle);
        val heightA = computeTriangleHeight(area, triangle.getSideBC());
        val heightB = computeTriangleHeight(area, triangle.getSideAC());
        val heightC = computeTriangleHeight(area, triangle.getSideAB());

        return new TriangleHeightData(heightA, heightB, heightC);
    }

    /**
     * Вычисление высоты треугольника.
     *
     * @param area площадь треугольника.
     * @param oppositeSide длина противоположной стороны треугольника от угла, из которого проводится высота.
     * @return числовое значение длины высоты треугольника.
     *
     * @implNote Длина высоты треугольника вычисляется по формуле:
     * <pre>
     *     h = (2 * A) / c
     * </pre>
     * где h - длина высоты треугольника, A - площадь треугольника, c - длина противоположной стороны.
     */
    private double computeTriangleHeight(double area, double oppositeSide) {
        return (2 * area) / oppositeSide;
    }

    /**
     * Вычисление площади вписанной в треугольник окружности.
     *
     * @param triangle {@link TriangleDataRequest} значения длин сторон.
     * @return числовое значение площади вписанной в треугольник окружности.
     *
     * @implNote Площадь вписанной в треугольник окружности вычисляется по формуле:
     * <pre>
     *     A = π * r^2
     * </pre>
     * где A - площадь вписанной в треугольник окружности, r - радиус этой окружности.
     * Радиус окружности, вписанной в треугольник, вычисляется как отношение площади треугольника к его полупериметру.
     */
    private double computeInscribedCircleOfTriangleArea(TriangleDataRequest triangle) {
        val halfPerimeter = computeHalfPerimeter(triangle);
        val area = computeArea(triangle);
        val radius = area / halfPerimeter;

        return Math.PI * Math.pow(radius, 2);
    }

    /**
     * Вычисление площади описанной вокруг треугольника окружности.
     *
     * @param triangle {@link TriangleDataRequest} значения длин сторон и углов треугольника.
     * @return числовое значение площади описанной вокруг треугольника окружности.
     *
     * @implNote Площадь описанной вокруг треугольника окружности вычисляется по формуле:
     * <pre>
     *     A = π * R^2
     * </pre>
     * где A - площадь описанной вокруг треугольника окружности, R - радиус этой окружности.
     * Радиус окружности, описанной вокруг треугольника, вычисляется как отношение длины любой из сторон треугольника к удвоенной синусе соответствующего угла.
     */
    private double computeCircumscribedCircleOfTriangleArea(TriangleDataRequest triangle) {
        val radius = triangle.getSideAB() / (2 * Math.sin(Math.toRadians(triangle.getAngleA())));

        return Math.PI * Math.pow(radius, 2);
    }

    /**
     * Вычисление дополнительной информации для прямоугольного треугольника
     * @param triangle {@link TriangleDataRequest} значения длин сторон и углов треугольника
     * @return {@link RightTypeTriangleData} информация о синусе, косинусе, тангенсе, длинах катетов и гипотенузы
     */
    private RightTypeTriangleData computeRightTypeTriangleInfo(TriangleDataRequest triangle) {

        var rightTypeTriangleData = new RightTypeTriangleData();
        setLegsAndHypotenuse(triangle, rightTypeTriangleData);

        if (!triangleValidationService.isTriangleRightByPythagoreanTheorem(rightTypeTriangleData)) {
            throw new TriangleValidateException(List.of("Сумма квадратов катетов не равна квадрату гипотенузы"));
        }

        rightTypeTriangleData.setSinDegrees(computeSinDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setSinRadians(computeSinDegrees(rightTypeTriangleData));

        rightTypeTriangleData.setCosDegrees(computeCosDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setCosRadians(computeCosRadian(rightTypeTriangleData));

        rightTypeTriangleData.setTgDegrees(computeTangentDegrees(rightTypeTriangleData));
        rightTypeTriangleData.setTgRadians(computeTangentRadian(rightTypeTriangleData));

        return rightTypeTriangleData;
    }

    /**
     * Вычисление синуса угла в градусах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение синуса угла в градусах.
     *
     * @implNote Синус угла в градусах в прямоугольном треугольнике вычисляется как отношение длины противоположенной стороны к длине гипотенузы:
     * <pre>
     *     sin(θ) = a / c
     * </pre>
     * где sin(θ) - синус угла, a - длина противоположенной стороны, c - длина гипотенузы.
     */
    private double computeSinDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getOppositeLeg() / rightTriangleData.getHypotenuse();
    }

    /**
     * Вычисление синуса угла в радианах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение синуса угла в радианах.
     *
     * @implNote Синус угла в радианах в прямоугольном треугольнике вычисляется как синус угла в градусах, переведенный в радианы:
     * <pre>
     *     sin(θ) = Math.toRadians(sinDegrees(θ))
     * </pre>
     * где sin(θ) - синус угла в радианах, sinDegrees(θ) - синус угла в градусах.
     */
    private double computeSinRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeSinDegrees(rightTriangleData));
    }

    /**
     * Вычисление косинуса угла в градусах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение косинуса угла в градусах.
     *
     * @implNote Косинус угла в градусах в прямоугольном треугольнике вычисляется как отношение длины прилегающего к углу катета к длине гипотенузы:
     * <pre>
     *     cos(θ) = b / c
     * </pre>
     * где cos(θ) - косинус угла, b - длина прилегающего к углу катета, c - длина гипотенузы.
     */
    private double computeCosDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getAdjacentLeg() / rightTriangleData.getHypotenuse();
    }

    /**
     * Вычисление косинуса угла в радианах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение косинуса угла в радианах.
     *
     * @implNote Косинус угла в радианах в прямоугольном треугольнике вычисляется как косинус угла в градусах, переведенный в радианы:
     * <pre>
     *     cos(θ) = Math.toRadians(cosDegrees(θ))
     * </pre>
     * где cos(θ) - косинус угла в радианах, cosDegrees(θ) - косинус угла в градусах.
     */
    private double computeCosRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeCosDegrees(rightTriangleData));
    }

    /**
     * Вычисление тангенса угла в градусах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение тангенса угла в градусах.
     *
     * @implNote Тангенс угла в градусах в прямоугольном треугольнике вычисляется как отношение противолежащей катета к прилежащей катету:
     * <pre>
     *     tan(θ) = a / b
     * </pre>
     * где tan(θ) - тангенс угла, a - длина противоположенного катета, b - длина прилежащего катета.
     */
    private double computeTangentDegrees(RightTypeTriangleData rightTriangleData) {
        return rightTriangleData.getOppositeLeg() / rightTriangleData.getAdjacentLeg();
    }

    /**
     * Вычисление тангенса угла в радианах в прямоугольном треугольнике.
     *
     * @param rightTriangleData {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника.
     * @return значение тангенса угла в радианах.
     *
     * @implNote Тангенс угла в радианах в прямоугольном треугольнике вычисляется как отношение противолежащего катета к прилежащему катету:
     * <pre>
     *     tan(θ) = oppositeLeg / adjacentLeg
     * </pre>
     * где \( \tan(θ) \) - тангенс угла, oppositeLeg - длина противолежащего катета, adjacentLeg - длина прилежащего катета.
     */
    private double computeTangentRadian(RightTypeTriangleData rightTriangleData) {
        return Math.toRadians(computeSinRadian(rightTriangleData));
    }

    /**
     * Устанавливает значения гипотенузы и катетов в {@link RightTypeTriangleData}
     * @param triangle {@link TriangleDataRequest} значения длин сторон и углов прямоугольного треугольника
     * @param rightTriangleData {@link RightTypeTriangleData}  с установленными полями для гипотенузы и катетов
     */
    private void setLegsAndHypotenuse(TriangleDataRequest triangle, RightTypeTriangleData rightTriangleData) {
        if (triangle.getAngleA() == RIGHT_ANGLE_DEGREES) {
            rightTriangleData.setAdjacentLeg(triangle.getSideAB());
            rightTriangleData.setOppositeLeg(triangle.getSideAC());
            rightTriangleData.setHypotenuse(triangle.getSideBC());
        } else if (triangle.getAngleB() == RIGHT_ANGLE_DEGREES) {
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
