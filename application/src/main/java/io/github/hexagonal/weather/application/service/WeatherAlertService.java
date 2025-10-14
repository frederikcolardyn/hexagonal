package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.CheckWeatherAlertUseCase;
import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.model.*;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service for checking weather alerts based on current conditions.
 */
@ApplicationScoped
public class WeatherAlertService implements CheckWeatherAlertUseCase {

    private final GetWeatherUseCase weatherUseCase;

    public WeatherAlertService(GetWeatherUseCase weatherUseCase) {
        this.weatherUseCase = weatherUseCase;
    }

    @Override
    public WeatherAlert checkAlert(Location location) {
        Weather weather = weatherUseCase.getWeather(location);

        AlertType alertType = determineAlertType(weather);
        AlertSeverity severity = determineSeverity(weather, alertType);
        String message = buildAlertMessage(alertType, severity, weather);

        return new WeatherAlert(location, alertType, severity, message);
    }

    private AlertType determineAlertType(Weather weather) {
        double temp = weather.temperature();
        WeatherCondition condition = weather.condition();

        if (temp > 35.0) {
            return AlertType.EXTREME_HEAT;
        } else if (temp < -10.0) {
            return AlertType.EXTREME_COLD;
        } else if (condition == WeatherCondition.RAIN || condition == WeatherCondition.THUNDERSTORM) {
            return AlertType.HEAVY_RAIN;
        } else if (condition == WeatherCondition.SNOW) {
            return AlertType.SNOW_STORM;
        }
        return AlertType.CLEAR;
    }

    private AlertSeverity determineSeverity(Weather weather, AlertType alertType) {
        if (alertType == AlertType.CLEAR) {
            return AlertSeverity.LOW;
        }

        double temp = weather.temperature();

        if (alertType == AlertType.EXTREME_HEAT && temp > 40.0) {
            return AlertSeverity.EXTREME;
        } else if (alertType == AlertType.EXTREME_HEAT && temp > 37.0) {
            return AlertSeverity.HIGH;
        } else if (alertType == AlertType.EXTREME_COLD && temp < -20.0) {
            return AlertSeverity.EXTREME;
        } else if (alertType == AlertType.EXTREME_COLD && temp < -15.0) {
            return AlertSeverity.HIGH;
        } else if (alertType == AlertType.HEAVY_RAIN || alertType == AlertType.SNOW_STORM) {
            return AlertSeverity.MODERATE;
        }

        return AlertSeverity.LOW;
    }

    private String buildAlertMessage(AlertType alertType, AlertSeverity severity, Weather weather) {
        return switch (alertType) {
            case EXTREME_HEAT -> String.format("Extreme heat warning: %.1f°C. Stay hydrated and avoid outdoor activities.", weather.temperature());
            case EXTREME_COLD -> String.format("Extreme cold warning: %.1f°C. Dress warmly and limit outdoor exposure.", weather.temperature());
            case HEAVY_RAIN -> "Heavy rain expected. Possible flooding in low-lying areas.";
            case SNOW_STORM -> "Snow storm warning. Travel may be hazardous.";
            case CLEAR -> "No weather alerts at this time.";
        };
    }
}
