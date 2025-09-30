package io.github.hexagonal.weather.adapter.rest.mapper;

import io.github.hexagonal.weather.adapter.rest.dto.WeatherResponse;
import io.github.hexagonal.weather.model.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting domain models to REST DTOs.
 */
@Mapper(componentModel = "jakarta")
public interface WeatherRestMapper {

    @Mapping(target = "temperatureCelsius", source = "temperature")
    @Mapping(target = "condition", expression = "java(weather.condition().name())")
    @Mapping(target = "conditionDescription", expression = "java(weather.condition().getDescription())")
    @Mapping(target = "timestamp", expression = "java(weather.timestamp().toString())")
    @Mapping(target = "location.latitude", source = "location.latitude")
    @Mapping(target = "location.longitude", source = "location.longitude")
    @Mapping(target = "location.cityName", source = "location.cityName")
    WeatherResponse toResponse(Weather weather);
}