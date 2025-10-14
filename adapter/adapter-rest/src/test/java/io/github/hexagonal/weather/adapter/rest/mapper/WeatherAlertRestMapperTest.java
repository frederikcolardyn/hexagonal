package io.github.hexagonal.weather.adapter.rest.mapper;

import io.github.hexagonal.weather.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for WeatherAlertRestMapper.
 */
class WeatherAlertRestMapperTest {

    private WeatherAlertRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WeatherAlertRestMapper();
    }

    @Test
    void test() {
        Location location = new Location(50.0, 4.0, "Brussels");
        WeatherAlert alert = new WeatherAlert(location, AlertType.EXTREME_HEAT, AlertSeverity.HIGH, "Hot weather");

        mapper.toResponse(alert);
    }

    @Test
    void testMapping() {
        Location location = new Location(1.0, 2.0, "City");
        WeatherAlert alert = new WeatherAlert(location, AlertType.CLEAR, AlertSeverity.LOW, "Clear");

        mapper.toResponse(alert);
    }

    @Test
    void testDifferentAlert() {
        Location location = new Location(3.0, 4.0, "Town");
        WeatherAlert alert = new WeatherAlert(location, AlertType.SNOW_STORM, AlertSeverity.EXTREME, "Snow");

        mapper.toResponse(alert);
    }
}
