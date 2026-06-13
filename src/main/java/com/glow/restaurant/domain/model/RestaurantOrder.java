package com.glow.restaurant.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RestaurantOrder {
    private final UUID id;
    private final UUID orderId;
    private final UUID restaurantId;
    private final Instant createdAt;
    private final Address deliveryAddress;
    private final String customerPhone;
    private final List<RestaurantOrderItem> items;
    private final Integer totalPrice;
    private String status;

    public RestaurantOrder(UUID orderId, UUID restaurantId, Address deliveryAddress,
                           String customerPhone, List<RestaurantOrderItem> items, Integer totalPrice) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.createdAt = Instant.now();
        this.deliveryAddress = deliveryAddress;
        this.customerPhone = customerPhone;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = "PROCESSING";
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public UUID getRestaurantId() { return restaurantId; }
    public Instant getCreatedAt() { return createdAt; }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public String getCustomerPhone() { return customerPhone; }
    public List<RestaurantOrderItem> getItems() { return items; }
    public Integer getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}