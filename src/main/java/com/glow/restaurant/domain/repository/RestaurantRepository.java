package com.glow.restaurant.domain.repository;

import com.glow.restaurant.domain.shared.PageRequest;
import com.glow.restaurant.domain.shared.PageResult;
import com.glow.restaurant.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    void save(Restaurant restaurant);

    Optional<Restaurant> findById(String id);
    PageResult<String> findIds(PageRequest pageRequest);
    List<Restaurant> findByIds(List<String> ids);

    boolean deleteById(String id);
}
