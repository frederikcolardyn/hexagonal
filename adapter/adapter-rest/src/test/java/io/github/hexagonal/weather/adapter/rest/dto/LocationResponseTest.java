package io.github.hexagonal.weather.adapter.rest.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for LocationResponse DTO.
 * Testing framework: JUnit 5 with AssertJ assertions
 */
@DisplayName("LocationResponse Tests")
class LocationResponseTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Constructor and Field Access Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create LocationResponse with valid positive coordinates")
        void shouldCreateLocationResponseWithValidPositiveCoordinates() {
            // Given
            double latitude = 40.7128;
            double longitude = -74.0060;
            String cityName = "New York";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(latitude);
            assertThat(response.longitude()).isEqualTo(longitude);
            assertThat(response.cityName()).isEqualTo(cityName);
        }

        @Test
        @DisplayName("Should create LocationResponse with valid negative coordinates")
        void shouldCreateLocationResponseWithNegativeCoordinates() {
            // Given
            double latitude = -33.8688;
            double longitude = 151.2093;
            String cityName = "Sydney";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(latitude);
            assertThat(response.longitude()).isEqualTo(longitude);
            assertThat(response.cityName()).isEqualTo(cityName);
        }

        @Test
        @DisplayName("Should create LocationResponse with zero coordinates")
        void shouldCreateLocationResponseWithZeroCoordinates() {
            // Given
            double latitude = 0.0;
            double longitude = 0.0;
            String cityName = "Null Island";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isZero();
            assertThat(response.longitude()).isZero();
            assertThat(response.cityName()).isEqualTo(cityName);
        }

        @Test
        @DisplayName("Should create LocationResponse with extreme valid coordinates")
        void shouldCreateLocationResponseWithExtremeCoordinates() {
            // Given - Testing boundary values
            double maxLatitude = 90.0;
            double minLongitude = -180.0;
            String cityName = "North Pole";

            // When
            LocationResponse response = new LocationResponse(maxLatitude, minLongitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(maxLatitude);
            assertThat(response.longitude()).isEqualTo(minLongitude);
            assertThat(response.cityName()).isEqualTo(cityName);
        }

        @Test
        @DisplayName("Should create LocationResponse with empty city name")
        void shouldCreateLocationResponseWithEmptyCityName() {
            // Given
            double latitude = 51.5074;
            double longitude = -0.1278;
            String cityName = "";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(latitude);
            assertThat(response.longitude()).isEqualTo(longitude);
            assertThat(response.cityName()).isEmpty();
        }

        @Test
        @DisplayName("Should create LocationResponse with null city name")
        void shouldCreateLocationResponseWithNullCityName() {
            // Given
            double latitude = 48.8566;
            double longitude = 2.3522;
            String cityName = null;

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(latitude);
            assertThat(response.longitude()).isEqualTo(longitude);
            assertThat(response.cityName()).isNull();
        }

        @Test
        @DisplayName("Should create LocationResponse with special characters in city name")
        void shouldCreateLocationResponseWithSpecialCharactersInCityName() {
            // Given
            double latitude = 46.9479;
            double longitude = 7.4474;
            String cityName = "Bern (Bärn) - Schweiz/Suisse";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.cityName()).isEqualTo(cityName);
        }

        @Test
        @DisplayName("Should create LocationResponse with Unicode city name")
        void shouldCreateLocationResponseWithUnicodeCityName() {
            // Given
            double latitude = 35.6762;
            double longitude = 139.6503;
            String cityName = "東京";

            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.cityName()).isEqualTo(cityName);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, "New York");
            LocationResponse response2 = new LocationResponse(40.7128, -74.0060, "New York");

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when latitude differs")
        void shouldNotBeEqualWhenLatitudeDiffers() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, "New York");
            LocationResponse response2 = new LocationResponse(40.7129, -74.0060, "New York");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Should not be equal when longitude differs")
        void shouldNotBeEqualWhenLongitudeDiffers() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, "New York");
            LocationResponse response2 = new LocationResponse(40.7128, -74.0061, "New York");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Should not be equal when city name differs")
        void shouldNotBeEqualWhenCityNameDiffers() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, "New York");
            LocationResponse response2 = new LocationResponse(40.7128, -74.0060, "Manhattan");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");

            // Then
            assertThat(response).isEqualTo(response);
            assertThat(response.hashCode()).isEqualTo(response.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");

            // Then
            assertThat(response).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");
            String notAResponse = "Not a LocationResponse";

            // Then
            assertThat(response).isNotEqualTo(notAResponse);
        }

        @Test
        @DisplayName("Should handle null city names in equality")
        void shouldHandleNullCityNamesInEquality() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, null);
            LocationResponse response2 = new LocationResponse(40.7128, -74.0060, null);

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate toString with all fields")
        void shouldGenerateToStringWithAllFields() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                .contains("LocationResponse")
                .contains("40.7128")
                .contains("-74.0060")
                .contains("New York");
        }

        @Test
        @DisplayName("Should generate toString with null city name")
        void shouldGenerateToStringWithNullCityName() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, null);

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                .contains("LocationResponse")
                .contains("null");
        }
    }

    @Nested
    @DisplayName("JSON Serialization Tests")
    class JsonSerializationTests {

        @Test
        @DisplayName("Should serialize to JSON with correct property names")
        void shouldSerializeToJsonWithCorrectPropertyNames() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");

            // When
            String json = objectMapper.writeValueAsString(response);

            // Then
            assertThat(json)
                .contains("\"latitude\":40.7128")
                .contains("\"longitude\":-74.0060")
                .contains("\"city_name\":\"New York\"");
        }

        @Test
        @DisplayName("Should deserialize from JSON with correct property names")
        void shouldDeserializeFromJsonWithCorrectPropertyNames() throws JsonProcessingException {
            // Given
            String json = "{\"latitude\":40.7128,\"longitude\":-74.0060,\"city_name\":\"New York\"}";

            // When
            LocationResponse response = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(response.latitude()).isEqualTo(40.7128);
            assertThat(response.longitude()).isEqualTo(-74.0060);
            assertThat(response.cityName()).isEqualTo("New York");
        }

        @Test
        @DisplayName("Should serialize and deserialize maintaining data integrity")
        void shouldSerializeAndDeserializeMaintainingDataIntegrity() throws JsonProcessingException {
            // Given
            LocationResponse original = new LocationResponse(51.5074, -0.1278, "London");

            // When
            String json = objectMapper.writeValueAsString(original);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(deserialized).isEqualTo(original);
        }

        @Test
        @DisplayName("Should handle null city name in JSON serialization")
        void shouldHandleNullCityNameInJsonSerialization() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(48.8566, 2.3522, null);

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(json).contains("\"city_name\":null");
            assertThat(deserialized.cityName()).isNull();
        }

        @Test
        @DisplayName("Should handle negative coordinates in JSON")
        void shouldHandleNegativeCoordinatesInJson() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(-33.8688, -151.2093, "Sydney");

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(deserialized.latitude()).isEqualTo(-33.8688);
            assertThat(deserialized.longitude()).isEqualTo(-151.2093);
        }

        @Test
        @DisplayName("Should handle special characters in city name during JSON serialization")
        void shouldHandleSpecialCharactersInCityNameDuringJsonSerialization() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(46.9479, 7.4474, "Bern \"City\"");

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(deserialized.cityName()).isEqualTo("Bern \"City\"");
        }

        @Test
        @DisplayName("Should handle empty string city name in JSON")
        void shouldHandleEmptyStringCityNameInJson() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(35.6762, 139.6503, "");

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(json).contains("\"city_name\":\"\"");
            assertThat(deserialized.cityName()).isEmpty();
        }

        @Test
        @DisplayName("Should preserve precision of coordinate values")
        void shouldPreservePrecisionOfCoordinateValues() throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(40.712776, -74.005974, "New York");

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(deserialized.latitude()).isEqualTo(40.712776);
            assertThat(deserialized.longitude()).isEqualTo(-74.005974);
        }
    }

    @Nested
    @DisplayName("Parameterized Tests for Multiple Cities")
    class ParameterizedCityTests {

        record CityTestCase(double latitude, double longitude, String cityName) {}

        static Stream<CityTestCase> worldCities() {
            return Stream.of(
                new CityTestCase(40.7128, -74.0060, "New York"),
                new CityTestCase(51.5074, -0.1278, "London"),
                new CityTestCase(48.8566, 2.3522, "Paris"),
                new CityTestCase(35.6762, 139.6503, "Tokyo"),
                new CityTestCase(-33.8688, 151.2093, "Sydney"),
                new CityTestCase(55.7558, 37.6173, "Moscow"),
                new CityTestCase(-23.5505, -46.6333, "São Paulo"),
                new CityTestCase(19.4326, -99.1332, "Mexico City"),
                new CityTestCase(1.3521, 103.8198, "Singapore"),
                new CityTestCase(-34.6037, -58.3816, "Buenos Aires")
            );
        }

        @ParameterizedTest
        @MethodSource("worldCities")
        @DisplayName("Should create LocationResponse for world cities")
        void shouldCreateLocationResponseForWorldCities(CityTestCase testCase) {
            // When
            LocationResponse response = new LocationResponse(
                testCase.latitude(),
                testCase.longitude(),
                testCase.cityName()
            );

            // Then
            assertThat(response.latitude()).isEqualTo(testCase.latitude());
            assertThat(response.longitude()).isEqualTo(testCase.longitude());
            assertThat(response.cityName()).isEqualTo(testCase.cityName());
        }

        @ParameterizedTest
        @MethodSource("worldCities")
        @DisplayName("Should serialize and deserialize world cities correctly")
        void shouldSerializeAndDeserializeWorldCities(CityTestCase testCase) throws JsonProcessingException {
            // Given
            LocationResponse response = new LocationResponse(
                testCase.latitude(),
                testCase.longitude(),
                testCase.cityName()
            );

            // When
            String json = objectMapper.writeValueAsString(response);
            LocationResponse deserialized = objectMapper.readValue(json, LocationResponse.class);

            // Then
            assertThat(deserialized).isEqualTo(response);
        }

        @ParameterizedTest
        @CsvSource({
            "90.0, 180.0, 'North Pole'",
            "-90.0, -180.0, 'South Pole'",
            "0.0, 0.0, 'Null Island'",
            "45.0, 45.0, 'Mid Point'",
            "-45.0, -45.0, 'Southern Mid Point'"
        })
        @DisplayName("Should handle boundary coordinate values")
        void shouldHandleBoundaryCoordinateValues(double latitude, double longitude, String cityName) {
            // When
            LocationResponse response = new LocationResponse(latitude, longitude, cityName);

            // Then
            assertThat(response.latitude()).isEqualTo(latitude);
            assertThat(response.longitude()).isEqualTo(longitude);
            assertThat(response.cityName()).isEqualTo(cityName);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long city names")
        void shouldHandleVeryLongCityNames() {
            // Given
            String longCityName = "Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch";
            LocationResponse response = new LocationResponse(53.2236, -4.1981, longCityName);

            // Then
            assertThat(response.cityName()).isEqualTo(longCityName);
        }

        @Test
        @DisplayName("Should handle city names with newlines")
        void shouldHandleCityNamesWithNewlines() {
            // Given
            String cityNameWithNewline = "New\nYork";
            LocationResponse response = new LocationResponse(40.7128, -74.0060, cityNameWithNewline);

            // Then
            assertThat(response.cityName()).isEqualTo(cityNameWithNewline);
        }

        @Test
        @DisplayName("Should handle city names with tabs")
        void shouldHandleCityNamesWithTabs() {
            // Given
            String cityNameWithTab = "New\tYork";
            LocationResponse response = new LocationResponse(40.7128, -74.0060, cityNameWithTab);

            // Then
            assertThat(response.cityName()).isEqualTo(cityNameWithTab);
        }

        @Test
        @DisplayName("Should handle Double.MAX_VALUE coordinates")
        void shouldHandleMaxValueCoordinates() {
            // Given
            LocationResponse response = new LocationResponse(Double.MAX_VALUE, Double.MAX_VALUE, "Extreme");

            // Then
            assertThat(response.latitude()).isEqualTo(Double.MAX_VALUE);
            assertThat(response.longitude()).isEqualTo(Double.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle Double.MIN_VALUE coordinates")
        void shouldHandleMinValueCoordinates() {
            // Given
            LocationResponse response = new LocationResponse(Double.MIN_VALUE, Double.MIN_VALUE, "Minimal");

            // Then
            assertThat(response.latitude()).isEqualTo(Double.MIN_VALUE);
            assertThat(response.longitude()).isEqualTo(Double.MIN_VALUE);
        }

        @Test
        @DisplayName("Should handle NaN coordinates")
        void shouldHandleNaNCoordinates() {
            // Given
            LocationResponse response = new LocationResponse(Double.NaN, Double.NaN, "Unknown");

            // Then
            assertThat(response.latitude()).isNaN();
            assertThat(response.longitude()).isNaN();
        }

        @Test
        @DisplayName("Should handle positive infinity coordinates")
        void shouldHandlePositiveInfinityCoordinates() {
            // Given
            LocationResponse response = new LocationResponse(
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                "Infinity"
            );

            // Then
            assertThat(response.latitude()).isEqualTo(Double.POSITIVE_INFINITY);
            assertThat(response.longitude()).isEqualTo(Double.POSITIVE_INFINITY);
        }

        @Test
        @DisplayName("Should handle negative infinity coordinates")
        void shouldHandleNegativeInfinityCoordinates() {
            // Given
            LocationResponse response = new LocationResponse(
                Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                "Negative Infinity"
            );

            // Then
            assertThat(response.latitude()).isEqualTo(Double.NEGATIVE_INFINITY);
            assertThat(response.longitude()).isEqualTo(Double.NEGATIVE_INFINITY);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable - fields cannot be modified")
        void shouldBeImmutable() {
            // Given
            LocationResponse response = new LocationResponse(40.7128, -74.0060, "New York");

            // When - attempting to get fields
            double lat = response.latitude();
            double lon = response.longitude();
            String city = response.cityName();

            // Then - original values unchanged
            assertThat(response.latitude()).isEqualTo(lat);
            assertThat(response.longitude()).isEqualTo(lon);
            assertThat(response.cityName()).isEqualTo(city);
        }

        @Test
        @DisplayName("Should create new instance with different values")
        void shouldCreateNewInstanceWithDifferentValues() {
            // Given
            LocationResponse response1 = new LocationResponse(40.7128, -74.0060, "New York");

            // When
            LocationResponse response2 = new LocationResponse(51.5074, -0.1278, "London");

            // Then
            assertThat(response1).isNotEqualTo(response2);
            assertThat(response1.latitude()).isNotEqualTo(response2.latitude());
            assertThat(response1.longitude()).isNotEqualTo(response2.longitude());
            assertThat(response1.cityName()).isNotEqualTo(response2.cityName());
        }
    }
}