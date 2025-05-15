package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Order API")
@Feature("Список заказов")
public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Story("Получение списка заказов")
    @Description("Проверка, что GET /api/v1/orders возвращает непустой список заказов")
    @Severity(SeverityLevel.NORMAL)
    public void testGetOrdersList() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()));
    }
}
