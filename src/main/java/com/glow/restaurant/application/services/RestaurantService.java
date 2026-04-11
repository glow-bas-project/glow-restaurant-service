package com.glow.restaurant.application.services;

import com.glow.restaurant.domain.shared.PageRequest;
import com.glow.restaurant.domain.shared.PageResult;
import com.glow.restaurant.application.mappers.RestaurantDtoMapper;
import com.glow.restaurant.application.model.RestaurantDto;
import com.glow.restaurant.domain.model.Address;
import com.glow.restaurant.domain.model.Restaurant;
import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import com.glow.restaurant.domain.repository.RestaurantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class RestaurantService {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final RestaurantDtoMapper mapper;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantDtoMapper mapper) {
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    public RestaurantDto createRestaurant(String name, String phoneNumber, Address address, WeeklyOpeningHours openingHours) {
        var restaurant = Restaurant.builder()
            .name(name)
            .phoneNumber(phoneNumber)
            .address(address)
            .openingHours(openingHours)
            .build();

        restaurantRepository.save(restaurant);
        return mapper.toDto(restaurant);
    }

    public RestaurantDto findById(String id) {
        return mapper.toDto(
            restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found")));
    }

    public PageResult<String> findRestaurantIds(int size, int page) {
        // TODO: Build some defaults but for now this is fine
        return restaurantRepository.findIds(new PageRequest(size, page));
    }

    public List<RestaurantDto> materialise(List<String> ids) {
        return restaurantRepository.findByIds(ids).stream()
            .map(mapper::toDto)
            .toList();
    }

    public boolean deleteRestaurantById(String id) {
        return restaurantRepository.deleteById(id);
    }
}
