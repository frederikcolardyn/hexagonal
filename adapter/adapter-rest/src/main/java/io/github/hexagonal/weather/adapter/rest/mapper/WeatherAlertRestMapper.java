package io.github.hexagonal.weather.adapter.rest.mapper;

import io.github.hexagonal.weather.adapter.rest.dto.LocationResponse;
import io.github.hexagonal.weather.adapter.rest.dto.WeatherAlertResponse;
import io.github.hexagonal.weather.model.WeatherAlert;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for weather alert REST DTOs.
 */
@ApplicationScoped
public class WeatherAlertRestMapper {

    public WeatherAlertResponse toResponse(WeatherAlert alert) {
        LocationResponse location = new LocationResponse(
            alert.location().latitude(),
            alert.location().longitude(),
            alert.location().cityName()
        );

        return new WeatherAlertResponse(
            location,
            alert.alertType().name(),
            alert.severity().name(),
            alert.message()
        );
    }
}
