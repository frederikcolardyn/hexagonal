package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.ManageUserPreferencesUseCase;
import io.github.hexagonal.weather.application.port.out.UserPreferencesRepository;
import io.github.hexagonal.weather.model.UserPreferences;
import lombok.extern.jbosslog.JBossLog;

/**
 * Service for managing user weather preferences.
 */
@JBossLog
public class UserPreferencesService implements ManageUserPreferencesUseCase {

    private final UserPreferencesRepository repository;

    public UserPreferencesService(UserPreferencesRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserPreferences savePreferences(UserPreferences preferences) {
        log.infof("Saving preferences for user: %s", preferences.userId());
        return repository.save(preferences);
    }

    @Override
    public UserPreferences getPreferences(String userId) {
        log.infof("Retrieving preferences for user: %s", userId);
        return repository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User preferences not found for: " + userId));
    }
}