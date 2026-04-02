package com.glow.restaurant.domain.model;

import com.glow.restaurant.domain.model.openinghours.DailyOpeningHours;
import com.glow.restaurant.domain.model.openinghours.TimeRange;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import com.glow.restaurant.domain.shared.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void build_rejectsBlankName(String name) {
        // given
        var builder = Restaurant.builder()
            .name(name)
            .phoneNumber("+123")
            .address(validAddress())
            .openingHours(validHours());

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Restaurant name cannot be null or empty", ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void build_rejectsBlankPhone(String phone) {
        // given
        var builder = Restaurant.builder()
            .name("Cafe")
            .phoneNumber(phone)
            .address(validAddress())
            .openingHours(validHours());

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Restaurant phone number cannot be null or empty", ex.getMessage());
    }

    @Test
    public void build_rejectsNullAddress() {
        // given
        var builder = Restaurant.builder()
            .name("Cafe")
            .phoneNumber("+123")
            .address(null)
            .openingHours(validHours());

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Restaurant address cannot be null", ex.getMessage());
    }

    @Test
    public void build_rejectsNullOpeningHours() {
        // given
        var builder = Restaurant.builder()
            .name("Cafe")
            .phoneNumber("+123")
            .address(validAddress())
            .openingHours(null);

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Restaurant opening hours cannot be null", ex.getMessage());
    }

    @Test
    public void build_rejectsEmptyOpeningHoursList() {
        // given
        var builder = Restaurant.builder()
            .name("Cafe")
            .phoneNumber("+123")
            .address(validAddress())
            .openingHours(new WeeklyOpeningHours(List.of()));

        // when
        var ex = assertThrows(DomainException.class, builder::build);

        // then
        assertEquals("Restaurant opening hours cannot be empty", ex.getMessage());
    }

    private static WeeklyOpeningHours validHours() {
        return new WeeklyOpeningHours(List.of(
            new DailyOpeningHours(
                DayOfWeek.MONDAY,
                List.of(new TimeRange(LocalTime.of(9, 0), LocalTime.of(17, 0))),
                false)));
    }

    private static Address validAddress() {
        return new Address("1 Main St", "City", "Country", 0.0, 0.0);
    }
}
