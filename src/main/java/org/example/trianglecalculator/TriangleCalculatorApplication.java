package org.example.trianglecalculator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Сервис расчета параметров треугольника",
        version = "0.0.1",
        contact = @Contact(email = "lexa.36484@gmail.com",
                url = "https://t.me/KorotkihA"),
        description = "Коротких А.И."))
@SpringBootApplication
public class TriangleCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriangleCalculatorApplication.class, args);
    }

}
