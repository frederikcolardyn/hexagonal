package io.github.hexagonal.weather.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a weather alert based on extreme conditions.
 *
 * @param location Location where alert was triggered
 * @param alertType Type of weather alert
 * @param severity Severity level of the alert
 * @param message Human-readable alert message
 */
public record WeatherAlert(
    @NotNull @Valid Location location,
    @NotNull AlertType alertType,
    @NotNull AlertSeverity severity,
    @NotNull String message
) {
}
