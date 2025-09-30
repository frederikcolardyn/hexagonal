package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST response DTO for weather information.
 */
public record WeatherResponse(
    @JsonProperty("location") LocationDto location,
    @JsonProperty("temperature_celsius") double temperatureCelsius,
    @JsonProperty("condition") String condition,
    @JsonProperty("condition_description") String conditionDescription,
    @JsonProperty("timestamp") String timestamp
) {
    public record LocationDto(
        @JsonProperty("latitude") double latitude,
        @JsonProperty("longitude") double longitude,
        @JsonProperty("city_name") String cityName
    ) {}
}