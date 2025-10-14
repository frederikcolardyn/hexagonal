package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationResponse(
    double latitude,
    double longitude,
    @JsonProperty("city_name") String cityName
) {
}
