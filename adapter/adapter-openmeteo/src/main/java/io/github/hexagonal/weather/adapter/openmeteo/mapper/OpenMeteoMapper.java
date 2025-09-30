package io.github.hexagonal.weather.adapter.openmeteo.mapper;

import io.github.hexagonal.weather.adapter.openmeteo.dto.OpenMeteoResponse;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import io.github.hexagonal.weather.model.WeatherCondition;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Mapper for converting Open-Meteo API responses to domain models.
 */
@ApplicationScoped
public class OpenMeteoMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Converts Open-Meteo response to Weather domain model.
     *
     * @param response The API response
     * @param location The location (with optional city name)
     * @return Weather domain model
     */
    public Weather toDomain(OpenMeteoResponse response, Location location) {
        WeatherCondition condition = WeatherCondition.fromWmoCode(
            response.current().weatherCode()
        );

        // Parse the timestamp (format: "2025-09-30T12:45") and convert to Instant
        LocalDateTime localDateTime = LocalDateTime.parse(response.current().time(), FORMATTER);
        Instant timestamp = localDateTime.toInstant(ZoneOffset.UTC);

        return new Weather(
            location,
            response.current().temperature(),
            condition,
            timestamp
        );
    }
}