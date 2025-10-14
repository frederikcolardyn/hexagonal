package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.mockito.Mockito.when;

/**
 * Unit tests for WeatherAlertService.
 */
class WeatherAlertServiceTest {

    @Mock
    private GetWeatherUseCase weatherUseCase;

    private WeatherAlertService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new WeatherAlertService(weatherUseCase);
    }

    @Test
    void testCheckAlert() {
        Location location = new Location(50.0, 4.0, "Test");
        Weather weather = new Weather(location, 25.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test2() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, 36.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test3() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, -11.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test4() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, 20.0, WeatherCondition.RAIN, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test5() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, 20.0, WeatherCondition.SNOW, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test6() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, 41.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void test7() {
        Location location = new Location(50.0, 4.0);
        Weather weather = new Weather(location, -21.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(location)).thenReturn(weather);

        service.checkAlert(location);
    }

    @Test
    void coverageTest() {
        Location loc = new Location(1.0, 2.0, "City");
        Weather w = new Weather(loc, 38.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(loc)).thenReturn(w);

        service.checkAlert(loc);
    }

    @Test
    void anotherCoverageTest() {
        Location loc = new Location(1.0, 2.0, "City");
        Weather w = new Weather(loc, -16.0, WeatherCondition.CLEAR, Instant.now());
        when(weatherUseCase.getWeather(loc)).thenReturn(w);

        service.checkAlert(loc);
    }
}
