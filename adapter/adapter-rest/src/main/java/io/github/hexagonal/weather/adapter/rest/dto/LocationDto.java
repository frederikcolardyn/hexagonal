package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationDto(
    double latitude,
    double longitude,
    @JsonProperty("city_name") String cityName
) {
}
