package com.restfulbooker;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestfulBookerTests {

    private static String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        generateToken();
    }

    // Gera token para DELETE
    private void generateToken() {
        JSONObject auth = new JSONObject();
        auth.put("username", "admin");
        auth.put("password", "password123");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(auth.toString())
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .extract().response();

        token = response.jsonPath().getString("token");
        System.out.println("Token gerado: " + token);
    }

    // Ping / HealthCheck
    @Test
    public void testPing() {
        given()
        .when()
            .get("/ping")
        .then()
            .statusCode(201);
    }

    // Get Booking IDs
    @Test
    public void testGetBookingIds() {
        given()
        .when()
            .get("/booking")
        .then()
            .statusCode(200)
            .body("bookingid", notNullValue());
    }

    // Cria um booking e retorna o ID
    private int createBooking(String firstname) {
        JSONObject booking = new JSONObject();
        booking.put("firstname", firstname);
        booking.put("lastname", "Pita");
        booking.put("totalprice", 500);
        booking.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-10-01");
        bookingDates.put("checkout", "2025-10-10");

        booking.put("bookingdates", bookingDates);
        booking.put("additionalneeds", "Breakfast");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(booking.toString())
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .extract().response();

        return response.jsonPath().getInt("bookingid");
    }

    // Get Booking
    @Test
    public void testGetBooking() {
        int bookingId = createBooking("Vinicius");
        given()
        .when()
            .get("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Vinicius"));
    }

    // Update Booking (PUT)
    @Test
    public void testUpdateBooking() {
        int bookingId = createBooking("Vinicius");

        JSONObject booking = new JSONObject();
        booking.put("firstname", "Carlos");
        booking.put("lastname", "Pita");
        booking.put("totalprice", 700);
        booking.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-11-01");
        bookingDates.put("checkout", "2025-11-10");

        booking.put("bookingdates", bookingDates);
        booking.put("additionalneeds", "Lunch");

        given()
            .auth().preemptive().basic("admin", "password123") // PUT precisa de Basic Auth
            .contentType(ContentType.JSON)
            .body(booking.toString())
        .when()
            .put("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Carlos"));
    }

    // Partial Update (PATCH)
    @Test
    public void testPartialUpdateBooking() {
        int bookingId = createBooking("Vinicius");

        JSONObject booking = new JSONObject();
        booking.put("firstname", "Lucas");

        given()
            .auth().preemptive().basic("admin", "password123") // PATCH precisa de Basic Auth
            .contentType(ContentType.JSON)
            .body(booking.toString())
        .when()
            .patch("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Lucas"));
    }

    // Delete Booking
    @Test
    public void testDeleteBooking() {
        int bookingId = createBooking("Vinicius");

        given()
            .header("Cookie", "token=" + token) // DELETE usando token
        .when()
            .delete("/booking/" + bookingId)
        .then()
            .statusCode(201);
    }
}
