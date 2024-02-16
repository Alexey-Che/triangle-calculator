package org.example.trianglecalculator.service;

import org.example.trianglecalculator.dto.RightTypeTriangleData;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.springframework.stereotype.Service;

@Service
public class TriangleValidationService {

    public void validateTriangleDataRequest(TriangleDataRequest request) {

    }

    public boolean isTriangleRightByPythagoreanTheorem(RightTypeTriangleData triangle) {
        return Math.pow(triangle.getHypotenuse(), 2) == Math.pow(triangle.getAdjacentLeg(), 2) + Math.pow(triangle.getOppositeLeg(), 2);
    }
}
