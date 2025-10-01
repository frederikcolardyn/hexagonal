package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.UserPreferences;

/**
 * Use case for managing user preferences.
 */
public interface ManageUserPreferencesUseCase {

    /**
     * Saves or updates user preferences.
     *
     * @param preferences The user preferences to save
     * @return The saved preferences
     */
    UserPreferences savePreferences(UserPreferences preferences);

    /**
     * Retrieves user preferences by user ID.
     *
     * @param userId The user identifier
     * @return User preferences if found
     */
    UserPreferences getPreferences(String userId);
}