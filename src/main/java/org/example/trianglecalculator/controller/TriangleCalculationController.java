package org.example.trianglecalculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.trianglecalculator.dto.TriangleDataRequest;
import org.example.trianglecalculator.dto.TriangleDataResponse;
import org.example.trianglecalculator.dto.TriangleValidationErrors;
import org.example.trianglecalculator.exception.TriangleValidateException;
import org.example.trianglecalculator.service.TriangleComputeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/triangle")
@Tag(name = "Расчет параметров треугольника")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TriangleCalculationController {

    TriangleComputeService triangleComputeService;

    @Operation(description = """
            Получение информации о треугольнике по длинам его сторон и величине углов:
            - Площадь
            - Периметр
            - Определение типа по сторонам (равнобедренный, разносторонний, равносторонний)
            - Определения типа по углам (острый, тупой, прямой)
            - Длины медиан
            - Длины биссектрис
            - Длины высот
            - Площадь вписанной окружности
            - Площадь описанной окружности
            
           Для прямых треугольников дополнительно рассчитываются:
            - Синус (в градусах и радианах),
            - Косинус (в градусах и радианах),
            - Тангенс (в градусах и радианах)
            - Длины катетов и гипотенузы (исходя из предоставленных данных)
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о треугольнике",
                    content = @Content(schema = @Schema(implementation = TriangleDataResponse.class))),
            @ApiResponse(responseCode = "400", description = "Входные данные не прошли валидацию",
                    content = @Content(schema = @Schema(oneOf = {TriangleValidationErrors.class, Map.class}),
                            examples = {
                                    @ExampleObject(name = "пример ошибки построения треугольника",
                                            value = "{\"errors\": [\"Одна из сторон больше суммы двух других\"," +
                                                    "\"Сумма углов треугольника не равна 180\"]}"),
                                    @ExampleObject(name = "пример ошибки введенных данных",
                                            value = "{\"sideAB\": \"Нужно указать длину стороны треугольника, " +
                                                    "ее значение должно быть больше 0\"}")
                            }))
    })
    @PostMapping("/info")
    public ResponseEntity<?> getTriangleInfo(@RequestBody @Valid TriangleDataRequest request) {
        return ResponseEntity.ok().body(triangleComputeService.getTriangleInfo(request));
    }

    @ExceptionHandler(TriangleValidateException.class)
    public ResponseEntity<?> handleTriangleValidateException(TriangleValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TriangleValidationErrors(e.getErrors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
