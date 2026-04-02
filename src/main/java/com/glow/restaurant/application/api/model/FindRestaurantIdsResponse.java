package com.glow.restaurant.application.api.model;

import java.util.List;

public record FindRestaurantIdsResponse(
    List<String> ids,
    int nextPage,
    boolean hasNext
) {
}
