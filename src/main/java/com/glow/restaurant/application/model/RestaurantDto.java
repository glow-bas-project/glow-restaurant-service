package com.glow.restaurant.application.model;

import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;

public record RestaurantDto(
    String id,
    String name,
    String phoneNumber,
    Address address,
    WeeklyOpeningHours openingHours) {
}
