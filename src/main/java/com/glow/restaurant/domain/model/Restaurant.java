package com.glow.restaurant.domain.model;

import com.glow.restaurant.domain.model.openinghours.WeeklyOpeningHours;
import com.glow.restaurant.domain.shared.DomainPrecondition;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Restaurant {

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String name;
    private final String phoneNumber;
    private final Address address;
    private final WeeklyOpeningHours openingHours;

    private Restaurant(Builder builder) {
        if (Objects.isNull(builder.id)) {
            this.id = UUID.randomUUID();
        } else {
            this.id = builder.id;
        }

        if (Objects.isNull(builder.createdAt)) {
            this.createdAt = Instant.now();
        } else {
            this.createdAt = builder.createdAt;
        }

        if (Objects.isNull(builder.updatedAt)) {
            this.updatedAt = Instant.now();
        } else {
            this.updatedAt = builder.updatedAt;
        }

        this.name = DomainPrecondition.requireNonBlank(builder.name,
            "Restaurant name cannot be null or empty");
        this.phoneNumber = DomainPrecondition.requireNonBlank(builder.phoneNumber,
            "Restaurant phone number cannot be null or empty");
        this.address = DomainPrecondition.requireNonNull(builder.address,
                        "Restaurant address cannot be null");

        DomainPrecondition.requireNonNull(builder.openingHours,
            "Restaurant opening hours cannot be null");
        DomainPrecondition.requireNonEmpty(builder.openingHours.dailyOpeningHours(),
            "Restaurant opening hours cannot be empty");
        this.openingHours = builder.openingHours;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public WeeklyOpeningHours getOpeningHours() {
        return openingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) && Objects.equals(name, that.name) &&
            Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(address, that.address) &&
            Objects.equals(openingHours, that.openingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, name, phoneNumber, address, openingHours);
    }

    public static class Builder {

        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private String name;
        private String phoneNumber;
        private Address address;
        private WeeklyOpeningHours openingHours;

        public Restaurant build() {
            return new Restaurant(this);
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public WeeklyOpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(WeeklyOpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public Builder openingHours(WeeklyOpeningHours openingHours) {
            this.openingHours = openingHours;
            return this;
        }
    }
}
