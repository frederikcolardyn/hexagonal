package io.github.hexagonal.weather.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for DailyForecast record.
 * Testing framework: JUnit 5
 * 
 * Coverage includes:
 * - Valid construction (happy paths)
 * - Validation rules for precipitation probability
 * - Validation rules for temperature constraints
 * - Edge cases and boundary conditions
 * - Null validation for required fields
 * - Record contract (equals, hashCode, toString)
 */
@DisplayName("DailyForecast Record Tests")
class DailyForecastTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2024, 1, 15);
    private static final double VALID_TEMP_MIN = 10.0;
    private static final double VALID_TEMP_MAX = 25.0;
    private static final WeatherCondition VALID_CONDITION = WeatherCondition.SUNNY;
    private static final double VALID_PRECIPITATION = 0.3;

    @Nested
    @DisplayName("Valid Construction Tests")
    class ValidConstructionTests {

        @Test
        @DisplayName("Should create DailyForecast with valid parameters")
        void shouldCreateDailyForecastWithValidParameters() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(TEST_DATE, forecast.date());
            assertEquals(VALID_TEMP_MIN, forecast.temperatureMin());
            assertEquals(VALID_TEMP_MAX, forecast.temperatureMax());
            assertEquals(VALID_CONDITION, forecast.condition());
            assertEquals(VALID_PRECIPITATION, forecast.precipitationProbability());
        }

        @Test
        @DisplayName("Should create DailyForecast with zero precipitation probability")
        void shouldCreateWithZeroPrecipitation() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                0.0
            );

            assertNotNull(forecast);
            assertEquals(0.0, forecast.precipitationProbability());
        }

        @Test
        @DisplayName("Should create DailyForecast with 100% precipitation probability")
        void shouldCreateWithMaxPrecipitation() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                1.0
            );

            assertNotNull(forecast);
            assertEquals(1.0, forecast.precipitationProbability());
        }

        @Test
        @DisplayName("Should create DailyForecast with equal min and max temperatures")
        void shouldCreateWithEqualMinMaxTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                20.0,
                20.0,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(20.0, forecast.temperatureMin());
            assertEquals(20.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should create DailyForecast with negative temperatures")
        void shouldCreateWithNegativeTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                -15.0,
                -5.0,
                WeatherCondition.SNOWY,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(-15.0, forecast.temperatureMin());
            assertEquals(-5.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should create DailyForecast with extreme temperature range")
        void shouldCreateWithExtremeTemperatureRange() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                -40.0,
                50.0,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(-40.0, forecast.temperatureMin());
            assertEquals(50.0, forecast.temperatureMax());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.1, 0.25, 0.5, 0.75, 0.99, 1.0})
        @DisplayName("Should accept valid precipitation probabilities")
        void shouldAcceptValidPrecipitationProbabilities(double probability) {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                probability
            );

            assertNotNull(forecast);
            assertEquals(probability, forecast.precipitationProbability());
        }
    }

    @Nested
    @DisplayName("Precipitation Probability Validation Tests")
    class PrecipitationValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when precipitation probability is negative")
        void shouldThrowExceptionWhenPrecipitationIsNegative() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    -0.1
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when precipitation probability exceeds 1.0")
        void shouldThrowExceptionWhenPrecipitationExceedsOne() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    1.1
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1.0, -0.01, 1.01, 2.0, 100.0, Double.MAX_VALUE})
        @DisplayName("Should reject invalid precipitation probabilities")
        void shouldRejectInvalidPrecipitationProbabilities(double invalidProbability) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    invalidProbability
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for NaN precipitation probability")
        void shouldThrowExceptionForNaNPrecipitation() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    Double.NaN
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for positive infinity precipitation")
        void shouldThrowExceptionForPositiveInfinityPrecipitation() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    Double.POSITIVE_INFINITY
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for negative infinity precipitation")
        void shouldThrowExceptionForNegativeInfinityPrecipitation() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    Double.NEGATIVE_INFINITY
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Temperature Validation Tests")
    class TemperatureValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when min temperature exceeds max temperature")
        void shouldThrowExceptionWhenMinExceedsMax() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    30.0,
                    20.0,
                    VALID_CONDITION,
                    VALID_PRECIPITATION
                )
            );

            assertEquals("Minimum temperature cannot be higher than maximum temperature", exception.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
            "25.0, 20.0",
            "0.0, -5.0",
            "-10.0, -15.0",
            "100.0, 50.0"
        })
        @DisplayName("Should reject forecasts where min temperature exceeds max")
        void shouldRejectInvalidTemperatureRanges(double min, double max) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    min,
                    max,
                    VALID_CONDITION,
                    VALID_PRECIPITATION
                )
            );

            assertEquals("Minimum temperature cannot be higher than maximum temperature", exception.getMessage());
        }

        @Test
        @DisplayName("Should allow zero temperature values")
        void shouldAllowZeroTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                0.0,
                0.0,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(0.0, forecast.temperatureMin());
            assertEquals(0.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should handle extreme negative temperatures")
        void shouldHandleExtremeNegativeTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                -273.15, // Absolute zero in Celsius
                -200.0,
                WeatherCondition.SNOWY,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(-273.15, forecast.temperatureMin());
            assertEquals(-200.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should handle extreme positive temperatures")
        void shouldHandleExtremePositiveTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                50.0,
                60.0, // Death Valley-like temperatures
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(50.0, forecast.temperatureMin());
            assertEquals(60.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should handle fractional temperature values")
        void shouldHandleFractionalTemperatures() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                15.5,
                23.7,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(15.5, forecast.temperatureMin());
            assertEquals(23.7, forecast.temperatureMax());
        }
    }

    @Nested
    @DisplayName("Null Validation Tests")
    class NullValidationTests {

        @Test
        @DisplayName("Should throw NullPointerException when date is null")
        void shouldThrowExceptionWhenDateIsNull() {
            assertThrows(
                NullPointerException.class,
                () -> new DailyForecast(
                    null,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    VALID_PRECIPITATION
                )
            );
        }

        @Test
        @DisplayName("Should throw NullPointerException when condition is null")
        void shouldThrowExceptionWhenConditionIsNull() {
            assertThrows(
                NullPointerException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    null,
                    VALID_PRECIPITATION
                )
            );
        }
    }

    @Nested
    @DisplayName("Date Handling Tests")
    class DateHandlingTests {

        @Test
        @DisplayName("Should create forecast for today")
        void shouldCreateForecastForToday() {
            LocalDate today = LocalDate.now();
            DailyForecast forecast = new DailyForecast(
                today,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(today, forecast.date());
        }

        @Test
        @DisplayName("Should create forecast for past date")
        void shouldCreateForecastForPastDate() {
            LocalDate pastDate = LocalDate.of(2020, 1, 1);
            DailyForecast forecast = new DailyForecast(
                pastDate,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(pastDate, forecast.date());
        }

        @Test
        @DisplayName("Should create forecast for future date")
        void shouldCreateForecastForFutureDate() {
            LocalDate futureDate = LocalDate.of(2030, 12, 31);
            DailyForecast forecast = new DailyForecast(
                futureDate,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(futureDate, forecast.date());
        }
    }

    @Nested
    @DisplayName("Record Contract Tests")
    class RecordContractTests {

        @Test
        @DisplayName("Should have equal instances with same values")
        void shouldHaveEqualInstancesWithSameValues() {
            DailyForecast forecast1 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            DailyForecast forecast2 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(forecast1, forecast2);
            assertEquals(forecast1.hashCode(), forecast2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when dates differ")
        void shouldNotBeEqualWhenDatesDiffer() {
            DailyForecast forecast1 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            DailyForecast forecast2 = new DailyForecast(
                TEST_DATE.plusDays(1),
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should not be equal when temperatures differ")
        void shouldNotBeEqualWhenTemperaturesDiffer() {
            DailyForecast forecast1 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            DailyForecast forecast2 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN + 1.0,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should not be equal when conditions differ")
        void shouldNotBeEqualWhenConditionsDiffer() {
            DailyForecast forecast1 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                WeatherCondition.SUNNY,
                VALID_PRECIPITATION
            );

            DailyForecast forecast2 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                WeatherCondition.RAINY,
                VALID_PRECIPITATION
            );

            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should not be equal when precipitation probabilities differ")
        void shouldNotBeEqualWhenPrecipitationDiffers() {
            DailyForecast forecast1 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                0.3
            );

            DailyForecast forecast2 = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                0.7
            );

            assertNotEquals(forecast1, forecast2);
        }

        @Test
        @DisplayName("Should have proper toString representation")
        void shouldHaveProperToStringRepresentation() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            String toString = forecast.toString();
            
            assertNotNull(toString);
            assertTrue(toString.contains("DailyForecast"));
            assertTrue(toString.contains(TEST_DATE.toString()));
            assertTrue(toString.contains(String.valueOf(VALID_TEMP_MIN)));
            assertTrue(toString.contains(String.valueOf(VALID_TEMP_MAX)));
            assertTrue(toString.contains(VALID_CONDITION.toString()));
            assertTrue(toString.contains(String.valueOf(VALID_PRECIPITATION)));
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(forecast, forecast);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotEquals(null, forecast);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertNotEquals(forecast, "Not a DailyForecast");
        }
    }

    @Nested
    @DisplayName("Combined Validation Tests")
    class CombinedValidationTests {

        @Test
        @DisplayName("Should fail validation before checking temperature when precipitation is invalid")
        void shouldFailOnPrecipitationBeforeTemperature() {
            // Both validations would fail, but precipitation is checked first
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    30.0,  // Invalid: min > max
                    20.0,
                    VALID_CONDITION,
                    -0.5   // Invalid: precipitation < 0
                )
            );

            assertEquals("Precipitation probability must be between 0.0 and 1.0", exception.getMessage());
        }

        @Test
        @DisplayName("Should validate temperature after precipitation passes")
        void shouldValidateTemperatureAfterPrecipitationPasses() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    30.0,  // Invalid: min > max
                    20.0,
                    VALID_CONDITION,
                    0.5    // Valid precipitation
                )
            );

            assertEquals("Minimum temperature cannot be higher than maximum temperature", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Weather Condition Tests")
    class WeatherConditionTests {

        @ParameterizedTest
        @MethodSource("provideAllWeatherConditions")
        @DisplayName("Should create DailyForecast with all weather conditions")
        void shouldCreateWithAllWeatherConditions(WeatherCondition condition) {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                condition,
                VALID_PRECIPITATION
            );

            assertNotNull(forecast);
            assertEquals(condition, forecast.condition());
        }

        private static Stream<WeatherCondition> provideAllWeatherConditions() {
            return Stream.of(WeatherCondition.values());
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should accept precipitation probability at lower boundary (0.0)")
        void shouldAcceptPrecipitationAtLowerBoundary() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                0.0
            );

            assertEquals(0.0, forecast.precipitationProbability());
        }

        @Test
        @DisplayName("Should accept precipitation probability at upper boundary (1.0)")
        void shouldAcceptPrecipitationAtUpperBoundary() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                1.0
            );

            assertEquals(1.0, forecast.precipitationProbability());
        }

        @Test
        @DisplayName("Should reject precipitation probability just below lower boundary")
        void shouldRejectPrecipitationJustBelowLowerBoundary() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    -0.000001
                )
            );
        }

        @Test
        @DisplayName("Should reject precipitation probability just above upper boundary")
        void shouldRejectPrecipitationJustAboveUpperBoundary() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    VALID_TEMP_MIN,
                    VALID_TEMP_MAX,
                    VALID_CONDITION,
                    1.000001
                )
            );
        }

        @Test
        @DisplayName("Should accept temperatures at equality boundary")
        void shouldAcceptTemperaturesAtEqualityBoundary() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                15.0,
                15.0,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            assertEquals(15.0, forecast.temperatureMin());
            assertEquals(15.0, forecast.temperatureMax());
        }

        @Test
        @DisplayName("Should reject temperatures just violating boundary")
        void shouldRejectTemperaturesJustViolatingBoundary() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new DailyForecast(
                    TEST_DATE,
                    15.000001,
                    15.0,
                    VALID_CONDITION,
                    VALID_PRECIPITATION
                )
            );
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Record should be immutable - date cannot be modified")
        void recordShouldBeImmutable() {
            LocalDate originalDate = LocalDate.of(2024, 6, 15);
            DailyForecast forecast = new DailyForecast(
                originalDate,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            // Modifying the original date should not affect the forecast
            LocalDate modifiedDate = originalDate.plusDays(10);
            
            assertEquals(originalDate, forecast.date());
            assertNotEquals(modifiedDate, forecast.date());
        }

        @Test
        @DisplayName("Accessor methods should return consistent values")
        void accessorsShouldReturnConsistentValues() {
            DailyForecast forecast = new DailyForecast(
                TEST_DATE,
                VALID_TEMP_MIN,
                VALID_TEMP_MAX,
                VALID_CONDITION,
                VALID_PRECIPITATION
            );

            // Call accessors multiple times
            assertEquals(forecast.date(), forecast.date());
            assertEquals(forecast.temperatureMin(), forecast.temperatureMin());
            assertEquals(forecast.temperatureMax(), forecast.temperatureMax());
            assertEquals(forecast.condition(), forecast.condition());
            assertEquals(forecast.precipitationProbability(), forecast.precipitationProbability());
        }
    }
}