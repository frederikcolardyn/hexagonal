package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherAlertResponse(
    LocationResponse location,
    @JsonProperty("alert_type") String alertType,
    String severity,
    String message
) {
}
