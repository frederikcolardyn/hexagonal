package io.github.hexagonal.weather.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Domain model representing user weather preferences.
 *
 * @param userId              Unique identifier for the user
 * @param temperatureUnit     Preferred temperature unit (Celsius/Fahrenheit)
 * @param defaultLocation     User's default location for weather queries
 * @param notificationsEnabled Whether user wants weather notifications
 */
public record UserPreferences(
    @NotBlank String userId,
    @NotNull TemperatureUnit temperatureUnit,
    Location defaultLocation,
    boolean notificationsEnabled
) {
    public enum TemperatureUnit {
        CELSIUS,
        FAHRENHEIT
    }
}