package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.adapter.rest.dto.UserPreferencesRequest;
import io.github.hexagonal.weather.adapter.rest.dto.UserPreferencesResponse;
import io.github.hexagonal.weather.adapter.rest.mapper.UserPreferencesRestMapper;
import io.github.hexagonal.weather.application.port.in.ManageUserPreferencesUseCase;
import io.github.hexagonal.weather.model.UserPreferences;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * REST controller for user preferences management.
 */
@Path("/preferences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPreferencesController {

    private final ManageUserPreferencesUseCase useCase;
    private final UserPreferencesRestMapper mapper;

    public UserPreferencesController(ManageUserPreferencesUseCase useCase, UserPreferencesRestMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @POST
    public UserPreferencesResponse savePreferences(UserPreferencesRequest request) {
        UserPreferences preferences = mapper.toDomain(request);
        UserPreferences saved = useCase.savePreferences(preferences);
        return mapper.toResponse(saved);
    }

    @GET
    @Path("/{userId}")
    public UserPreferencesResponse getPreferences(@PathParam("userId") String userId) {
        UserPreferences preferences = useCase.getPreferences(userId);
        return mapper.toResponse(preferences);
    }
}
