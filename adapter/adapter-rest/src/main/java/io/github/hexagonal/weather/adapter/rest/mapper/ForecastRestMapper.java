package io.github.hexagonal.weather.adapter.rest.mapper;

import io.github.hexagonal.weather.adapter.rest.dto.DailyForecastResponse;
import io.github.hexagonal.weather.adapter.rest.dto.ForecastResponse;
import io.github.hexagonal.weather.adapter.rest.dto.LocationResponse;
import io.github.hexagonal.weather.model.DailyForecast;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting forecast domain models to REST DTOs.
 */
@Mapper(componentModel = "jakarta")
public interface ForecastRestMapper {

    @Mapping(target = "location", source = "location")
    @Mapping(target = "dailyForecasts", source = "dailyForecasts")
    ForecastResponse toResponse(WeatherForecast forecast);

    @Mapping(target = "condition", expression = "java(dailyForecast.condition().name())")
    DailyForecastResponse toDailyResponse(DailyForecast dailyForecast);

    LocationResponse toLocationResponse(Location location);
}
