package io.github.hexagonal.weather.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing a geographic location.
 *
 * @param latitude  Latitude coordinate (-90 to 90)
 * @param longitude Longitude coordinate (-180 to 180)
 * @param cityName  Optional city name for display purposes
 */
@Embeddable
public record Location(
    @Min(-90) @Max(90) double latitude,
    @Min(-180) @Max(180) double longitude,
    @NotBlank String cityName
) {
    /**
     * Creates a location with coordinates only.
     * City name will be set to "Unknown".
     */
    public Location(double latitude, double longitude) {
        this(latitude, longitude, "Unknown");
    }
}