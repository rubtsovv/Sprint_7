package client;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class OrderClient {

    private static final String BASE_PATH = "/api/v1/orders";

    public Response createOrder(model.Order order) {
        return given()
                .contentType("application/json")
                .body(order)
                .post(BASE_PATH);
    }

    public Response getOrdersList() {
        return given()
                .get(BASE_PATH);
    }
}