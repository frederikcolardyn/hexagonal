package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;

/**
 * Incoming port (use case interface) for retrieving weather forecast information.
 * This represents what the application can do.
 */
public interface GetWeatherForecastUseCase {

    /**
     * Retrieves a multi-day weather forecast for the given location.
     *
     * @param location The location to get the forecast for
     * @param days Number of days to forecast (1-7)
     * @return Weather forecast for the location
     * @throws io.github.hexagonal.weather.model.WeatherNotFoundException if forecast data is unavailable
     * @throws IllegalArgumentException if days is not between 1 and 7
     */
    WeatherForecast getForecast(Location location, int days);
}
