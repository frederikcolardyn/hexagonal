package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherAlert;

/**
 * Use case for checking weather alerts based on current conditions.
 */
public interface CheckWeatherAlertUseCase {

    /**
     * Checks current weather and generates an alert if conditions are extreme.
     *
     * @param location Location to check weather for
     * @return Weather alert if conditions warrant it
     */
    WeatherAlert checkAlert(Location location);
}
