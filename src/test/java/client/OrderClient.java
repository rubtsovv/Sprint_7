package client;

import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.*;

public class OrderClient {

    private static final String BASE_PATH = "/api/v1/orders";

    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(BASE_PATH);
    }

    public Response getOrdersList() {
        return get(BASE_PATH);
    }
}