package com.glow.restaurant.application.model;

import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.RestaurantOrderItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RestaurantOrderDto(
    UUID id,
    UUID orderId,
    UUID restaurantId,
    Instant createdAt,
    Address deliveryAddress,
    String customerPhone,
    List<RestaurantOrderItem> items,
    Integer totalPrice,
    String status
) {}