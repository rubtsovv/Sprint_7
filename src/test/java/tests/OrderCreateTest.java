package tests;

import client.OrderClient;
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

import static org.hamcrest.Matchers.notNullValue;

@Epic("Order API")
@Feature("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    private final List<String> color;
    private final OrderClient orderClient = new OrderClient();

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
        RestAssured.baseURI = BASE_URI;
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

        orderClient.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}