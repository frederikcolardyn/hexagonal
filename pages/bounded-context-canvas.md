# Bounded Context Canvas: Weather Service

## Purpose
Provides real-time weather information for geographic locations by integrating with external weather data providers.

## Strategic Classification
### Domain
**Supporting Domain** - Provides weather data access capabilities that support core business functions but is not the primary business differentiator. The service acts as an educational example of hexagonal architecture patterns.

### Business Model
- **Risk Reduction**: Demonstrates proper architectural patterns and clean separation of concerns, reducing technical debt and maintenance costs in enterprise applications
- **Knowledge Transfer**: Serves as a learning tool for teams adopting hexagonal architecture and domain-driven design principles

### Evolution
- **Custom Built**: Purpose-built educational service demonstrating hexagonal architecture patterns with clean domain boundaries

## Domain Roles
- **Gateway**: Acts as a translation layer between external weather APIs and consuming applications, transforming external weather data formats into domain-specific models without complex business rules

## Ubiquitous Language
### Context-specific domain terminology
- **Weather**: Current atmospheric conditions at a specific location, including temperature, weather condition, and observation timestamp
- **Location**: Geographic coordinate pair (latitude/longitude) with optional city name identifier for human-readable reference
- **Weather Condition**: Standardized categorization of atmospheric state (clear, cloudy, rain, snow, etc.) mapped from WMO weather codes
- **Weather Provider**: External system or service that supplies raw weather observation data
- **Temperature**: Atmospheric temperature measured in Celsius degrees

### Business Decisions
1. Weather data is always requested in real-time and not cached, ensuring freshness but accepting potential latency
2. Geographic locations must be valid coordinates (latitude: -90 to 90, longitude: -180 to 180)
3. Weather conditions are standardized using WMO (World Meteorological Organization) codes for consistency
4. Missing or unavailable weather data results in explicit failures rather than returning stale or default values

## Inbound Communication

### Messages (Query/Command/Event)
#### GetWeather
**Query** - Retrieves current weather information for a specified geographic location with latitude, longitude, and optional city name

### Collaborators
- **Client Applications**: Any HTTP client consuming the REST API for weather information display or decision-making
- **Educational Users**: Developers and architects studying hexagonal architecture implementation patterns

## Outbound Communication
### Messages
- **Query: Fetch Current Weather**: Retrieves real-time weather data including temperature and condition code from external provider
- **Query: Resolve WMO Code**: Translates numeric WMO weather codes into human-readable weather condition categories

### Collaborators
- **Open-Meteo API**: Free public weather API providing current atmospheric observations accessed via REST endpoints at api.open-meteo.com

## Assumptions
- Open-Meteo API maintains 99% uptime and responds within 2 seconds for weather data requests
- Weather data accuracy from Open-Meteo is sufficient for educational and non-critical use cases
- Geographic coordinates provided by clients are validated and within Earth's valid range
- Network connectivity to external weather API is stable with minimal latency (<500ms)

## Verification Metrics
- API response time: 95th percentile under 1 second for weather retrieval operations
- Weather data freshness: All returned weather observations are less than 15 minutes old
- Service availability: 99.5% uptime for the REST API endpoints during business hours
- Constraint: Service provides current weather only, no historical data or forecasting capabilities

## Open Questions
- Should the service support caching of weather data to reduce external API calls and improve response time?
- How should the service handle weather data for multiple locations simultaneously (batch requests)?
- Should weather alerts or severe weather warnings be included in the domain model?
- What retry and fallback strategies should be implemented for Open-Meteo API failures?