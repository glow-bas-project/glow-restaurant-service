package com.glow.restaurant.infrastructure.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "order-api")
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderApiClient {

    @PATCH
    @Path("/{orderId}/status")
    void updateOrderStatus(@PathParam("orderId") String orderId, OrderStatusRequest request);
}