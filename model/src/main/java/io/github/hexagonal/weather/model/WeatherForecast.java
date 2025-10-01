package io.github.hexagonal.weather.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Domain model representing a multi-day weather forecast for a location.
 *
 * @param location          The location for which the forecast is provided
 * @param dailyForecasts    List of daily forecasts
 */
public record WeatherForecast(
    @NotNull @Valid Location location,
    @NotNull List<@Valid DailyForecast> dailyForecasts
) {
    public WeatherForecast {
        if (dailyForecasts == null || dailyForecasts.isEmpty()) {
            throw new IllegalArgumentException("Daily forecasts list cannot be null or empty");
        }
    }
}
