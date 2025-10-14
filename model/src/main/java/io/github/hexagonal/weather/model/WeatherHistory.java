package io.github.hexagonal.weather.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Domain model representing historical weather data.
 * Stores weather observations for a location at a specific point in time.
 */
@Entity
@Table(name = "weather_history")
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Valid
    @NotNull
    private Location location;

    @Column(name = "temperature_celsius")
    private double temperature;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_condition")
    @NotNull
    private WeatherCondition condition;

    @Column(name = "observed_at")
    @NotNull
    private Instant timestamp;

    @Column(name = "created_at")
    private Instant createdAt;

    // JPA requires no-arg constructor
    protected WeatherHistory() {
    }

    public WeatherHistory(Location location, double temperature, WeatherCondition condition, Instant timestamp) {
        this.location = location;
        this.temperature = temperature;
        this.condition = condition;
        this.timestamp = timestamp;
        this.createdAt = Instant.now();
    }

    // Factory method from Weather domain object
    public static WeatherHistory fromWeather(Weather weather) {
        return new WeatherHistory(
            weather.location(),
            weather.temperature(),
            weather.condition(),
            weather.timestamp()
        );
    }

    // Getters for JPA
    public Long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public double getTemperature() {
        return temperature;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
