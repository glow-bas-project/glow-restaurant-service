package com.glow.restaurant.domain.model.openinghours;

import java.util.List;

public record WeeklyOpeningHours(List<DailyOpeningHours> dailyOpeningHours) {
}
