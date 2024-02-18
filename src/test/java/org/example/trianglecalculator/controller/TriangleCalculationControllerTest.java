package org.example.trianglecalculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TriangleCalculationControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    public MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("listOfCorrectTriangleParameters")
    @SneakyThrows
    void getTriangleInfo(TriangleDataRequest request) {
        mockMvc.
                perform(post("/api/v1/triangle/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.area", is(notNullValue())))
                .andExpect(jsonPath("$.perimeter", is(notNullValue())))
                .andExpect(jsonPath("$.sideType", is(notNullValue())))
                .andExpect(jsonPath("$.angleType", is(notNullValue())))
                .andExpect(jsonPath("$.medians", is(notNullValue())))
                .andExpect(jsonPath("$.bisectors", is(notNullValue())))
                .andExpect(jsonPath("$.heights", is(notNullValue())))
                .andExpect(jsonPath("$.inscribedCircle", is(notNullValue())))
                .andExpect(jsonPath("$.circumscribedCircle", is(notNullValue())));
    }

    static Stream<Arguments> listOfCorrectTriangleParameters() {
        return Stream.of(
                arguments(new TriangleDataRequest(5, 5, 5, 60, 60 , 60)),
                arguments(new TriangleDataRequest(10, 5, 10, 28.96, 75.52 , 75.52)),
                arguments(new TriangleDataRequest(14.14, 19.32, 10, 105, 30 , 45)),
                arguments(new TriangleDataRequest(3, 5, 4, 90, 53.13 , 36.87))
        );
    }

    @ParameterizedTest
    @MethodSource("listOfInCorrectTriangleParameters")
    @SneakyThrows
    void getBadRequestWhenParametersIncorrect(TriangleDataRequest request) {
        mockMvc.
                perform(post("/api/v1/triangle/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> listOfInCorrectTriangleParameters() {
        return Stream.of(
                arguments(new TriangleDataRequest(0, 5, 0, 60, 60 , 60)),
                arguments(new TriangleDataRequest(10, 10, 10, 28.96, 75.52 , 75.52)),
                arguments(new TriangleDataRequest(14.14, 19.32, 10, 190, 30 , 45)),
                arguments(new TriangleDataRequest(3, 6, 4, 190, 30 , 45)),
                arguments(new TriangleDataRequest(3, 5, 4, 0, 53.13 , 36.87))
        );
    }

    @Test
    @SneakyThrows
    void getRightTriangleInfo() {
        TriangleDataRequest request = TriangleDataRequest.builder()
                .sideAB(3)
                .sideAC(4)
                .sideBC(5)
                .angleA(90)
                .angleB(53.13)
                .angleC(36.87)
                .build();
        mockMvc.
                perform(post("/api/v1/triangle/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.area", is(notNullValue())))
                .andExpect(jsonPath("$.perimeter", is(notNullValue())))
                .andExpect(jsonPath("$.sideType", is(notNullValue())))
                .andExpect(jsonPath("$.angleType", is(notNullValue())))
                .andExpect(jsonPath("$.medians", is(notNullValue())))
                .andExpect(jsonPath("$.bisectors", is(notNullValue())))
                .andExpect(jsonPath("$.heights", is(notNullValue())))
                .andExpect(jsonPath("$.inscribedCircle", is(notNullValue())))
                .andExpect(jsonPath("$.circumscribedCircle", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.hypotenuse", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.adjacentLeg", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.oppositeLeg", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.sinDegrees", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.sinRadians", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.cosDegrees", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.cosRadians", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.tgDegrees", is(notNullValue())))
                .andExpect(jsonPath("$.rightTriangleInfo.tgRadians", is(notNullValue())));


    }
}