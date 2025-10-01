package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.GetWeatherForecastUseCase;
import io.github.hexagonal.weather.application.port.out.ForecastProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;
import lombok.extern.jbosslog.JBossLog;

/**
 * Service orchestrator implementing the GetWeatherForecastUseCase.
 * This is the application layer that coordinates domain logic for weather forecasts.
 */
@JBossLog
public class ForecastService implements GetWeatherForecastUseCase {

    private final ForecastProvider forecastProvider;

    public ForecastService(ForecastProvider forecastProvider) {
        this.forecastProvider = forecastProvider;
    }

    @Override
    public WeatherForecast getForecast(Location location, int days) {
        if (days < 1 || days > 7) {
            throw new IllegalArgumentException("Days must be between 1 and 7");
        }

        log.infof("Fetching %d-day forecast for location: %s", days, location);

        WeatherForecast forecast = forecastProvider.fetchForecast(location, days);

        log.infof("Retrieved forecast with %d daily forecasts", forecast.dailyForecasts().size());

        return forecast;
    }
}
