package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Order API")
@Feature("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Story("Успешное создание заказа")
    @Description("Заказ можно создать с любым или без указания цвета. В ответе должен быть track.")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateOrderWithColors() {
        Order order = new Order(
                "Владислав", "Смирнов", "г. Москва, ул. Ленина, 1",
                "Новогиреево", "+79998887766", 3,
                "2025-05-20", "Пожалуйста, позвоните", color
        );

        given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}