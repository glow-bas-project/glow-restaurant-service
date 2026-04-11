package com.glow.restaurant.infrastructure.services;

import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.Restaurant;
import com.glow.restaurant.domain.model.openinghours.DailyOpeningHours;
import com.glow.restaurant.domain.model.openinghours.TimeRange;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import com.glow.restaurant.domain.shared.PageRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class RestaurantRepositoryServiceIntegrationTest {

    @Inject
    RestaurantRepositoryService repository;

    @Test
    @Transactional
    public void saveAndFindById_roundTrips() {
        // given
        var restaurant = newRestaurant("Integration Cafe");

        // when
        repository.save(restaurant);
        var id = restaurant.getId().toString();
        var found = repository.findById(id);

        // then
        assertTrue(found.isPresent());
        assertEquals("Integration Cafe", found.get().getName());
        assertEquals("+1234567890", found.get().getPhoneNumber());
    }

    @Test
    @Transactional
    public void findIds_returnsPersistedId() {
        // given
        var restaurant = newRestaurant("Paged");
        repository.save(restaurant);
        String id = restaurant.getId().toString();

        // when
        var page = repository.findIds(new PageRequest(10, 0));

        // then
        assertTrue(page.result().contains(id));
    }

    @Test
    @Transactional
    public void deleteById_removesRestaurant() {
        // given
        var restaurant = newRestaurant("To Delete");
        repository.save(restaurant);
        String id = restaurant.getId().toString();

        // when
        var deleted = repository.deleteById(id);
        var after = repository.findById(id);

        // then
        assertTrue(deleted);
        assertTrue(after.isEmpty());
    }

    @Test
    @Transactional
    public void findByIds_returnsMatching() {
        // given
        var a = newRestaurant("A");
        var b = newRestaurant("B");
        repository.save(a);
        repository.save(b);
        var ids = List.of(a.getId().toString(), b.getId().toString());

        // when
        var list = repository.findByIds(ids);

        // then
        assertEquals(2, list.size());
    }

    private static Restaurant newRestaurant(String name) {
        return Restaurant.builder()
            .name(name)
            .phoneNumber("+1234567890")
            .address(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .openingHours(createValidHours())
            .build();
    }

    private static WeeklyOpeningHours createValidHours() {
        return new WeeklyOpeningHours(List.of(
            new DailyOpeningHours(
                DayOfWeek.MONDAY,
                List.of(new TimeRange(LocalTime.of(9, 0), LocalTime.of(17, 0))),
                false)));
    }
}
