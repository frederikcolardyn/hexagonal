package io.github.hexagonal.weather.application.port.out;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;

/**
 * Outgoing port (repository interface) for retrieving weather forecast data.
 * This represents what the application needs from external systems.
 * Implementations are provided by adapter modules.
 */
public interface ForecastProvider {

    /**
     * Fetches weather forecast from an external source.
     *
     * @param location The location to get the forecast for
     * @param days Number of days to forecast
     * @return Weather forecast information
     * @throws io.github.hexagonal.weather.model.WeatherException if forecast data cannot be retrieved
     */
    WeatherForecast fetchForecast(Location location, int days);
}
