package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserPreferencesRequest(
    @JsonProperty("user_id") String userId,
    @JsonProperty("temperature_unit") String temperatureUnit,
    @JsonProperty("default_location") LocationDto defaultLocation,
    @JsonProperty("notifications_enabled") boolean notificationsEnabled
) {
}
