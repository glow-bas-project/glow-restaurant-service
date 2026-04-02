package com.glow.restaurant.infrastructure.mappers;

import com.glow.restaurant.domain.model.Restaurant;
import com.glow.restaurant.infrastructure.repository.entities.RestaurantJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RestaurantEntityMapper {

    RestaurantJpaEntity toEntity(Restaurant restaurant);
    Restaurant toDomain(RestaurantJpaEntity entity);
}
