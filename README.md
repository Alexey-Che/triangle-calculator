## Triangle-calculator  сервис расчета параметров треугольника

Java : 17

Получение информации о треугольнике по длинам его сторон и величине углов:

    Площадь
    Периметр
    Определение типа по сторонам (равнобедренный, разносторонний, равносторонний)
    Определения типа по углам (острый, тупой, прямой)
    Длины медиан
    Длины биссектрис
    Длины высот
    Площадь вписанной окружности
    Площадь описанной окружности

Для прямых треугольников дополнительно рассчитываются:

    Синус (в градусах и радианах),
    Косинус (в градусах и радианах),
    Тангенс (в градусах и радианах)
    Длины катетов и гипотенузы (исходя из предоставленных данных)


### Swagger
Документация swagger доступна при локальном запуске на  [*http://localhost:8080/swagger-ui/#/*](http://localhost:8080/swagger-ui/#/)

### Примеры ответов:

- 200: 
```
  {
  "area": 6,
  "perimeter": 12,
  "sideType": "SCALENE",
  "angleType": "RIGHT",
  "medians": {
    "medianA": 2.5,
    "medianB": 3.605551275463989,
    "medianC": 4.272001872658765
  },
  "bisectors": {
    "bisectorA": 3.1426968052735447,
    "bisectorB": 3.066608881558455,
    "bisectorC": 3.557561308473011
  },
  "heights": {
    "heightA": 2.4,
    "heightB": 3,
    "heightC": 4
  },
  "inscribedCircle": 3.141592653589793,
  "circumscribedCircle": 7.0685834705770345,
  "rightTriangleInfo": {
    "hypotenuse": 5,
    "adjacentLeg": 3,
    "oppositeLeg": 4,
    "sinDegrees": 0.8,
    "sinRadians": 0.8,
    "cosDegrees": 0.6,
    "cosRadians": 0.010471975511965976,
    "tgDegrees": 1.3333333333333333,
    "tgRadians": 0.00024369393582936687
  }
}
```
- 400:
```
{
  "errors": [
    "Одна из сторон больше суммы двух других",
    "Сумма углов треугольника не равна 180"
  ]
}
```