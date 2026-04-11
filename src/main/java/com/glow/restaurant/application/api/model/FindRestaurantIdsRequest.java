package com.glow.restaurant.application.api.model;

public record FindRestaurantIdsRequest(
    int page,
    int size
) {
}
