package io.github.hexagonal.weather.application.port.out;

import io.github.hexagonal.weather.model.UserPreferences;

import java.util.Optional;

/**
 * Repository port for user preferences persistence.
 */
public interface UserPreferencesRepository {

    /**
     * Saves user preferences.
     *
     * @param preferences Preferences to save
     * @return Saved preferences
     */
    UserPreferences save(UserPreferences preferences);

    /**
     * Finds user preferences by user ID.
     *
     * @param userId User identifier
     * @return Optional containing preferences if found
     */
    Optional<UserPreferences> findByUserId(String userId);
}
