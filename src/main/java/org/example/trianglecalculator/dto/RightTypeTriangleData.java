package org.example.trianglecalculator.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RightTypeTriangleData {
    double hypotenuse;
    double adjacentLeg;
    double oppositeLeg;
    double sinDegrees;
    double sinRadians;
    double cosDegrees;
    double cosRadians;
    double tgDegrees;
    double tgRadians;
}
