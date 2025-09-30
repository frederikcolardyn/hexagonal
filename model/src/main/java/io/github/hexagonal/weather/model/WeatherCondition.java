package io.github.hexagonal.weather.model;

/**
 * Enum representing weather conditions based on WMO Weather interpretation codes.
 * Simplified version for demonstration purposes.
 */
public enum WeatherCondition {
    CLEAR("Clear sky"),
    PARTLY_CLOUDY("Partly cloudy"),
    CLOUDY("Cloudy"),
    FOG("Fog"),
    DRIZZLE("Drizzle"),
    RAIN("Rain"),
    SNOW("Snow"),
    THUNDERSTORM("Thunderstorm"),
    UNKNOWN("Unknown");

    private final String description;

    WeatherCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Maps WMO weather code to WeatherCondition.
     * Based on https://open-meteo.com/en/docs
     */
    public static WeatherCondition fromWmoCode(int code) {
        return switch (code) {
            case 0 -> CLEAR;
            case 1, 2 -> PARTLY_CLOUDY;
            case 3 -> CLOUDY;
            case 45, 48 -> FOG;
            case 51, 53, 55, 56, 57 -> DRIZZLE;
            case 61, 63, 65, 66, 67, 80, 81, 82 -> RAIN;
            case 71, 73, 75, 77, 85, 86 -> SNOW;
            case 95, 96, 99 -> THUNDERSTORM;
            default -> UNKNOWN;
        };
    }
}