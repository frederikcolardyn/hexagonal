package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST response DTO for location information.
 */
public record LocationResponse(
    @JsonProperty("latitude") double latitude,
    @JsonProperty("longitude") double longitude,
    @JsonProperty("city_name") String cityName
) {
}
