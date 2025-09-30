package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;

/**
 * Incoming port (use case interface) for retrieving weather information.
 * This represents what the application can do.
 */
public interface GetWeatherUseCase {

    /**
     * Retrieves current weather for the given location.
     *
     * @param location The location to get weather for
     * @return Current weather information
     * @throws io.github.hexagonal.weather.model.WeatherNotFoundException if weather data is unavailable
     */
    Weather getWeather(Location location);
}