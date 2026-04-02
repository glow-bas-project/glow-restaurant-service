package com.glow.restaurant.application.api.model;

import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;

public record CreateRestaurantRequest(
    String name,
    String phoneNumber,
    Address address,
    WeeklyOpeningHours openingHours) {
}
