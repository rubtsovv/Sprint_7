package tests;

import client.OrderClient;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@Epic("Order API")
@Feature("Список заказов")
public class OrderListTest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private final OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @Story("Получение списка заказов")
    @Description("Проверка, что GET /api/v1/orders возвращает непустой список заказов")
    @Severity(SeverityLevel.NORMAL)
    public void testGetOrdersList() {
        orderClient.getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", not(empty()));
    }
}