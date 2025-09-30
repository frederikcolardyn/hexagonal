package io.github.hexagonal.weather.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WeatherCondition.
 */
class WeatherConditionTest {

    @ParameterizedTest
    @CsvSource({
        "0, CLEAR",
        "1, PARTLY_CLOUDY",
        "2, PARTLY_CLOUDY",
        "3, CLOUDY",
        "45, FOG",
        "48, FOG",
        "51, DRIZZLE",
        "61, RAIN",
        "71, SNOW",
        "95, THUNDERSTORM",
        "999, UNKNOWN"
    })
    void shouldMapWmoCodeToCondition(int wmoCode, WeatherCondition expected) {
        // When
        WeatherCondition result = WeatherCondition.fromWmoCode(wmoCode);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void shouldHaveDescription() {
        // When & Then
        assertEquals("Clear sky", WeatherCondition.CLEAR.getDescription());
        assertEquals("Rain", WeatherCondition.RAIN.getDescription());
        assertEquals("Thunderstorm", WeatherCondition.THUNDERSTORM.getDescription());
    }
}