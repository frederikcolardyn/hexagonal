package io.github.hexagonal.weather.bootstrap.config;

import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.application.port.out.WeatherProvider;
import io.github.hexagonal.weather.application.service.WeatherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * CDI configuration for wiring application components.
 * This demonstrates the dependency inversion principle:
 * - The application layer defines interfaces (ports)
 * - Adapters implement those interfaces
 * - This config wires them together
 */
@ApplicationScoped
public class ApplicationConfig {

    /**
     * Produces the GetWeatherUseCase bean.
     * The WeatherProvider is automatically injected by CDI from the adapter module.
     */
    @Produces
    @ApplicationScoped
    public GetWeatherUseCase getWeatherUseCase(WeatherProvider weatherProvider) {
        return new WeatherService(weatherProvider);
    }
}