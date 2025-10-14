package io.github.hexagonal.weather.adapter.rest.mapper;

import io.github.hexagonal.weather.adapter.rest.dto.LocationDto;
import io.github.hexagonal.weather.adapter.rest.dto.UserPreferencesRequest;
import io.github.hexagonal.weather.adapter.rest.dto.UserPreferencesResponse;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.TemperatureUnit;
import io.github.hexagonal.weather.model.UserPreferences;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for user preferences REST DTOs.
 */
@ApplicationScoped
public class UserPreferencesRestMapper {

    public UserPreferences toDomain(UserPreferencesRequest request) {
        Location location = request.defaultLocation() != null
            ? new Location(
                request.defaultLocation().latitude(),
                request.defaultLocation().longitude(),
                request.defaultLocation().cityName())
            : null;

        TemperatureUnit unit = TemperatureUnit.valueOf(request.temperatureUnit().toUpperCase());

        return new UserPreferences(
            request.userId(),
            unit,
            location,
            request.notificationsEnabled()
        );
    }

    public UserPreferencesResponse toResponse(UserPreferences preferences) {
        LocationDto locationDto = preferences.defaultLocation() != null
            ? new LocationDto(
                preferences.defaultLocation().latitude(),
                preferences.defaultLocation().longitude(),
                preferences.defaultLocation().cityName())
            : null;

        return new UserPreferencesResponse(
            preferences.userId(),
            preferences.temperatureUnit().name(),
            locationDto,
            preferences.notificationsEnabled()
        );
    }
}
