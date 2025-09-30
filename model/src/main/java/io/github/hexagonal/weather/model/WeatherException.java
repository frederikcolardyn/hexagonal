package io.github.hexagonal.weather.model;

/**
 * Base exception for weather-related domain errors.
 */
public class WeatherException extends RuntimeException {

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}