package com.glow.restaurant.application.services;

import com.glow.restaurant.application.api.model.NotifyRestaurantRequest;
import com.glow.restaurant.application.model.RestaurantOrderDto;
import com.glow.restaurant.domain.model.RestaurantOrder;
import com.glow.restaurant.infrastructure.clients.OrderApiClient;
import com.glow.restaurant.infrastructure.clients.OrderStatusRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RestaurantOrderService {

    private static final Logger LOG = Logger.getLogger(RestaurantOrderService.class);

    private final ConcurrentHashMap<UUID, List<RestaurantOrder>> store = new ConcurrentHashMap<>();
    private final OrderApiClient orderApiClient;

    public RestaurantOrderService(@RestClient OrderApiClient orderApiClient) {
        this.orderApiClient = orderApiClient;
    }

    public RestaurantOrderDto notifyOrder(UUID restaurantId, NotifyRestaurantRequest request) {
        LOG.infof("Restaurant %s notified of order %s", restaurantId, request.orderId());

        var orders = store.computeIfAbsent(restaurantId, k -> new ArrayList<>());

        boolean alreadyExists = orders.stream()
            .anyMatch(o -> o.getOrderId().equals(request.orderId()));

        if (alreadyExists) {
            LOG.infof("Order %s already exists for restaurant %s, skipping", request.orderId(), restaurantId);
            return orders.stream()
                .filter(o -> o.getOrderId().equals(request.orderId()))
                .findFirst()
                .map(this::toDto)
                .orElseThrow();
        }

        var order = new RestaurantOrder(
            request.orderId(), restaurantId, request.deliveryAddress(),
            request.customerPhone(), request.items(), request.totalPrice());

        orders.add(order);
        return toDto(order);
    }

    public List<RestaurantOrderDto> getOrdersForRestaurant(UUID restaurantId) {
        return store.getOrDefault(restaurantId, List.of())
            .stream()
            .map(this::toDto)
            .toList();
    }

    public RestaurantOrderDto updateOrderStatus(UUID restaurantId, UUID orderId, String newStatus) {
        var orders = store.getOrDefault(restaurantId, List.of());

        RestaurantOrder order = orders.stream()
            .filter(o -> o.getOrderId().equals(orderId))
            .findFirst()
            .orElseThrow(() -> new com.glow.restaurant.domain.shared.DomainException(
                "Order not found: " + orderId));

        order.setStatus(newStatus);

        // Sync to order-service
        try {
            orderApiClient.updateOrderStatus(orderId.toString(), new OrderStatusRequest(newStatus));
            LOG.infof("Order %s status synced to order-service: %s", orderId, newStatus);
        } catch (Exception e) {
            LOG.warnf("Failed to sync order status to order-service: %s", e.getMessage());
        }

        return toDto(order);
    }

    private RestaurantOrderDto toDto(RestaurantOrder order) {
        return new RestaurantOrderDto(
            order.getId(), order.getOrderId(), order.getRestaurantId(),
            order.getCreatedAt(), order.getDeliveryAddress(), order.getCustomerPhone(),
            order.getItems(), order.getTotalPrice(), order.getStatus());
    }
}