package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * REST response DTO for a single day's forecast.
 */
public record DailyForecastResponse(
    @JsonProperty("date") LocalDate date,
    @JsonProperty("temperature_min") double temperatureMin,
    @JsonProperty("temperature_max") double temperatureMax,
    @JsonProperty("condition") String condition,
    @JsonProperty("precipitation_probability") double precipitationProbability
) {
}
