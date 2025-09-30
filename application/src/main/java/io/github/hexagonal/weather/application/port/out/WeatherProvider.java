package io.github.hexagonal.weather.application.port.out;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;

/**
 * Outgoing port (repository interface) for retrieving weather data.
 * This represents what the application needs from external systems.
 * Implementations are provided by adapter modules.
 */
public interface WeatherProvider {

    /**
     * Fetches current weather from an external source.
     *
     * @param location The location to get weather for
     * @return Current weather information
     * @throws io.github.hexagonal.weather.model.WeatherException if weather data cannot be retrieved
     */
    Weather fetchWeather(Location location);
}