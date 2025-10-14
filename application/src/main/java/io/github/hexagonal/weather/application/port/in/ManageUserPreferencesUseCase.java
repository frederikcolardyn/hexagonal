package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.UserPreferences;

/**
 * Use case for managing user preferences.
 */
public interface ManageUserPreferencesUseCase {

    /**
     * Saves or updates user preferences.
     *
     * @param preferences User preferences to save
     * @return Saved preferences
     */
    UserPreferences savePreferences(UserPreferences preferences);

    /**
     * Retrieves user preferences by user ID.
     *
     * @param userId User identifier
     * @return User preferences or default if not found
     */
    UserPreferences getPreferences(String userId);
}
