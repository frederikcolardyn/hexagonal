package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.application.port.out.WeatherProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import lombok.extern.jbosslog.JBossLog;

/**
 * Service orchestrator implementing the GetWeatherUseCase.
 * This is the application layer that coordinates domain logic.
 */
@JBossLog
public class WeatherService implements GetWeatherUseCase {

    private final WeatherProvider weatherProvider;

    public WeatherService(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    @Override
    public Weather getWeather(Location location) {
        log.infof("Fetching weather for location: %s", location);

        Weather weather = weatherProvider.fetchWeather(location);

        log.infof("Retrieved weather: %s at %s", weather.condition(), weather.temperature());

        return weather;
    }
}