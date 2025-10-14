package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.ManageUserPreferencesUseCase;
import io.github.hexagonal.weather.application.port.out.UserPreferencesRepository;
import io.github.hexagonal.weather.model.UserPreferences;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service for managing user preferences.
 */
@ApplicationScoped
public class UserPreferencesService implements ManageUserPreferencesUseCase {

    private final UserPreferencesRepository repository;

    public UserPreferencesService(UserPreferencesRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserPreferences savePreferences(UserPreferences preferences) {
        return repository.save(preferences);
    }

    @Override
    public UserPreferences getPreferences(String userId) {
        return repository.findByUserId(userId)
                .orElse(new UserPreferences(userId));
    }
}
