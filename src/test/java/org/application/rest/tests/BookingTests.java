package org.application.rest.tests;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.application.rest.model.Booking;
import org.application.rest.model.BookingDates;
import org.application.rest.model.User;
import org.junit.jupiter.api.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BookingTests {

    public static Faker faker;
    private static RequestSpecification requestSpecification;
    private static Booking booking;
    private static BookingDates bookingDates;
    private static User user;

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        faker = new Faker();
        user = new User
                (faker.name().username(),
                faker.number().randomDigit(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(8,10),
                faker.phoneNumber().toString());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        bookingDates = new BookingDates(sdf.format(faker.date().past(1, TimeUnit.DAYS)), sdf.format(faker.date().future(1, TimeUnit.DAYS)));
        booking = new Booking(user.getFirstName(), user.getLastName(),
                (float)faker.number().randomDouble(2,50,100000),
        true, bookingDates, "");

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());
    }

    @BeforeEach
    public void setRequest(){

        requestSpecification = given().config(RestAssured.config().logConfig(
                LogConfig.logConfig()
                .enableLoggingOfRequestAndResponseIfValidationFails())).contentType(ContentType.JSON)
                .auth().basic("admin", "password123");
    }


    @Test
    @Order(3)
    public void getBookingById_returnOk(){

        requestSpecification
                .when()
                .get("/booking/" + user.getId())
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(1)
    public void createBooking_WithValidData_returnOk(){
        RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking")
                .contentType(ContentType.JSON)
                .body("{\n" +

                        "\"firstname\" : \"Bea\",\n" +
                        "\"lastname\" : \"Gomes\",\n" +
                        "\"totalprice\" : 111,\n" +
                        "\"depositpaid\" : true,\n" +
                        "\"bookingdates\" : {\n" +
                        "    \"checkin\" : \"2018-01-01\",\n" +
                        "    \"checkout\" : \"2019-01-01\"\n" +
                        "},\n" +
                        "\"additionalneeds\" : \"None\"\n" +
                        "}")
                .when().post()
                .then().statusCode(200);

    }

    @Test
    @Order(2)
    public void  getAllBookingsByUserFirstName_BookingExists_returnOk(){
        requestSpecification
                .when()
                .queryParam("firstName", faker.name().firstName())
                .get("/booking")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                .body("results", hasSize(greaterThan(0)));
    }

    @Test // create token
    public void CreateAuthToken(){
        Map<String, String> body = new HashMap<>();
        body.put("username", "admin");
        body.put("password", "password123");

        requestSpecification
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post("/auth")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    @Order(4)
    public void updateInformationFromExistingBooking() {
        RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking")
                .basePath("3")
                .contentType(ContentType.JSON)
                .accept("application/json")
                .auth().preemptive().basic("admin", "password123")
                .body("{\n" +
                        "\"firstname\" : \"James\",\n" +
                        "\"lastname\" : \"Brown\",\n" +
                        "\"totalprice\" : 111,\n" +
                        "\"depositpaid\" : true,\n" +
                        "\"bookingdates\" : {\n" +
                        "    \"checkin\" : \"2018-01-01\",\n" +
                        "    \"checkout\" : \"2019-01-01\"\n" +
                        "},\n" +
                        "\"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .when().put()
                .then().statusCode(200);
    }

    @Test
    @Order(5)
    public void updatePartOfInformation_returnOk() {
        RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking")
                .basePath("3")
                .contentType(ContentType.JSON)
                .accept("application/json")
                .auth().preemptive().basic("admin", "password123")
                .body("{\n" +
                        "\"firstname\" : \"James\",\n" +
                        "\"lastname\" : \"Brown\"\n" +
                        "}")
                .when().patch()
                .then().statusCode(200);
    }

    @Test
    @Order(6)
    public void DeleteBookingById_returnOk(){
        requestSpecification
                //.header("Authorization","Basic")
                .header("Cookie", "token=3390c953ce6c173")
                .when()
                .delete("/booking/" + user.getId())
                .then()
                .assertThat()
                .statusCode(201);
    }


}
