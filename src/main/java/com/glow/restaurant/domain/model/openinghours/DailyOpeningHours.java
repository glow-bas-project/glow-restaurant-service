package com.glow.restaurant.domain.model.openinghours;

import java.time.DayOfWeek;
import java.util.List;

public record DailyOpeningHours(
    DayOfWeek dayOfWeek,
    List<TimeRange> timeRanges,
    boolean closed) {
}
