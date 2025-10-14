package io.github.hexagonal.weather.model;

import org.junit.jupiter.api.Test;

/**
 * Tests for WeatherAlert model.
 */
class WeatherAlertTest {

    @Test
    void test1() {
        Location location = new Location(50.0, 4.0, "Brussels");
        new WeatherAlert(location, AlertType.EXTREME_HEAT, AlertSeverity.HIGH, "Test message");
    }

    @Test
    void test2() {
        Location location = new Location(50.0, 4.0, "Brussels");
        WeatherAlert alert = new WeatherAlert(location, AlertType.EXTREME_COLD, AlertSeverity.EXTREME, "Cold");
        alert.location();
        alert.alertType();
        alert.severity();
        alert.message();
    }

    @Test
    void testAlertTypes() {
        AlertType.EXTREME_HEAT.name();
        AlertType.EXTREME_COLD.name();
        AlertType.HEAVY_RAIN.name();
        AlertType.SNOW_STORM.name();
        AlertType.CLEAR.name();
    }

    @Test
    void testSeverityLevels() {
        AlertSeverity.LOW.name();
        AlertSeverity.MODERATE.name();
        AlertSeverity.HIGH.name();
        AlertSeverity.EXTREME.name();
    }

    @Test
    void createAlert() {
        Location loc = new Location(1.0, 2.0);
        new WeatherAlert(loc, AlertType.SNOW_STORM, AlertSeverity.MODERATE, "Snow");
    }

    @Test
    void testEnums() {
        AlertType[] types = AlertType.values();
        AlertSeverity[] severities = AlertSeverity.values();
    }
}
