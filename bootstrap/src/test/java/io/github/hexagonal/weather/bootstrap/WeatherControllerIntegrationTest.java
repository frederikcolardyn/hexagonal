package io.github.hexagonal.weather.bootstrap;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Integration test for the Weather REST API.
 * Uses @QuarkusTest to start the application and test end-to-end.
 */
@QuarkusTest
class WeatherControllerIntegrationTest {

    @Test
    void shouldGetWeatherForBrussels() {
        given()
            .queryParam("lat", 50.8503)
            .queryParam("lon", 4.3517)
            .queryParam("city", "Brussels")
            .when()
            .get("/weather")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("location.latitude", is(50.8503f))
            .body("location.longitude", is(4.3517f))
            .body("location.city_name", is("Brussels"))
            .body("temperature_celsius", notNullValue())
            .body("condition", notNullValue())
            .body("condition_description", notNullValue())
            .body("timestamp", notNullValue());
    }

    @Test
    void shouldGetWeatherWithoutCityName() {
        given()
            .queryParam("lat", 51.5074)
            .queryParam("lon", -0.1278)
            .when()
            .get("/weather")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("location.latitude", is(51.5074f))
            .body("location.longitude", is(-0.1278f))
            .body("location.city_name", is("Unknown"))
            .body("temperature_celsius", notNullValue());
    }

    @Test
    void shouldGetWeatherForMultipleLocations() {
        // Test Paris
        given()
            .queryParam("lat", 48.8566)
            .queryParam("lon", 2.3522)
            .queryParam("city", "Paris")
            .when()
            .get("/weather")
            .then()
            .statusCode(200)
            .body("location.city_name", is("Paris"));

        // Test Berlin
        given()
            .queryParam("lat", 52.5200)
            .queryParam("lon", 13.4050)
            .queryParam("city", "Berlin")
            .when()
            .get("/weather")
            .then()
            .statusCode(200)
            .body("location.city_name", is("Berlin"));
    }
}