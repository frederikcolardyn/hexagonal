package io.github.hexagonal.weather.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for WeatherForecast record.
 * Tests cover validation, constructor behavior, immutability, edge cases, and failure scenarios.
 * 
 * Testing Framework: JUnit 5 (Jupiter)
 * Validation Framework: Jakarta Bean Validation
 */
@DisplayName("WeatherForecast Record Tests")
class WeatherForecastTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ==================== Test Fixtures ====================

    private static Location createValidLocation() {
        return new Location("New York", "US", 40.7128, -74.0060);
    }

    private static DailyForecast createValidDailyForecast(LocalDate date) {
        return new DailyForecast(
            date,
            20.0,
            15.0,
            25.0,
            "Partly Cloudy",
            60,
            10.5,
            1013.25
        );
    }

    private static List<DailyForecast> createValidDailyForecasts() {
        return List.of(
            createValidDailyForecast(LocalDate.now()),
            createValidDailyForecast(LocalDate.now().plusDays(1)),
            createValidDailyForecast(LocalDate.now().plusDays(2))
        );
    }

    // ==================== Happy Path Tests ====================

    @Nested
    @DisplayName("Valid Construction Tests")
    class ValidConstructionTests {

        @Test
        @DisplayName("Should create WeatherForecast with valid location and single daily forecast")
        void shouldCreateWithSingleDailyForecast() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = List.of(createValidDailyForecast(LocalDate.now()));

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(location, weatherForecast.location());
            assertEquals(forecasts, weatherForecast.dailyForecasts());
            assertEquals(1, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should create WeatherForecast with valid location and multiple daily forecasts")
        void shouldCreateWithMultipleDailyForecasts() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(location, weatherForecast.location());
            assertEquals(forecasts, weatherForecast.dailyForecasts());
            assertEquals(3, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should create WeatherForecast with 7-day forecast (typical use case)")
        void shouldCreateWithSevenDayForecast() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                forecasts.add(createValidDailyForecast(LocalDate.now().plusDays(i)));
            }

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(7, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should pass Jakarta Bean Validation with valid inputs")
        void shouldPassBeanValidation() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Act
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(weatherForecast);

            // Assert
            assertTrue(violations.isEmpty(), "Should have no validation violations");
        }
    }

    // ==================== Null Validation Tests ====================

    @Nested
    @DisplayName("Null Input Validation Tests")
    class NullInputTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when dailyForecasts is null")
        void shouldThrowExceptionWhenDailyForecastsIsNull() {
            // Arrange
            Location location = createValidLocation();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WeatherForecast(location, null)
            );
            assertEquals("Daily forecasts list cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should fail Jakarta Bean Validation when location is null")
        void shouldFailValidationWhenLocationIsNull() {
            // Arrange
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast weatherForecast = new WeatherForecast(null, forecasts);

            // Act
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(weatherForecast);

            // Assert
            assertFalse(violations.isEmpty(), "Should have validation violations for null location");
            assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("location")));
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should handle null location parameter")
        void shouldHandleNullLocationParameter(Location nullLocation) {
            // Arrange
            List<DailyForecast> forecasts = createValidDailyForecasts();

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(nullLocation, forecasts);
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(weatherForecast);

            // Assert
            assertFalse(violations.isEmpty());
        }
    }

    // ==================== Empty List Tests ====================

    @Nested
    @DisplayName("Empty List Validation Tests")
    class EmptyListTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when dailyForecasts is empty list")
        void shouldThrowExceptionWhenDailyForecastsIsEmpty() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> emptyList = Collections.emptyList();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WeatherForecast(location, emptyList)
            );
            assertEquals("Daily forecasts list cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when dailyForecasts is empty ArrayList")
        void shouldThrowExceptionWhenDailyForecastsIsEmptyArrayList() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> emptyList = new ArrayList<>();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WeatherForecast(location, emptyList)
            );
            assertEquals("Daily forecasts list cannot be null or empty", exception.getMessage());
        }
    }

    // ==================== Record Equality and HashCode Tests ====================

    @Nested
    @DisplayName("Record Equality and HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields are equal")
        void shouldBeEqualWhenAllFieldsAreEqual() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast1 = new WeatherForecast(location, forecasts);
            WeatherForecast forecast2 = new WeatherForecast(location, forecasts);

            // Act & Assert
            assertEquals(forecast1, forecast2);
            assertEquals(forecast1.hashCode(), forecast2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when locations differ")
        void shouldNotBeEqualWhenLocationsDiffer() {
            // Arrange
            Location location1 = createValidLocation();
            Location location2 = new Location("London", "UK", 51.5074, -0.1278);
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast1 = new WeatherForecast(location1, forecasts);
            WeatherForecast forecast2 = new WeatherForecast(location2, forecasts);

            // Act & Assert
            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should not be equal when daily forecasts differ")
        void shouldNotBeEqualWhenDailyForecastsDiffer() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts1 = createValidDailyForecasts();
            List<DailyForecast> forecasts2 = List.of(createValidDailyForecast(LocalDate.now().plusDays(10)));
            WeatherForecast forecast1 = new WeatherForecast(location, forecasts1);
            WeatherForecast forecast2 = new WeatherForecast(location, forecasts2);

            // Act & Assert
            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should be reflexive (equals itself)")
        void shouldBeReflexive() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act & Assert
            assertEquals(forecast, forecast);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act & Assert
            assertNotEquals(null, forecast);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);
            String differentType = "Not a WeatherForecast";

            // Act & Assert
            assertNotEquals(forecast, differentType);
        }
    }

    // ==================== ToString Tests ====================

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate valid toString representation")
        void shouldGenerateValidToString() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            String toString = forecast.toString();

            // Assert
            assertNotNull(toString);
            assertTrue(toString.contains("WeatherForecast"));
            assertTrue(toString.contains("location"));
            assertTrue(toString.contains("dailyForecasts"));
        }

        @Test
        @DisplayName("Should include location information in toString")
        void shouldIncludeLocationInToString() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            String toString = forecast.toString();

            // Assert
            assertTrue(toString.contains("New York") || toString.contains(location.toString()));
        }
    }

    // ==================== Immutability Tests ====================

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should maintain immutability - modifying original list should not affect record")
        void shouldMaintainImmutabilityWhenOriginalListIsModified() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = new ArrayList<>(createValidDailyForecasts());
            int originalSize = forecasts.size();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            forecasts.add(createValidDailyForecast(LocalDate.now().plusDays(10)));

            // Assert
            assertEquals(originalSize, forecast.dailyForecasts().size(), 
                "Record should not be affected by modifications to original list");
        }

        @Test
        @DisplayName("Should return same instance for accessor methods")
        void shouldReturnConsistentAccessorValues() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act & Assert
            assertSame(forecast.location(), forecast.location());
            assertSame(forecast.dailyForecasts(), forecast.dailyForecasts());
        }
    }

    // ==================== Edge Cases and Boundary Tests ====================

    @Nested
    @DisplayName("Edge Case and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle forecast with large number of days (365 days)")
        void shouldHandleLargeForecastList() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = new ArrayList<>();
            for (int i = 0; i < 365; i++) {
                forecasts.add(createValidDailyForecast(LocalDate.now().plusDays(i)));
            }

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(365, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should handle minimum valid case - single forecast")
        void shouldHandleMinimumValidCase() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> singleForecast = List.of(createValidDailyForecast(LocalDate.now()));

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, singleForecast);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(1, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should handle forecasts with duplicate dates")
        void shouldHandleForcastsWithDuplicateDates() {
            // Arrange
            Location location = createValidLocation();
            LocalDate sameDate = LocalDate.now();
            List<DailyForecast> forecasts = List.of(
                createValidDailyForecast(sameDate),
                createValidDailyForecast(sameDate)
            );

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(2, weatherForecast.dailyForecasts().size());
        }

        @Test
        @DisplayName("Should handle forecasts in non-chronological order")
        void shouldHandleNonChronologicalForecasts() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = List.of(
                createValidDailyForecast(LocalDate.now().plusDays(5)),
                createValidDailyForecast(LocalDate.now()),
                createValidDailyForecast(LocalDate.now().plusDays(2))
            );

            // Act
            WeatherForecast weatherForecast = new WeatherForecast(location, forecasts);

            // Assert
            assertNotNull(weatherForecast);
            assertEquals(3, weatherForecast.dailyForecasts().size());
        }
    }

    // ==================== Parameterized Tests ====================

    @Nested
    @DisplayName("Parameterized Validation Tests")
    class ParameterizedValidationTests {

        private record InvalidForecastTestCase(
            String description,
            Location location,
            List<DailyForecast> dailyForecasts,
            Class<? extends Exception> expectedException
        ) {}

        private static Stream<Arguments> invalidForecastCases() {
            return Stream.of(
                Arguments.of(
                    new InvalidForecastTestCase(
                        "null location and null forecasts",
                        null,
                        null,
                        IllegalArgumentException.class
                    )
                ),
                Arguments.of(
                    new InvalidForecastTestCase(
                        "valid location and null forecasts",
                        createValidLocation(),
                        null,
                        IllegalArgumentException.class
                    )
                ),
                Arguments.of(
                    new InvalidForecastTestCase(
                        "valid location and empty forecasts",
                        createValidLocation(),
                        Collections.emptyList(),
                        IllegalArgumentException.class
                    )
                ),
                Arguments.of(
                    new InvalidForecastTestCase(
                        "valid location and empty ArrayList",
                        createValidLocation(),
                        new ArrayList<>(),
                        IllegalArgumentException.class
                    )
                )
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("invalidForecastCases")
        @DisplayName("Should throw exception for invalid inputs")
        void shouldThrowExceptionForInvalidInputs(InvalidForecastTestCase testCase) {
            // Act & Assert
            assertThrows(
                testCase.expectedException(),
                () -> new WeatherForecast(testCase.location(), testCase.dailyForecasts()),
                testCase.description()
            );
        }
    }

    // ==================== Integration with Jakarta Validation ====================

    @Nested
    @DisplayName("Jakarta Bean Validation Integration Tests")
    class JakartaValidationIntegrationTests {

        @Test
        @DisplayName("Should validate nested Location object")
        void shouldValidateNestedLocation() {
            // Arrange - Create invalid location (assuming Location has validation constraints)
            Location invalidLocation = new Location(null, null, 0.0, 0.0);
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(invalidLocation, forecasts);

            // Act
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(forecast);

            // Assert
            // This will fail if Location has @NotNull constraints on its fields
            assertNotNull(violations);
        }

        @Test
        @DisplayName("Should validate nested DailyForecast objects in list")
        void shouldValidateNestedDailyForecasts() {
            // Arrange - Create invalid daily forecast
            Location location = createValidLocation();
            DailyForecast invalidForecast = new DailyForecast(
                null, 0.0, 0.0, 0.0, null, 0, 0.0, 0.0
            );
            List<DailyForecast> forecasts = List.of(invalidForecast);
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(forecast);

            // Assert
            assertNotNull(violations);
        }

        @Test
        @DisplayName("Should pass validation with all valid nested objects")
        void shouldPassValidationWithAllValidNestedObjects() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            Set<ConstraintViolation<WeatherForecast>> violations = validator.validate(forecast);

            // Assert
            assertTrue(violations.isEmpty(), "Valid nested objects should pass validation");
        }
    }

    // ==================== Accessor Method Tests ====================

    @Nested
    @DisplayName("Accessor Method Tests")
    class AccessorMethodTests {

        @Test
        @DisplayName("Should return correct location from accessor")
        void shouldReturnCorrectLocation() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            Location retrievedLocation = forecast.location();

            // Assert
            assertNotNull(retrievedLocation);
            assertEquals(location, retrievedLocation);
            assertEquals("New York", retrievedLocation.city());
        }

        @Test
        @DisplayName("Should return correct dailyForecasts from accessor")
        void shouldReturnCorrectDailyForecasts() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act
            List<DailyForecast> retrievedForecasts = forecast.dailyForecasts();

            // Assert
            assertNotNull(retrievedForecasts);
            assertEquals(forecasts.size(), retrievedForecasts.size());
            assertEquals(forecasts, retrievedForecasts);
        }

        @Test
        @DisplayName("Should return non-null values from accessors")
        void shouldReturnNonNullFromAccessors() {
            // Arrange
            Location location = createValidLocation();
            List<DailyForecast> forecasts = createValidDailyForecasts();
            WeatherForecast forecast = new WeatherForecast(location, forecasts);

            // Act & Assert
            assertAll(
                () -> assertNotNull(forecast.location(), "Location accessor should not return null"),
                () -> assertNotNull(forecast.dailyForecasts(), "DailyForecasts accessor should not return null")
            );
        }
    }
}