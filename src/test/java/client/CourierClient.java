package client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;

import static io.restassured.RestAssured.*;

public class CourierClient {

    private static final String BASE_PATH = "/api/v1/courier";

    public CourierClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(BASE_PATH);
    }

    public Response loginCourier(CourierLogin login) {
        return given()
                .header("Content-type", "application/json")
                .body(login)
                .post(BASE_PATH + "/login");
    }

    public Response deleteCourier(int courierId) {
        return given()
                .delete(BASE_PATH + "/" + courierId);
    }
}