package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ForecastResponse DTO.
 * Testing framework: JUnit 5 (Jupiter)
 */
@DisplayName("ForecastResponse Tests")
class ForecastResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static LocationResponse sampleLocation(double latitude, double longitude, String cityName) {
        return new LocationResponse(latitude, longitude, cityName);
    }

    private static DailyForecastResponse sampleForecast(
        LocalDate date,
        double minTemperature,
        double maxTemperature,
        String condition,
        double precipitationProbability
    ) {
        return new DailyForecastResponse(date, minTemperature, maxTemperature, condition, precipitationProbability);
    }

    @Nested
    @DisplayName("Constructor and Accessor Behavior")
    class ConstructorAndAccessorTests {

        @Test
        @DisplayName("Should retain provided location and forecasts")
        void shouldRetainProvidedValues() {
            LocationResponse location = sampleLocation(40.7128, -74.0060, "New York");
            List<DailyForecastResponse> forecasts = List.of(
                sampleForecast(LocalDate.of(2025, 10, 1), 14.0, 21.0, "Sunny", 10.0),
                sampleForecast(LocalDate.of(2025, 10, 2), 13.0, 20.0, "Cloudy", 25.0)
            );

            ForecastResponse response = new ForecastResponse(location, forecasts);

            assertSame(location, response.location());
            assertSame(forecasts, response.dailyForecasts());
            assertEquals(2, response.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should allow null location")
        void shouldAllowNullLocation() {
            List<DailyForecastResponse> forecasts = List.of(
                sampleForecast(LocalDate.of(2025, 10, 1), 11.0, 18.0, "Rain", 60.0)
            );

            ForecastResponse response = new ForecastResponse(null, forecasts);

            assertNull(response.location());
            assertEquals(forecasts, response.dailyForecasts());
        }

        @Test
        @DisplayName("Should allow null daily forecasts")
        void shouldAllowNullDailyForecasts() {
            LocationResponse location = sampleLocation(51.5074, -0.1278, "London");

            ForecastResponse response = new ForecastResponse(location, null);

            assertSame(location, response.location());
            assertNull(response.dailyForecasts());
        }

        @Test
        @DisplayName("Should handle empty daily forecasts")
        void shouldHandleEmptyDailyForecasts() {
            LocationResponse location = sampleLocation(48.8566, 2.3522, "Paris");
            List<DailyForecastResponse> emptyForecasts = Collections.emptyList();

            ForecastResponse response = new ForecastResponse(location, emptyForecasts);

            assertSame(location, response.location());
            assertTrue(response.dailyForecasts().isEmpty());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("Should follow record equality contract for identical data")
        void shouldFollowRecordEqualityContract() {
            ForecastResponse response1 = new ForecastResponse(
                sampleLocation(35.6895, 139.6917, "Tokyo"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 18.0, 27.0, "Clear", 5.0))
            );
            ForecastResponse response2 = new ForecastResponse(
                sampleLocation(35.6895, 139.6917, "Tokyo"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 18.0, 27.0, "Clear", 5.0))
            );

            assertEquals(response1, response2);
            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Should detect inequality when location differs")
        void shouldDetectInequalityByLocation() {
            ForecastResponse baseline = new ForecastResponse(
                sampleLocation(59.3293, 18.0686, "Stockholm"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 7.0, 15.0, "Cloudy", 20.0))
            );
            ForecastResponse differentLocation = new ForecastResponse(
                sampleLocation(60.1699, 24.9384, "Helsinki"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 7.0, 15.0, "Cloudy", 20.0))
            );

            assertNotEquals(baseline, differentLocation);
        }

        @Test
        @DisplayName("Should detect inequality when forecasts differ")
        void shouldDetectInequalityByForecasts() {
            ForecastResponse baseline = new ForecastResponse(
                sampleLocation(34.0522, -118.2437, "Los Angeles"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 16.0, 26.0, "Sunny", 0.0))
            );
            ForecastResponse differentForecasts = new ForecastResponse(
                sampleLocation(34.0522, -118.2437, "Los Angeles"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 2), 17.0, 27.0, "Windy", 10.0))
            );

            assertNotEquals(baseline, differentForecasts);
        }

        @Test
        @DisplayName("Should not equal null or different type")
        void shouldNotEqualNullOrDifferentType() {
            ForecastResponse response = new ForecastResponse(
                sampleLocation(41.9028, 12.4964, "Rome"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 10.0, 19.0, "Showers", 45.0))
            );

            assertNotEquals(response, null);
            assertNotEquals(response, "ForecastResponse");
        }
    }

    @Nested
    @DisplayName("toString Output")
    class ToStringTests {

        @Test
        @DisplayName("Should include component names in toString output")
        void shouldIncludeComponentNamesInToString() {
            ForecastResponse response = new ForecastResponse(
                sampleLocation(52.5200, 13.4050, "Berlin"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 9.0, 17.0, "Foggy", 35.0))
            );

            String representation = response.toString();

            assertNotNull(representation);
            assertTrue(representation.contains("location="));
            assertTrue(representation.contains("dailyForecasts="));
        }
    }

    @Nested
    @DisplayName("Jackson JSON Interoperability")
    class JsonTests {

        @Test
        @DisplayName("Should serialize using snake_case property names")
        void shouldSerializeUsingSnakeCasePropertyNames() throws Exception {
            ForecastResponse response = new ForecastResponse(
                sampleLocation(38.7223, -9.1393, "Lisbon"),
                List.of(sampleForecast(LocalDate.of(2025, 10, 1), 14.0, 23.0, "Sunny", 10.0))
            );

            String json = objectMapper.writeValueAsString(response);

            assertTrue(json.contains("\"location\""));
            assertTrue(json.contains("\"city_name\""));
            assertTrue(json.contains("\"daily_forecasts\""));
            assertTrue(json.contains("\"temperature_min\""));
            assertTrue(json.contains("\"temperature_max\""));
            assertTrue(json.contains("\"precipitation_probability\""));
            assertFalse(json.contains("dailyForecasts"));
        }

        @Test
        @DisplayName("Should deserialize from snake_case JSON payload")
        void shouldDeserializeFromSnakeCaseJson() throws Exception {
            String json = """
                {
                  "location": {
                    "latitude": 51.5074,
                    "longitude": -0.1278,
                    "city_name": "London"
                  },
                  "daily_forecasts": [
                    {
                      "date": "2025-10-01",
                      "temperature_min": 11.0,
                      "temperature_max": 19.0,
                      "condition": "Rain",
                      "precipitation_probability": 65.0
                    }
                  ]
                }
                """;

            ForecastResponse response = objectMapper.readValue(json, ForecastResponse.class);

            assertNotNull(response);
            assertEquals(51.5074, response.location().latitude(), 0.0001);
            assertEquals(-0.1278, response.location().longitude(), 0.0001);
            assertEquals("London", response.location().cityName());
            assertEquals(1, response.dailyForecasts().size());

            DailyForecastResponse forecast = response.dailyForecasts().get(0);
            assertEquals(LocalDate.of(2025, 10, 1), forecast.date());
            assertEquals(11.0, forecast.temperatureMin(), 0.0001);
            assertEquals(19.0, forecast.temperatureMax(), 0.0001);
            assertEquals("Rain", forecast.condition());
            assertEquals(65.0, forecast.precipitationProbability(), 0.0001);
        }

        @Test
        @DisplayName("Should round-trip serialize and deserialize without data loss")
        void shouldRoundTripSerializeAndDeserialize() throws Exception {
            ForecastResponse original = new ForecastResponse(
                sampleLocation(45.4642, 9.1900, "Milan"),
                List.of(
                    sampleForecast(LocalDate.of(2025, 10, 1), 12.0, 20.0, "Cloudy", 30.0),
                    sampleForecast(LocalDate.of(2025, 10, 2), 13.0, 21.0, "Partly Cloudy", 20.0)
                )
            );

            String json = objectMapper.writeValueAsString(original);
            ForecastResponse restored = objectMapper.readValue(json, ForecastResponse.class);

            assertEquals(original, restored);
        }
    }
}