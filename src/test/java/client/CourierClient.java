package client;

import io.qameta.allure.Step;
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

    @Step("Создание курьера с логином: {0}")
    public Response createCourier(Courier courier) {
        return given()
                .contentType("application/json")
                .body(courier)
                .post(BASE_PATH);
    }

    @Step("Логин курьера с логином: {0}")
    public Response loginCourier(CourierLogin login) {
        return given()
                .contentType("application/json")
                .body(login)
                .post(BASE_PATH + "/login");
    }

    @Step("Удаление курьера с ID: {0}")
    public Response deleteCourier(int courierId) {
        return given()
                .delete(BASE_PATH + "/" + courierId);
    }
}