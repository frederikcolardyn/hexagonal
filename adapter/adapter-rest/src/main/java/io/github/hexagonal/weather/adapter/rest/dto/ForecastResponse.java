package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * REST response DTO for weather forecast.
 */
public record ForecastResponse(
    @JsonProperty("location") LocationResponse location,
    @JsonProperty("daily_forecasts") List<DailyForecastResponse> dailyForecasts
) {
}
