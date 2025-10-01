package io.github.hexagonal.weather.bootstrap.config;

import io.github.hexagonal.weather.application.port.in.GetWeatherForecastUseCase;
import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.application.port.out.ForecastProvider;
import io.github.hexagonal.weather.application.port.out.WeatherProvider;
import io.github.hexagonal.weather.application.service.ForecastService;
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

    /**
     * Produces the GetWeatherForecastUseCase bean.
     * The ForecastProvider is automatically injected by CDI from the adapter module.
     */
    @Produces
    @ApplicationScoped
    public GetWeatherForecastUseCase getWeatherForecastUseCase(ForecastProvider forecastProvider) {
        return new ForecastService(forecastProvider);
    }
}