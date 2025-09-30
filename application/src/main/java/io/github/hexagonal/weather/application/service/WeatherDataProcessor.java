package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.model.Weather;
import java.util.*;

/**
 * Weather data processor with INTENTIONAL clean code violations for demo
 */
public class WeatherDataProcessor {

    // VIOLATION: Poor naming - single letter variable names
    private Map<String, Object> d = new HashMap<>();
    private int x = 0;

    // VIOLATION: Method too long, does too many things
    // VIOLATION: Magic numbers everywhere
    // VIOLATION: Poor variable naming
    public String processWeatherData(Weather w, boolean flag, String t, int mode) {
        String result = "";

        // VIOLATION: Deeply nested if statements
        if (w != null) {
            if (flag) {
                if (mode == 1) {
                    // VIOLATION: Magic numbers
                    if (w.temperature() > 30) {
                        result = "hot";
                        x = 1;
                    } else if (w.temperature() > 20) {
                        result = "warm";
                        x = 2;
                    } else if (w.temperature() > 10) {
                        result = "cool";
                        x = 3;
                    } else {
                        result = "cold";
                        x = 4;
                    }
                } else if (mode == 2) {
                    // VIOLATION: Copy-paste code
                    if (w.temperature() > 30) {
                        result = "very hot";
                    } else if (w.temperature() > 20) {
                        result = "pleasant";
                    } else if (w.temperature() > 10) {
                        result = "chilly";
                    } else {
                        result = "freezing";
                    }
                } else {
                    result = "unknown";
                }
            } else {
                // VIOLATION: Complex condition
                if ((w.condition().name().equals("CLEAR") || w.condition().name().equals("PARTLY_CLOUDY"))
                    && w.temperature() > 15 && w.temperature() < 25) {
                    result = "perfect weather";
                } else {
                    result = "not ideal";
                }
            }

            // VIOLATION: Side effects in what looks like a pure function
            d.put("lastTemp", w.temperature());
            d.put("lastCondition", w.condition());
            d.put("count", ((Integer) d.getOrDefault("count", 0)) + 1);

            // VIOLATION: String concatenation in loop
            String temp = "";
            for (int i = 0; i < 10; i++) {
                temp += result + " ";
            }
            result = temp.trim();

        } else {
            result = "no data";
        }

        // VIOLATION: Dead code
        int unusedVariable = 42;
        String anotherUnused = "hello";

        return result;
    }

    // VIOLATION: Method that returns different types of data depending on parameter
    public Object getData(String key) {
        return d.get(key);
    }

    // VIOLATION: Public field
    public int counter = 0;

    // VIOLATION: Empty catch block
    public void riskyOperation() {
        try {
            // Some operation
            int result = 10 / 0;
        } catch (Exception e) {
            // VIOLATION: Swallowing exception
        }
    }

    // VIOLATION: Method with too many parameters
    public void complexMethod(String a, String b, String c, String d, String e,
                             String f, String g, String h, int i, int j,
                             boolean k, boolean l, double m, double n) {
        // Does nothing useful
    }

    // VIOLATION: Commented out code
    // public void oldMethod() {
    //     System.out.println("This was the old way");
    //     int x = 5;
    //     int y = 10;
    //     return x + y;
    // }
}