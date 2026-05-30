package com.glow.restaurant.application.api;

import com.glow.restaurant.application.api.model.CreateRestaurantRequest;
import com.glow.restaurant.application.api.model.FindRestaurantIdsRequest;
import com.glow.restaurant.application.api.model.FindRestaurantIdsResponse;
import com.glow.restaurant.application.api.model.MaterialiseRestaurantsByIdsRequest;
import com.glow.restaurant.application.model.RestaurantDto;
import com.glow.restaurant.application.services.RestaurantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
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

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        boolean deleted = service.deleteRestaurantById(id);
        return deleted ? RestResponse.noContent() : RestResponse.status(RestResponse.Status.NOT_FOUND);
    }
}
