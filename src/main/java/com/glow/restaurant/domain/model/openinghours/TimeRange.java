package com.glow.restaurant.domain.model.openinghours;

import java.time.LocalTime;

public record TimeRange(
    LocalTime open,
    LocalTime close) {
}
