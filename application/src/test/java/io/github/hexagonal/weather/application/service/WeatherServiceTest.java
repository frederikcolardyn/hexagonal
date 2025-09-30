package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.out.WeatherProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import io.github.hexagonal.weather.model.WeatherCondition;
import io.github.hexagonal.weather.model.WeatherNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WeatherService.
 * Tests business logic in isolation using mocks.
 */
@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherProvider weatherProvider;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(weatherProvider);
    }

    @Test
    void shouldFetchWeatherSuccessfully() {
        // Given
        Location location = new Location(50.8503, 4.3517, "Brussels");
        Weather expectedWeather = new Weather(
            location,
            20.5,
            WeatherCondition.PARTLY_CLOUDY,
            Instant.now()
        );

        when(weatherProvider.fetchWeather(location)).thenReturn(expectedWeather);

        // When
        Weather result = weatherService.getWeather(location);

        // Then
        assertNotNull(result);
        assertEquals(expectedWeather, result);
        assertEquals(20.5, result.temperature());
        assertEquals(WeatherCondition.PARTLY_CLOUDY, result.condition());
        verify(weatherProvider, times(1)).fetchWeather(location);
    }

    @Test
    void shouldPropagateExceptionWhenProviderFails() {
        // Given
        Location location = new Location(50.8503, 4.3517, "Brussels");
        when(weatherProvider.fetchWeather(location))
            .thenThrow(new WeatherNotFoundException(location));

        // When & Then
        assertThrows(WeatherNotFoundException.class, () -> weatherService.getWeather(location));
        verify(weatherProvider, times(1)).fetchWeather(location);
    }

    @Test
    void shouldHandleLocationWithoutCityName() {
        // Given
        Location location = new Location(50.8503, 4.3517);
        Weather expectedWeather = new Weather(
            location,
            18.0,
            WeatherCondition.CLEAR,
            Instant.now()
        );

        when(weatherProvider.fetchWeather(location)).thenReturn(expectedWeather);

        // When
        Weather result = weatherService.getWeather(location);

        // Then
        assertNotNull(result);
        assertEquals("Unknown", result.location().cityName());
        verify(weatherProvider, times(1)).fetchWeather(location);
    }
}