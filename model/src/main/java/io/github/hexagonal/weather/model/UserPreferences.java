package io.github.hexagonal.weather.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents user preferences for weather service personalization.
 *
 * @param userId User identifier
 * @param temperatureUnit Preferred temperature unit (CELSIUS or FAHRENHEIT)
 * @param defaultLocation Default location for quick weather access
 * @param notificationsEnabled Whether to enable weather notifications
 */
public record UserPreferences(
    @NotBlank String userId,
    @NotNull TemperatureUnit temperatureUnit,
    @Valid Location defaultLocation,
    boolean notificationsEnabled
) {
    public UserPreferences(String userId) {
        this(userId, TemperatureUnit.CELSIUS, null, false);
    }
}
