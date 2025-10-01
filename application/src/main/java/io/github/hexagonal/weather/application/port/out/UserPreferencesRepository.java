package io.github.hexagonal.weather.application.port.out;

import io.github.hexagonal.weather.model.UserPreferences;

import java.util.Optional;

/**
 * Repository interface for user preferences persistence.
 */
public interface UserPreferencesRepository {

    /**
     * Saves user preferences.
     *
     * @param preferences The preferences to save
     * @return The saved preferences
     */
    UserPreferences save(UserPreferences preferences);

    /**
     * Finds user preferences by user ID.
     *
     * @param userId The user identifier
     * @return Optional containing preferences if found
     */
    Optional<UserPreferences> findByUserId(String userId);
}