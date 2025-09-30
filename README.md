# Weather Service - Hexagonal Architecture Example

A practical learning project demonstrating **Hexagonal Architecture** (Ports & Adapters) using Java 21 and Quarkus. This service provides weather information by integrating with the free [Open-Meteo API](https://open-meteo.com/).

## 🎯 Learning Objectives

This project showcases:

- **Hexagonal Architecture**: Clear separation between domain, application, and infrastructure layers
- **Dependency Inversion**: Application core defines interfaces; adapters implement them
- **Domain-Driven Design**: Rich domain models with business logic
- **Testability**: Easy unit and integration testing through well-defined boundaries
- **External System Integration**: Clean adapter pattern for third-party APIs

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Bootstrap                            │
│              (Application Configuration & Wiring)            │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐    ┌──────────────────┐    ┌──────────────┐
│   REST API   │    │   Application    │    │  Open-Meteo  │
│   Adapter    │───▶│      Layer       │◀───│   Adapter    │
│  (Incoming)  │    │  (Use Cases &    │    │  (Outgoing)  │
│              │    │     Ports)       │    │              │
└──────────────┘    └──────────────────┘    └──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │    Domain    │
                    │    Model     │
                    └──────────────┘
```

### Layers

#### 1. **Domain Layer** (`model/`)
- Pure business logic and domain models
- No external dependencies (except validation annotations)
- Contains: `Weather`, `Location`, `WeatherCondition`, domain exceptions

#### 2. **Application Layer** (`application/`)
- Use cases and business orchestration
- Defines ports (interfaces):
  - **Incoming Ports** (`port/in/`): What the application can do (`GetWeatherUseCase`)
  - **Outgoing Ports** (`port/out/`): What the application needs (`WeatherProvider`)
- Service implementations coordinate domain logic

#### 3. **Adapter Layer** (`adapter/`)
- **adapter-rest**: Incoming adapter exposing REST endpoints
- **adapter-openmeteo**: Outgoing adapter integrating with Open-Meteo API
- Adapters translate between external formats and domain models

#### 4. **Bootstrap Layer** (`bootstrap/`)
- Application entry point
- Dependency injection configuration
- Wires adapters to application ports

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Maven 3.9+

### Running the Application

```bash
# From project root
./mvnw quarkus:dev
```

The application starts at: `http://localhost:8080`

Development UI available at: `http://localhost:8080/q/dev-ui`

### Example API Calls

**Get weather for Brussels:**
```bash
curl "http://localhost:8080/weather?lat=50.8503&lon=4.3517&city=Brussels"
```

**Response:**
```json
{
  "location": {
    "latitude": 50.8503,
    "longitude": 4.3517,
    "city_name": "Brussels"
  },
  "temperature_celsius": 18.5,
  "condition": "PARTLY_CLOUDY",
  "condition_description": "Partly cloudy",
  "timestamp": "2025-01-15T14:30:00Z"
}
```

**Get weather without city name:**
```bash
curl "http://localhost:8080/weather?lat=51.5074&lon=-0.1278"
```

## 🧪 Testing

### Run All Tests
```bash
./mvnw verify
```

### Test Structure

1. **Unit Tests** (`*Test.java`)
   - Test business logic in isolation
   - Use Mockito for dependencies
   - Example: `WeatherServiceTest`, `LocationTest`

2. **Integration Tests** (`*IntegrationTest.java`)
   - End-to-end testing with `@QuarkusTest`
   - Test full request/response cycle
   - Example: `WeatherControllerIntegrationTest`

3. **Architecture Tests** (`HexagonalArchitectureTest.java`)
   - Enforce architectural rules using ArchUnit
   - Verify layer dependencies and naming conventions
   - Ensure hexagonal principles are maintained

### Test Coverage
Aim for 80%+ code coverage in the domain and application layers.

```bash
./mvnw verify jacoco:report
# View report at: bootstrap/target/site/jacoco/index.html
```

## 📦 Project Structure

```
weather-service/
├── model/                              # Domain layer
│   └── src/main/java/.../model/
│       ├── Weather.java               # Domain entity
│       ├── Location.java              # Value object
│       ├── WeatherCondition.java      # Enum
│       └── WeatherException.java      # Domain exception
│
├── application/                        # Application layer
│   └── src/main/java/.../application/
│       ├── port/
│       │   ├── in/
│       │   │   └── GetWeatherUseCase.java    # Incoming port
│       │   └── out/
│       │       └── WeatherProvider.java      # Outgoing port
│       └── service/
│           └── WeatherService.java           # Use case implementation
│
├── adapter/                            # Adapter layer
│   ├── adapter-rest/                  # REST API adapter
│   │   └── src/main/java/.../adapter/rest/
│   │       ├── WeatherController.java
│   │       ├── dto/WeatherResponse.java
│   │       └── mapper/WeatherRestMapper.java
│   │
│   └── adapter-openmeteo/             # External API adapter
│       └── src/main/java/.../adapter/openmeteo/
│           ├── OpenMeteoAdapter.java
│           ├── client/OpenMeteoClient.java
│           ├── dto/OpenMeteoResponse.java
│           └── mapper/OpenMeteoMapper.java
│
└── bootstrap/                          # Bootstrap layer
    └── src/main/java/.../bootstrap/
        ├── WeatherApplication.java    # Main entry point
        └── config/ApplicationConfig.java  # CDI configuration
```

## 🔑 Key Concepts Demonstrated

### 1. Dependency Inversion Principle

The application layer defines what it needs through **ports** (interfaces):

```java
// Application defines the interface (port)
public interface WeatherProvider {
    Weather fetchWeather(Location location);
}

// Adapter implements the interface
@ApplicationScoped
public class OpenMeteoAdapter implements WeatherProvider {
    @Override
    public Weather fetchWeather(Location location) {
        // Implementation details
    }
}
```

### 2. Separation of Concerns

Each layer has a single responsibility:
- **Domain**: Business rules and entities
- **Application**: Use case orchestration
- **Adapters**: External world integration
- **Bootstrap**: Configuration and wiring

### 3. Testability

```java
// Easy to test with mocks
@Test
void shouldFetchWeatherSuccessfully() {
    WeatherProvider mockProvider = mock(WeatherProvider.class);
    WeatherService service = new WeatherService(mockProvider);

    when(mockProvider.fetchWeather(location))
        .thenReturn(expectedWeather);

    Weather result = service.getWeather(location);

    assertEquals(expectedWeather, result);
}
```

### 4. Technology Independence

The domain and application layers have no framework dependencies. You could:
- Replace Quarkus with Spring Boot
- Replace REST with GraphQL
- Replace Open-Meteo with another weather API
- Add database persistence

**Only the adapters and bootstrap need to change.**

## 🛠️ Technology Stack

- **Java 21**: Modern Java with records and pattern matching
- **Quarkus 3.15**: Cloud-native Java framework
- **Maven**: Build and dependency management
- **Lombok**: Reduce boilerplate code
- **MapStruct**: Type-safe bean mapping
- **JUnit 5 & Mockito**: Testing framework
- **ArchUnit**: Architecture testing
- **RESTEasy Reactive**: REST endpoints
- **MicroProfile REST Client**: HTTP client

## 📚 Further Learning

### Hexagonal Architecture Resources

- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Netflix Tech Blog - Ready for changes with Hexagonal Architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

### Extension Ideas

1. **Add Caching**: Create a `CachingWeatherProvider` adapter
2. **Add Database**: Store historical weather data
3. **Add Events**: Publish weather events to Kafka
4. **Add GraphQL**: Create a GraphQL adapter alongside REST
5. **Add Authentication**: Implement security in the REST adapter
6. **Add Metrics**: Instrument with Micrometer/Prometheus

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

This is an educational project. Feel free to fork, modify, and experiment!

## 📬 Questions?

Open an issue on GitHub if you have questions about the architecture or implementation.

---

**Happy Learning! 🎓**