package com.glow.restaurant.infrastructure.services;

import com.glow.restaurant.domain.shared.PageRequest;
import com.glow.restaurant.domain.shared.PageResult;
import com.glow.restaurant.domain.model.Restaurant;
import com.glow.restaurant.domain.repository.RestaurantRepository;
import com.glow.restaurant.infrastructure.mappers.RestaurantEntityMapper;
import com.glow.restaurant.infrastructure.repository.RestaurantJpaRepository;
import com.glow.restaurant.infrastructure.repository.entities.RestaurantJpaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RestaurantRepositoryService implements RestaurantRepository {

    private final RestaurantJpaRepository repository;
    private final RestaurantEntityMapper mapper;

    public RestaurantRepositoryService(RestaurantJpaRepository repository, RestaurantEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(Restaurant restaurant) {
        RestaurantJpaEntity entity = mapper.toEntity(restaurant);
        repository.persist(entity);
    }

    @Override
    public Optional<Restaurant> findById(String id) {
        return repository.findByIdOptional(id)
            .map(mapper::toDomain);
    }

    @Override
    public PageResult<String> findIds(PageRequest pageRequest) {
        var result = repository.findIds(pageRequest);

        return new PageResult<>(
            result.result(),
            result.nextPage(),
            result.lastPage());
    }

    @Override
    public List<Restaurant> findByIds(List<String> ids) {
        return repository.findRestaurantsByIds(ids).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public boolean deleteById(String id) {
        return repository.deleteById(id);
    }
}
