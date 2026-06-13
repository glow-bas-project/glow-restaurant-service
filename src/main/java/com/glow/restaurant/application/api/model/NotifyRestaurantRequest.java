package com.glow.restaurant.application.api.model;

import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.RestaurantOrderItem;

import java.util.List;
import java.util.UUID;

public record NotifyRestaurantRequest(
    UUID orderId,
    Address deliveryAddress,
    String customerPhone,
    List<RestaurantOrderItem> items,
    Integer totalPrice
) {}