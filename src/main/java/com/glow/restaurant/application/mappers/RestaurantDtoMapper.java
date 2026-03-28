package com.glow.restaurant.application.mappers;

import com.glow.restaurant.application.model.RestaurantDto;
import com.glow.restaurant.domain.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RestaurantDtoMapper {

    RestaurantDto toDto(Restaurant restaurant);
    Restaurant toDomain(RestaurantDto dto);
}
