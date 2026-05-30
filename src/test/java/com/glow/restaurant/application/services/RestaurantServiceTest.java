package com.glow.restaurant.application.services;

import com.glow.restaurant.application.mappers.RestaurantDtoMapper;
import com.glow.restaurant.application.model.RestaurantDto;
import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.Restaurant;
import com.glow.restaurant.domain.model.openinghours.DailyOpeningHours;
import com.glow.restaurant.domain.model.openinghours.TimeRange;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import com.glow.restaurant.domain.repository.RestaurantRepository;
import com.glow.restaurant.domain.shared.PageRequest;
import com.glow.restaurant.domain.shared.PageResult;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    RestaurantDtoMapper mapper;

    @InjectMocks
    RestaurantService service;

    @Test
    public void createRestaurant_savesAndMapsToDto() {
        // given
        var address = createAddress();
        var hours = createHours();
        when(mapper.toDto(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant r = invocation.getArgument(0);
            return new RestaurantDto(
                r.getId().toString(),
                r.getName(),
                r.getPhoneNumber(),
                r.getAddress(),
                r.getOpeningHours());
        });

        // when
        var dto = service.createRestaurant("Cafe", "+1234567890", address, hours);

        // then
        assertEquals("Cafe", dto.name());
        assertEquals("+1234567890", dto.phoneNumber());
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(mapper).toDto(any(Restaurant.class));
    }

    @Test
    public void findById_throwsWhenMissing() {
        // given
        when(restaurantRepository.findById("missing")).thenReturn(Optional.empty());

        // when / then
        assertThrows(NotFoundException.class, () -> service.findById("missing"));
    }

    @Test
    public void findById_returnsDtoWhenPresent() {
        // given
        var restaurant = Restaurant.builder()
            .name("Cafe")
            .phoneNumber("+1")
            .address(createAddress())
            .openingHours(createHours())
            .build();
        var expected = new RestaurantDto(
            restaurant.getId().toString(),
            restaurant.getName(),
            restaurant.getPhoneNumber(),
            restaurant.getAddress(),
            restaurant.getOpeningHours());
        when(restaurantRepository.findById(expected.id())).thenReturn(Optional.of(restaurant));
        when(mapper.toDto(restaurant)).thenReturn(expected);

        // when
        var actual = service.findById(expected.id());

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void findRestaurants_filtersBySearchTerm() {
        // given
        var restaurant = Restaurant.builder()
            .name("Glow Kitchen")
            .phoneNumber("+45 12 34 56 78")
            .address(new Address("1 Main St", "Copenhagen", "Denmark", 0.0, 0.0))
            .openingHours(createHours())
            .build();
        var dto = new RestaurantDto(
            restaurant.getId().toString(),
            restaurant.getName(),
            restaurant.getPhoneNumber(),
            restaurant.getAddress(),
            restaurant.getOpeningHours());
        when(restaurantRepository.findAllRestaurants()).thenReturn(List.of(restaurant));
        when(mapper.toDto(restaurant)).thenReturn(dto);

        // when
        var actual = service.findRestaurants("kitchen");

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    public void findRestaurants_returnsAllWhenSearchIsBlank() {
        // given
        var restaurant = Restaurant.builder()
            .name("Glow Kitchen")
            .phoneNumber("+45 12 34 56 78")
            .address(createAddress())
            .openingHours(createHours())
            .build();
        var dto = new RestaurantDto(
            restaurant.getId().toString(),
            restaurant.getName(),
            restaurant.getPhoneNumber(),
            restaurant.getAddress(),
            restaurant.getOpeningHours());
        when(restaurantRepository.findAllRestaurants()).thenReturn(List.of(restaurant));
        when(mapper.toDto(restaurant)).thenReturn(dto);

        // when
        var actual = service.findRestaurants("   ");

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    public void findRestaurantIds_delegatesToRepository() {
        // given
        var pageResult = new PageResult<>(List.of(UUID.randomUUID().toString()), 1, true);
        when(restaurantRepository.findIds(any(PageRequest.class))).thenReturn(pageResult);

        // when
        var actual = service.findRestaurantIds(10, 0);

        // then
        assertEquals(pageResult, actual);
        var captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(restaurantRepository).findIds(captor.capture());
        assertEquals(10, captor.getValue().size());
        assertEquals(0, captor.getValue().page());
    }

    @Test
    public void materialise_mapsEachRestaurant() {
        // given
        var r = Restaurant.builder()
            .name("Cafe")
            .phoneNumber("+1")
            .address(createAddress())
            .openingHours(createHours())
            .build();
        var id = r.getId().toString();
        var dto = new RestaurantDto(id, r.getName(), r.getPhoneNumber(), r.getAddress(), r.getOpeningHours());
        when(restaurantRepository.findByIds(List.of(id))).thenReturn(List.of(r));
        when(mapper.toDto(r)).thenReturn(dto);

        // when
        var actual = service.materialise(List.of(id));

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    public void deleteRestaurantById_returnsTrueWhenRepositoryDeletes() {
        // given
        when(restaurantRepository.deleteById("x")).thenReturn(true);

        // when
        var deleted = service.deleteRestaurantById("x");

        // then
        assertTrue(deleted);
    }

    @Test
    public void deleteRestaurantById_returnsFalseWhenRepositoryDoesNotDelete() {
        // given
        when(restaurantRepository.deleteById("y")).thenReturn(false);

        // when
        var deleted = service.deleteRestaurantById("y");

        // then
        assertFalse(deleted);
    }

    private static WeeklyOpeningHours createHours() {
        return new WeeklyOpeningHours(List.of(
            new DailyOpeningHours(
                DayOfWeek.MONDAY,
                List.of(new TimeRange(LocalTime.of(9, 0), LocalTime.of(17, 0))),
                false)));
    }

    private static Address createAddress() {
        return new Address("1 Main St", "City", "Country", 0.0, 0.0);
    }
}
