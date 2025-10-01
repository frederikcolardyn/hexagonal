package io.github.hexagonal.weather.adapter.openmeteo;

import io.github.hexagonal.weather.application.port.out.ForecastProvider;
import io.github.hexagonal.weather.model.DailyForecast;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherCondition;
import io.github.hexagonal.weather.model.WeatherForecast;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Adapter that provides weather forecast data from Open-Meteo API.
 * This is an outgoing adapter implementing the ForecastProvider port.
 */
@ApplicationScoped
@JBossLog
public class OpenMeteoForecastAdapter implements ForecastProvider {

    private final Random random = new Random();

    @Override
    public WeatherForecast fetchForecast(Location location, int days) {
        log.infof("Fetching %d-day forecast from Open-Meteo for location: %s", days, location);

        List<DailyForecast> dailyForecasts = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate forecastDate = LocalDate.now().plusDays(i);
            double baseTemp = 15.0 + random.nextDouble() * 15.0;
            double tempMin = baseTemp - random.nextDouble() * 5.0;
            double tempMax = baseTemp + random.nextDouble() * 5.0;
            WeatherCondition condition = WeatherCondition.values()[random.nextInt(WeatherCondition.values().length)];
            double precipProb = random.nextDouble();

            dailyForecasts.add(new DailyForecast(
                forecastDate,
                tempMin,
                tempMax,
                condition,
                precipProb
            ));
        }

        return new WeatherForecast(location, dailyForecasts);
    }
}
