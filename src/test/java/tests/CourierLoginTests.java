package tests;

import client.CourierClient;
import io.qameta.allure.*;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Courier API")
public class CourierLoginTests {

    private final CourierClient courierClient = new CourierClient();
    private CourierLogin validCourierLogin;
    private int courierId;  // для удаления

    @Before
    public void setUp() {
        String login = "courier_" + UUID.randomUUID();
        String password = "Qwerty123";
        String firstName = "Vladislav";

        Courier courier = new Courier(login, password, firstName);
        courierClient.createCourier(courier)
                .then().statusCode(SC_CREATED);

        validCourierLogin = new CourierLogin(login, password);

        // Получаем ID курьера для удаления после тестов
        courierId = courierClient.loginCourier(validCourierLogin)
                .then().statusCode(SC_OK)
                .extract().path("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId)
                    .then().statusCode(SC_OK);
        }
    }

    @Test
    @Feature("Логин курьера")
    @Story("Успешный логин")
    @Description("Успешная авторизация курьера")
    public void testLoginCourierSuccessfully() {
        courierClient.loginCourier(validCourierLogin)
                .then().statusCode(SC_OK).body("id", notNullValue());
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине с неверным паролем")
    @Description("Попытка авторизоваться с неверным паролем")
    public void testLoginCourierWithIncorrectPassword() {
        CourierLogin invalidLogin = new CourierLogin(validCourierLogin.getLogin(), "wrongPassword");

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", is("Неверный логин или пароль"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине с неправильным логином")
    @Description("Попытка авторизоваться с неверным логином")
    public void testLoginCourierWithIncorrectLogin() {
        CourierLogin invalidLogin = new CourierLogin("wrongLogin", validCourierLogin.getPassword());

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине без пароля")
    @Description("Попытка авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {
        CourierLogin invalidLogin = new CourierLogin(validCourierLogin.getLogin(), null);

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для авторизации"));
    }

    @Test
    @Feature("Логин курьера")
    @Story("Ошибка при логине без логина")
    @Description("Попытка авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {
        CourierLogin invalidLogin = new CourierLogin(null, validCourierLogin.getPassword());

        courierClient.loginCourier(invalidLogin)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }
}
