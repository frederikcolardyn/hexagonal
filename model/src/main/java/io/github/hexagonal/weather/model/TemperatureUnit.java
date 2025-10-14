package io.github.hexagonal.weather.model;

/**
 * Represents temperature unit preferences.
 */
public enum TemperatureUnit {
    CELSIUS,
    FAHRENHEIT;

    public double convert(double celsius) {
        return switch (this) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> (celsius * 9.0 / 5.0) + 32.0;
        };
    }
}
