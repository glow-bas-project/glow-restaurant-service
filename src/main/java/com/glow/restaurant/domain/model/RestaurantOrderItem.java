package com.glow.restaurant.domain.model;

import java.util.UUID;

public record RestaurantOrderItem(
    UUID menuItemId,
    String name,
    Integer price,
    Integer quantity
) {}