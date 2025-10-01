package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.application.port.in.ManageUserPreferencesUseCase;
import io.github.hexagonal.weather.model.UserPreferences;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for managing user weather preferences.
 */
@Path("/preferences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPreferencesController {

    private final ManageUserPreferencesUseCase manageUserPreferencesUseCase;

    @Inject
    public UserPreferencesController(ManageUserPreferencesUseCase manageUserPreferencesUseCase) {
        this.manageUserPreferencesUseCase = manageUserPreferencesUseCase;
    }

    @POST
    public Response savePreferences(UserPreferences preferences) {
        UserPreferences saved = manageUserPreferencesUseCase.savePreferences(preferences);
        return Response.ok(saved).build();
    }

    @GET
    @Path("/{userId}")
    public Response getPreferences(@PathParam("userId") String userId) {
        UserPreferences preferences = manageUserPreferencesUseCase.getPreferences(userId);
        return Response.ok(preferences).build();
    }
}