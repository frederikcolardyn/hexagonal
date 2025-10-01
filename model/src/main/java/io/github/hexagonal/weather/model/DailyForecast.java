package io.github.hexagonal.weather.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Domain model representing a single day's weather forecast.
 *
 * @param date              The date of the forecast
 * @param temperatureMin    Minimum temperature in Celsius
 * @param temperatureMax    Maximum temperature in Celsius
 * @param condition         Predicted weather condition
 * @param precipitationProbability Probability of precipitation (0.0 to 1.0)
 */
public record DailyForecast(
    @NotNull LocalDate date,
    double temperatureMin,
    double temperatureMax,
    @NotNull WeatherCondition condition,
    double precipitationProbability
) {
    public DailyForecast {
        if (precipitationProbability < 0.0 || precipitationProbability > 1.0) {
            throw new IllegalArgumentException("Precipitation probability must be between 0.0 and 1.0");
        }
        if (temperatureMin > temperatureMax) {
            throw new IllegalArgumentException("Minimum temperature cannot be higher than maximum temperature");
        }
    }
}
