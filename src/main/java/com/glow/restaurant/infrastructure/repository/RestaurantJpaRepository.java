package com.glow.restaurant.infrastructure.repository;

import com.glow.restaurant.domain.shared.PageRequest;
import com.glow.restaurant.domain.shared.PageResult;
import com.glow.restaurant.infrastructure.repository.entities.RestaurantJpaEntity;
import com.glow.restaurant.infrastructure.repository.projections.RestaurantIdProjection;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RestaurantJpaRepository implements PanacheRepositoryBase<RestaurantJpaEntity, String> {

    public PageResult<String> findIds(PageRequest pageRequest) {
        var query = findAll()
            .page(pageRequest.page(), pageRequest.size())
            .project(RestaurantIdProjection.class);
        var hasNext = query.hasNextPage();

        return new PageResult<>(
            query.list().stream().map(RestaurantIdProjection::id).toList(),
            hasNext ? pageRequest.page() + 1 : pageRequest.page(),
            !hasNext);
    }

    public List<RestaurantJpaEntity> findRestaurantsByIds(List<String> ids) {
        return list("id in ?1", ids);
    }
}
