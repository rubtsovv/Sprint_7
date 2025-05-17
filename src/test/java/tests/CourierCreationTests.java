package tests;

import client.CourierClient;
import io.qameta.allure.*;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Courier API")
public class CourierCreationTests {

    private final CourierClient courierClient = new CourierClient();
    private Courier testCourier;
    private int courierId = 0;

    @Before
    public void setUp() {
        // Уникальный логин на каждую сессию
        String uniqueLogin = "courier_" + System.currentTimeMillis();
        testCourier = new Courier(uniqueLogin, "qwerty123", "Vladislav");
        courierClient.createCourier(testCourier)
                .then().statusCode(anyOf(is(SC_CREATED), is(SC_OK)));

        courierId = extractCourierId(testCourier);
    }

    @After
    public void tearDown() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId)
                    .then().statusCode(SC_OK);
        }
    }

    private int extractCourierId(Courier courier) {
        return courierClient.loginCourier(new CourierLogin(courier.getLogin(), courier.getPassword()))
                .then().statusCode(SC_OK).extract().path("id");
    }

    @Test
    @Feature("Создание курьера")
    @Story("Успешное создание")
    @Description("Создание нового уникального курьера")
    public void testCreateCourierSuccessfully() {
        Courier newCourier = new Courier("courier_" + System.currentTimeMillis(), "pass123", "Vlad");
        courierClient.createCourier(newCourier)
                .then().statusCode(SC_CREATED).body("ok", is(true));
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании одинакового курьера")
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void testCreateCourierWithExistingLogin() {
        courierClient.createCourier(testCourier)
                .then().statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании курьера без обязательного поля")
    @Description("Попытка создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Courier invalidCourier = new Courier("newLogin_" + System.currentTimeMillis(), null, "Vlad");

        courierClient.createCourier(invalidCourier)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }

    @Test
    @Feature("Создание курьера")
    @Story("Ошибка при создании курьера без логина")
    @Description("Попытка создать курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Courier invalidCourier = new Courier(null, "securePassword", "Vlad");

        courierClient.createCourier(invalidCourier)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных"));
    }
}