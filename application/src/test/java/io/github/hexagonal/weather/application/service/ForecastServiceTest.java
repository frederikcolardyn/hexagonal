package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.out.ForecastProvider;
import io.github.hexagonal.weather.model.DailyForecast;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for ForecastService.
 * Tests cover happy paths, edge cases, boundary conditions, and error scenarios.
 * 
 * Testing Framework: JUnit 5 (Jupiter)
 * Mocking Framework: Mockito
 */
@DisplayName("ForecastService Unit Tests")
class ForecastServiceTest {

    @Mock
    private ForecastProvider forecastProvider;

    private ForecastService forecastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        forecastService = new ForecastService(forecastProvider);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ForecastService with valid ForecastProvider")
        void shouldCreateServiceWithValidProvider() {
            // Arrange & Act
            ForecastService service = new ForecastService(forecastProvider);

            // Assert
            assertNotNull(service, "ForecastService should not be null");
        }

        @Test
        @DisplayName("Should accept null ForecastProvider without throwing exception during construction")
        void shouldAcceptNullProviderDuringConstruction() {
            // Arrange & Act & Assert
            assertDoesNotThrow(() -> new ForecastService(null),
                    "Constructor should not throw exception with null provider");
        }
    }

    @Nested
    @DisplayName("Happy Path Tests - Valid Inputs")
    class HappyPathTests {

        @Test
        @DisplayName("Should successfully fetch 1-day forecast for valid location")
        void shouldFetchOneDayForecast() {
            // Arrange
            Location location = new Location("New York", "US", 40.7128, -74.0060);
            int days = 1;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Forecast should not be null");
            assertEquals(expectedForecast, result, "Should return the forecast from provider");
            assertEquals(1, result.dailyForecasts().size(), "Should have 1 daily forecast");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should successfully fetch 7-day forecast for valid location")
        void shouldFetchSevenDayForecast() {
            // Arrange
            Location location = new Location("London", "UK", 51.5074, -0.1278);
            int days = 7;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Forecast should not be null");
            assertEquals(expectedForecast, result, "Should return the forecast from provider");
            assertEquals(7, result.dailyForecasts().size(), "Should have 7 daily forecasts");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should successfully fetch 3-day forecast (mid-range)")
        void shouldFetchMidRangeForecast() {
            // Arrange
            Location location = new Location("Tokyo", "JP", 35.6762, 139.6503);
            int days = 3;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Forecast should not be null");
            assertEquals(expectedForecast, result, "Should return the forecast from provider");
            assertEquals(3, result.dailyForecasts().size(), "Should have 3 daily forecasts");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
        @DisplayName("Should successfully fetch forecast for all valid day values (1-7)")
        void shouldFetchForecastForAllValidDays(int days) {
            // Arrange
            Location location = new Location("Paris", "FR", 48.8566, 2.3522);
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Forecast should not be null");
            assertEquals(expectedForecast, result, "Should return the forecast from provider");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is 0")
        void shouldThrowExceptionWhenDaysIsZero() {
            // Arrange
            Location location = new Location("Berlin", "DE", 52.5200, 13.4050);
            int days = 0;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for days = 0"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is negative")
        void shouldThrowExceptionWhenDaysIsNegative() {
            // Arrange
            Location location = new Location("Sydney", "AU", -33.8688, 151.2093);
            int days = -1;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for negative days"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is 8")
        void shouldThrowExceptionWhenDaysIsEight() {
            // Arrange
            Location location = new Location("Toronto", "CA", 43.6532, -79.3832);
            int days = 8;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for days = 8"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is very large")
        void shouldThrowExceptionWhenDaysIsVeryLarge() {
            // Arrange
            Location location = new Location("Mumbai", "IN", 19.0760, 72.8777);
            int days = 100;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for days = 100"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is Integer.MIN_VALUE")
        void shouldThrowExceptionWhenDaysIsMinValue() {
            // Arrange
            Location location = new Location("Singapore", "SG", 1.3521, 103.8198);
            int days = Integer.MIN_VALUE;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for Integer.MIN_VALUE"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when days is Integer.MAX_VALUE")
        void shouldThrowExceptionWhenDaysIsMaxValue() {
            // Arrange
            Location location = new Location("Dubai", "AE", 25.2048, 55.2708);
            int days = Integer.MAX_VALUE;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should throw IllegalArgumentException for Integer.MAX_VALUE"
            );

            assertEquals("Days must be between 1 and 7", exception.getMessage());
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }
    }

    @Nested
    @DisplayName("Location Parameter Tests")
    class LocationParameterTests {

        @Test
        @DisplayName("Should handle location with special characters in city name")
        void shouldHandleLocationWithSpecialCharacters() {
            // Arrange
            Location location = new Location("São Paulo", "BR", -23.5505, -46.6333);
            int days = 5;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle special characters in location name");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle location with extreme coordinates")
        void shouldHandleLocationWithExtremeCoordinates() {
            // Arrange
            Location location = new Location("North Pole", "XX", 90.0, 0.0);
            int days = 3;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle extreme latitude coordinates");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle location with negative coordinates")
        void shouldHandleLocationWithNegativeCoordinates() {
            // Arrange
            Location location = new Location("Antarctica", "AQ", -82.8628, 135.0000);
            int days = 2;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle negative coordinates");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle null location by delegating to provider")
        void shouldHandleNullLocation() {
            // Arrange
            Location location = null;
            int days = 3;

            // Act & Assert - The service itself doesn't validate null location
            // The provider or downstream components should handle this
            assertDoesNotThrow(() -> forecastService.getForecast(location, days),
                    "Service should delegate null location handling to provider");
        }
    }

    @Nested
    @DisplayName("ForecastProvider Integration Tests")
    class ForecastProviderIntegrationTests {

        @Test
        @DisplayName("Should propagate exception when provider throws RuntimeException")
        void shouldPropagateProviderRuntimeException() {
            // Arrange
            Location location = new Location("Madrid", "ES", 40.4168, -3.7038);
            int days = 5;
            RuntimeException expectedException = new RuntimeException("Provider connection failed");

            when(forecastProvider.fetchForecast(location, days)).thenThrow(expectedException);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should propagate provider exceptions"
            );

            assertEquals("Provider connection failed", exception.getMessage());
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle provider returning empty forecast list")
        void shouldHandleEmptyForecastList() {
            // Arrange
            Location location = new Location("Rome", "IT", 41.9028, 12.4964);
            int days = 4;
            WeatherForecast emptyForecast = new WeatherForecast(
                    location,
                    Collections.emptyList()
            );

            when(forecastProvider.fetchForecast(location, days)).thenReturn(emptyForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Result should not be null");
            assertTrue(result.dailyForecasts().isEmpty(), "Should handle empty forecast list");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle provider returning null forecast")
        void shouldHandleNullForecast() {
            // Arrange
            Location location = new Location("Amsterdam", "NL", 52.3676, 4.9041);
            int days = 2;

            when(forecastProvider.fetchForecast(location, days)).thenReturn(null);

            // Act & Assert
            assertThrows(
                    NullPointerException.class,
                    () -> forecastService.getForecast(location, days),
                    "Should fail when provider returns null (NPE when accessing dailyForecasts)"
            );

            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle provider returning fewer forecasts than requested")
        void shouldHandleFewerForecastsThanRequested() {
            // Arrange
            Location location = new Location("Vienna", "AT", 48.2082, 16.3738);
            int days = 7;
            WeatherForecast partialForecast = createMockWeatherForecast(location, 3); // Only 3 instead of 7

            when(forecastProvider.fetchForecast(location, days)).thenReturn(partialForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Result should not be null");
            assertEquals(3, result.dailyForecasts().size(), "Should return whatever provider gives");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle provider returning more forecasts than requested")
        void shouldHandleMoreForecastsThanRequested() {
            // Arrange
            Location location = new Location("Brussels", "BE", 50.8503, 4.3517);
            int days = 3;
            WeatherForecast extraForecast = createMockWeatherForecast(location, 10); // More than requested

            when(forecastProvider.fetchForecast(location, days)).thenReturn(extraForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Result should not be null");
            assertEquals(10, result.dailyForecasts().size(), "Should return whatever provider gives");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }
    }

    @Nested
    @DisplayName("Multiple Invocation Tests")
    class MultipleInvocationTests {

        @Test
        @DisplayName("Should handle multiple sequential calls with same location")
        void shouldHandleMultipleCallsSameLocation() {
            // Arrange
            Location location = new Location("Stockholm", "SE", 59.3293, 18.0686);
            int days1 = 3;
            int days2 = 5;
            WeatherForecast forecast1 = createMockWeatherForecast(location, days1);
            WeatherForecast forecast2 = createMockWeatherForecast(location, days2);

            when(forecastProvider.fetchForecast(location, days1)).thenReturn(forecast1);
            when(forecastProvider.fetchForecast(location, days2)).thenReturn(forecast2);

            // Act
            WeatherForecast result1 = forecastService.getForecast(location, days1);
            WeatherForecast result2 = forecastService.getForecast(location, days2);

            // Assert
            assertNotNull(result1, "First result should not be null");
            assertNotNull(result2, "Second result should not be null");
            assertEquals(days1, result1.dailyForecasts().size());
            assertEquals(days2, result2.dailyForecasts().size());
            verify(forecastProvider, times(1)).fetchForecast(location, days1);
            verify(forecastProvider, times(1)).fetchForecast(location, days2);
        }

        @Test
        @DisplayName("Should handle multiple sequential calls with different locations")
        void shouldHandleMultipleCallsDifferentLocations() {
            // Arrange
            Location location1 = new Location("Oslo", "NO", 59.9139, 10.7522);
            Location location2 = new Location("Copenhagen", "DK", 55.6761, 12.5683);
            int days = 3;
            WeatherForecast forecast1 = createMockWeatherForecast(location1, days);
            WeatherForecast forecast2 = createMockWeatherForecast(location2, days);

            when(forecastProvider.fetchForecast(location1, days)).thenReturn(forecast1);
            when(forecastProvider.fetchForecast(location2, days)).thenReturn(forecast2);

            // Act
            WeatherForecast result1 = forecastService.getForecast(location1, days);
            WeatherForecast result2 = forecastService.getForecast(location2, days);

            // Assert
            assertNotNull(result1, "First result should not be null");
            assertNotNull(result2, "Second result should not be null");
            verify(forecastProvider, times(1)).fetchForecast(location1, days);
            verify(forecastProvider, times(1)).fetchForecast(location2, days);
        }
    }

    @Nested
    @DisplayName("Service Behavior Tests")
    class ServiceBehaviorTests {

        @Test
        @DisplayName("Should validate days before calling provider")
        void shouldValidateDaysBeforeCallingProvider() {
            // Arrange
            Location location = new Location("Helsinki", "FI", 60.1695, 24.9354);
            int invalidDays = 10;

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> forecastService.getForecast(location, invalidDays),
                    "Should validate days before provider call"
            );

            // Verify provider was never called due to early validation
            verify(forecastProvider, never()).fetchForecast(any(), anyInt());
        }

        @Test
        @DisplayName("Should call provider exactly once per valid request")
        void shouldCallProviderExactlyOnce() {
            // Arrange
            Location location = new Location("Lisbon", "PT", 38.7223, -9.1393);
            int days = 4;
            WeatherForecast forecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(forecast);

            // Act
            forecastService.getForecast(location, days);

            // Assert
            verify(forecastProvider, times(1)).fetchForecast(location, days);
            verifyNoMoreInteractions(forecastProvider);
        }

        @Test
        @DisplayName("Should return the exact forecast object from provider without modification")
        void shouldReturnUnmodifiedForecastFromProvider() {
            // Arrange
            Location location = new Location("Warsaw", "PL", 52.2297, 21.0122);
            int days = 3;
            WeatherForecast expectedForecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(expectedForecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertSame(expectedForecast, result, "Should return the exact same object reference from provider");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle location with very long city name")
        void shouldHandleVeryLongCityName() {
            // Arrange
            String longCityName = "Taumatawhakatangihangakoauauotamateaturipukakapikimaungahoronukupokaiwhenuakitanatahu";
            Location location = new Location(longCityName, "NZ", -40.3569, 176.5369);
            int days = 2;
            WeatherForecast forecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(forecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle very long city names");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle location with empty string city name")
        void shouldHandleEmptyStringCityName() {
            // Arrange
            Location location = new Location("", "XX", 0.0, 0.0);
            int days = 1;
            WeatherForecast forecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(forecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle empty city name");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle location at equator")
        void shouldHandleLocationAtEquator() {
            // Arrange
            Location location = new Location("Quito", "EC", 0.0, -78.4678);
            int days = 3;
            WeatherForecast forecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(forecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle location at equator");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }

        @Test
        @DisplayName("Should handle location at prime meridian")
        void shouldHandleLocationAtPrimeMeridian() {
            // Arrange
            Location location = new Location("Greenwich", "UK", 51.4779, 0.0);
            int days = 4;
            WeatherForecast forecast = createMockWeatherForecast(location, days);

            when(forecastProvider.fetchForecast(location, days)).thenReturn(forecast);

            // Act
            WeatherForecast result = forecastService.getForecast(location, days);

            // Assert
            assertNotNull(result, "Should handle location at prime meridian");
            verify(forecastProvider, times(1)).fetchForecast(location, days);
        }
    }

    // Helper method to create mock weather forecast objects
    private WeatherForecast createMockWeatherForecast(Location location, int days) {
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        LocalDate startDate = LocalDate.now();

        for (int i = 0; i < days; i++) {
            DailyForecast dailyForecast = new DailyForecast(
                    startDate.plusDays(i),
                    20.0 + i,  // temperature
                    15.0 + i,  // minTemperature
                    25.0 + i,  // maxTemperature
                    "Sunny",   // condition
                    50.0       // humidity
            );
            dailyForecasts.add(dailyForecast);
        }

        return new WeatherForecast(location, dailyForecasts);
    }
}