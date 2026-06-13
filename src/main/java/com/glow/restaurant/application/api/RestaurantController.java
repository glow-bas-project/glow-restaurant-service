package com.glow.restaurant.application.api;

import com.glow.restaurant.application.api.model.CreateRestaurantRequest;
import com.glow.restaurant.application.api.model.FindRestaurantIdsRequest;
import com.glow.restaurant.application.api.model.FindRestaurantIdsResponse;
import com.glow.restaurant.application.api.model.MaterialiseRestaurantsByIdsRequest;
import com.glow.restaurant.application.model.RestaurantDto;
import com.glow.restaurant.application.services.RestaurantService;
import com.glow.restaurant.application.model.RestaurantOrderDto;
import com.glow.restaurant.application.services.RestaurantOrderService;
import com.glow.restaurant.application.api.model.NotifyRestaurantRequest;
import com.glow.restaurant.application.api.model.UpdateOrderStatusRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.UUID;
import java.util.List;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantController {

    private final RestaurantService service;
    private final RestaurantOrderService restaurantOrderService;

    public RestaurantController(RestaurantService service, RestaurantOrderService restaurantOrderService) {
        this.service = service;
        this.restaurantOrderService = restaurantOrderService;
    }

    @GET
    public RestResponse<List<RestaurantDto>> getRestaurants(@QueryParam("search") String search) {
        return RestResponse.ok(service.findRestaurants(search));
    }

    @POST
    public RestResponse<RestaurantDto> createRestaurant(CreateRestaurantRequest request) {
        return RestResponse.ok(service.createRestaurant(request.name(), request.phoneNumber(),
            request.address(), request.openingHours()));
    }

    @GET
    @Path("{id}")
    public RestResponse<RestaurantDto> findById(@PathParam("id") String id) {
        return RestResponse.ok(service.findById(id));
    }

    @GET
    @Path("{id}/orders")
    public RestResponse<List<RestaurantOrderDto>> getOrders(@PathParam("id") String restaurantId) {
        return RestResponse.ok(restaurantOrderService.getOrdersForRestaurant(UUID.fromString(restaurantId)));
    }

    @POST
    @Path("find")
    public RestResponse<FindRestaurantIdsResponse> findRestaurants(FindRestaurantIdsRequest request) {
        var result = service.findRestaurantIds(request.size(), request.page());

        return RestResponse.ok(new FindRestaurantIdsResponse(
            result.result(),
            result.nextPage(),
            !result.lastPage()));
    }

    @POST
    @Path("materialise")
    public RestResponse<List<RestaurantDto>> materialiseRestaurants(MaterialiseRestaurantsByIdsRequest request) {
        return RestResponse.ok(service.materialise(request.ids()));
    }

    @POST
    @Path("{id}/orders")
    public RestResponse<RestaurantOrderDto> notifyOrder(@PathParam("id") String restaurantId, NotifyRestaurantRequest request) {
        var dto = restaurantOrderService.notifyOrder(UUID.fromString(restaurantId), request);
        return RestResponse.status(RestResponse.Status.CREATED, dto);
    }

    @PATCH
    @Path("{id}/orders/{orderId}/status")
    public RestResponse<RestaurantOrderDto> updateOrderStatus(
            @PathParam("id") String restaurantId,
            @PathParam("orderId") String orderId,
            UpdateOrderStatusRequest request) {
        var dto = restaurantOrderService.updateOrderStatus(
            UUID.fromString(restaurantId),
            UUID.fromString(orderId),
            request.status());
        return RestResponse.ok(dto);
    }

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        boolean deleted = service.deleteRestaurantById(id);
        return deleted ? RestResponse.noContent() : RestResponse.status(RestResponse.Status.NOT_FOUND);
    }
}
