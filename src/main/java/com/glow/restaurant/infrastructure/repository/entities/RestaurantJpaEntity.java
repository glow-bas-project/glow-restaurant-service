package com.glow.restaurant.infrastructure.repository.entities;

import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "restaurant")
public class RestaurantJpaEntity {

    @Id
    private String id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Embedded
    private AddressEntity address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "opening_hours", columnDefinition = "jsonb")
    private WeeklyOpeningHours openingHours;

    public RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(String id, Instant createdAt, Instant updatedAt, String name, String phoneNumber,
                               AddressEntity address, WeeklyOpeningHours openingHours) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.openingHours = openingHours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public WeeklyOpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(WeeklyOpeningHours openingHours) {
        this.openingHours = openingHours;
    }
}
