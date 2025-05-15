package tests;

import client.CourierClient;
import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

@Epic("Courier API")
public class CourierTests {

    private final CourierClient courierClient = new CourierClient();
    private Courier testCourier;
    private int courierId = 0;

    @Before
    public void setUp() {
        // Уникальный логин на каждую сессию
        String uniqueLogin = "courier_" + System.currentTimeMillis();
        testCourier = new Courier(uniqueLogin, "qwerty123", "Vladislav");
        courierClient.createCourier(testCourier)
                .then().statusCode(anyOf(is(201), is(200)));

        courierId = extractCourierId(testCourier);
    }

    @After
    public void tearDown() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId)
                    .then().statusCode(200);
        }
    }

    private int extractCourierId(Courier courier) {
        return courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()))
                .then().statusCode(200).extract().path("id");
    }

    @Test
    @Feature("Создание курьера")
    @Story("Успешное создание")
    @Description("Создание нового уникального курьера")
    public void testCreateCourierSuccessfully() {
        Courier newCourier = new Courier("courier_" + System.currentTimeMillis(), "pass123", "Vlad");
        courierClient.createCourier(newCourier)
                .then().statusCode(201).body("ok", is(true));

        // Удаление созданного курьера после успешной регистрации
        int id = extractCourierId(newCourier);
        courierClient.deleteCourier(id)
                .then().statusCode(200);
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании одинакового курьера")
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void testCreateCourierWithExistingLogin() {
        courierClient.createCourier(testCourier)
                .then().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании курьера без обязательного поля")
    @Description("Попытка создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Courier invalidCourier = new Courier("newLogin_" + System.currentTimeMillis(), null, "Vlad");

        courierClient.createCourier(invalidCourier)
                .then().statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании курьера без логина")
    @Description("Попытка создать курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Courier invalidCourier = new Courier(null, "securePassword", "Vlad");

        courierClient.createCourier(invalidCourier)
                .then().statusCode(400)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Успешный логин")
    @Description("Успешная авторизация курьера")
    public void testLoginCourierSuccessfully() {
        courierClient.loginCourier(
                        new CourierLogin(testCourier.getLogin(), testCourier.getPassword()))
                .then().statusCode(200).body("id", notNullValue());
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине с неверным паролем")
    @Description("Попытка авторизоваться с неверным паролем")
    public void testLoginCourierWithIncorrectPassword() {
        CourierLogin invalidLogin = new CourierLogin(testCourier.getLogin(), "wrongPassword");

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(400)
                .body("message", is("Неверный логин или пароль"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине с неправильным логином")
    @Description("Попытка авторизоваться с неверным логином")
    public void testLoginCourierWithIncorrectLogin() {
        CourierLogin invalidLogin = new CourierLogin("wrongLogin", testCourier.getPassword());

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине без пароля")
    @Description("Попытка авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {
        CourierLogin invalidLogin = new CourierLogin(testCourier.getLogin(), null);

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(400)
                .body("message", is("Недостаточно данных для авторизации"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине без логина")
    @Description("Попытка авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {
        CourierLogin invalidLogin = new CourierLogin(null, testCourier.getPassword());

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }
}