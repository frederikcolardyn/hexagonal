package io.github.hexagonal.weather.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Domain model representing current weather information.
 *
 * @param location      The location where weather was measured
 * @param temperature   Temperature in Celsius
 * @param condition     Weather condition
 * @param timestamp     When the weather data was measured
 */
public record Weather(
    @NotNull @Valid Location location,
    double temperature,
    @NotNull WeatherCondition condition,
    @NotNull Instant timestamp
) {
    /**
     * Creates weather with current timestamp.
     */
    public Weather(Location location, double temperature, WeatherCondition condition) {
        this(location, temperature, condition, Instant.now());
    }
}