package io.github.hexagonal.weather.adapter.openmeteo;

import io.github.hexagonal.weather.adapter.openmeteo.client.OpenMeteoClient;
import io.github.hexagonal.weather.adapter.openmeteo.dto.OpenMeteoResponse;
import io.github.hexagonal.weather.adapter.openmeteo.mapper.OpenMeteoMapper;
import io.github.hexagonal.weather.application.port.out.WeatherProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import io.github.hexagonal.weather.model.WeatherNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Outgoing adapter that implements WeatherProvider using the Open-Meteo API.
 * This adapter translates between the application's domain model and the external API.
 */
@ApplicationScoped
@JBossLog
public class OpenMeteoAdapter implements WeatherProvider {

    private final OpenMeteoClient client;
    private final OpenMeteoMapper mapper;

    @Inject
    public OpenMeteoAdapter(@RestClient OpenMeteoClient client, OpenMeteoMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Weather fetchWeather(Location location) {
        try {
            log.infof("Fetching weather from Open-Meteo API for: lat=%f, lon=%f",
                location.latitude(), location.longitude());

            OpenMeteoResponse response = client.getCurrentWeather(
                location.latitude(),
                location.longitude(),
                "temperature_2m,weather_code"
            );

            Weather weather = mapper.toDomain(response, location);

            log.infof("Successfully fetched weather: %s at %s°C",
                weather.condition(), weather.temperature());

            return weather;
        } catch (Exception e) {
            log.errorf(e, "Failed to fetch weather for location: %s", location);
            throw new WeatherNotFoundException("Unable to fetch weather data", e);
        }
    }
}