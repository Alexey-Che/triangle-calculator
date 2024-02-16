package org.example.trianglecalculator.dto;

import lombok.Builder;
import org.example.trianglecalculator.domain.TriangleAngleType;
import org.example.trianglecalculator.domain.TriangleSideType;

@Builder
public class TriangleDataResponse {

    double area;
    double perimeter;
    TriangleSideType sideType;
    TriangleAngleType angleType;
    TriangleMedianData medians;
    TriangleBisectorData bisectors;
    TriangleHeightData heights;
    double inscribedCircle;   //вписанная окружность
    double circumscribedCircle;    //описанная окружность
    RightTypeTriangleData rightTriangleInfo;
}
