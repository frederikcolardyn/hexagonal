package io.github.hexagonal.weather.adapter.preferences;

import io.github.hexagonal.weather.application.port.out.UserPreferencesRepository;
import io.github.hexagonal.weather.model.UserPreferences;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of user preferences repository.
 */
@ApplicationScoped
public class InMemoryUserPreferencesRepository implements UserPreferencesRepository {

    private final Map<String, UserPreferences> storage = new ConcurrentHashMap<>();

    @Override
    public UserPreferences save(UserPreferences preferences) {
        storage.put(preferences.userId(), preferences);
        return preferences;
    }

    @Override
    public Optional<UserPreferences> findByUserId(String userId) {
        return Optional.ofNullable(storage.get(userId));
    }
}
