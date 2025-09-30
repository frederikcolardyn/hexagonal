package io.github.hexagonal.weather.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Location value object.
 */
class LocationTest {

    @Test
    void shouldCreateLocationWithCityName() {
        // When
        Location location = new Location(50.8503, 4.3517, "Brussels");

        // Then
        assertEquals(50.8503, location.latitude());
        assertEquals(4.3517, location.longitude());
        assertEquals("Brussels", location.cityName());
    }

    @Test
    void shouldCreateLocationWithoutCityName() {
        // When
        Location location = new Location(50.8503, 4.3517);

        // Then
        assertEquals(50.8503, location.latitude());
        assertEquals(4.3517, location.longitude());
        assertEquals("Unknown", location.cityName());
    }

    @Test
    void shouldBeEqualWhenCoordinatesMatch() {
        // Given
        Location location1 = new Location(50.8503, 4.3517, "Brussels");
        Location location2 = new Location(50.8503, 4.3517, "Brussels");

        // Then
        assertEquals(location1, location2);
        assertEquals(location1.hashCode(), location2.hashCode());
    }
}