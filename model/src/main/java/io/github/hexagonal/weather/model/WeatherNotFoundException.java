package io.github.hexagonal.weather.model;

/**
 * Exception thrown when weather data cannot be found for a location.
 */
public class WeatherNotFoundException extends WeatherException {

    public WeatherNotFoundException(Location location) {
        super("Weather data not found for location: " + location);
    }

    public WeatherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}