package io.github.hexagonal.weather.bootstrap;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

/**
 * Main application entry point for the Weather Service.
 */
@QuarkusMain
public class WeatherApplication implements QuarkusApplication {

    private static final Logger log = Logger.getLogger(WeatherApplication.class);

    public static void main(String[] args) {
        Quarkus.run(WeatherApplication.class, args);
    }

    @Override
    public int run(String... args) {
        log.info("Weather Service started successfully");
        Quarkus.waitForExit();
        return 0;
    }
}