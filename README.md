# ThreeBody Universe Simulation

A cosmic civilization simulation based on the Dark Forest theory from Liu Cixin's "Three-Body Problem" trilogy. This Java implementation models the evolution of civilizations in a virtual universe, where survival depends on strategic development, resource management, and preemptive strikes against potential threats.

## Project Overview

This simulation implements the core concepts from the Three-Body trilogy:
- 🌌 **Civilization Development**: Tech advancement, population growth, resource management
- 🌑 **Dark Forest Mechanics**: Tech exposure risks, long-range strikes, mutual destruction
- 🔭 **Cosmic Sociology**: Survival as the primary need, resource competition, chain of suspicion

Civilizations evolve through technological development, risk exposure to advanced neighbors, and strategic attacks, with only the most strategically advanced surviving. The simulation runs for configurable time steps, with comprehensive logging of all significant events.

## Features

### Core Simulation Mechanics
- **Civilization State Management**: Hidden, Exposed, and Destroyed states
- **Technology Development**: Regular advancement with chance for "tech explosions"
- **Resource System**: Allocation, consumption, and depletion mechanics
- **Attack System**: Distance-based strike probability calculations
- **Dynamic Exposure**: Adaptive exposure probability based on surviving civilizations

### Performance Optimizations
- 🚀 **3D Spatial Indexing**: Grid-based system for efficient neighbor detection
- ⚡ **Square Distance Calculations**: Optimized collision detection
- 📊 **Compact Logging**: Human-readable output with essential information
- 🧪 **Parameterized Configuration**: Easy adjustment of simulation parameters

### Architecture
- **Mediator Pattern**: Centralized civilization interaction management
- **State Pattern**: Clean implementation of civilization states
- **Factory Pattern**: Civilization creation and initialization
- **Service Locator**: Centralized random and logging services

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven or Gradle (optional)

### Installation & Running
```bash
# Clone the repository
git clone https://github.com/<your-username>/ThreeBody-Simulation.git
cd ThreeBody-Simulation

# Direct execution:
java -cp target/classes com.rainy.ThreeBody.Test
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/rainy/ThreeBody/
│   │   │   ├── Civilization.java          # Core civilization model
│   │   │   ├── CivilizationFactory.java   # Civilization creation
│   │   │   ├── CivilizationState.java     # State interface
│   │   │   ├── DestroyedState.java        # Destroyed state
│   │   │   ├── ExposedState.java          # Exposed state
│   │   │   ├── HiddenState.java           # Hidden state
│   │   │   ├── LoggerService.java         # Logging service
│   │   │   ├── RandomGenerator.java       # Random utilities
│   │   │   ├── RandomService.java         # Central random service
│   │   │   ├── ResourceAllocator.java     # Resource distribution
│   │   │   ├── SimulationConfig.java      # Simulation parameters
│   │   │   ├── SpatialIndex.java          # 3D spatial index
│   │   │   ├── UniverseMediator.java      # Civilization interaction
│   │   │   ├── UniverseSimulation.java    # Main controller
│   │   │   └── CompactFormatter.java      # Log formatting
│   │   └── com/rainy/tool/
│   │       └── Point.java                 # 3D point implementation
│   └── resources/                         # Configuration files
test/                                      # Unit tests
.gitignore                                 # Version control ignore
README.md                                  # Project documentation
```

## Configuration Parameters

Key parameters in `SimulationConfig.java`:

| Parameter | Default Value | Description |
|-----------|---------------|-------------|
| `UNIVERSE_SIZE_X` | 100,000 | Universe width |
| `UNIVERSE_SIZE_Y` | 100,000 | Universe height |
| `UNIVERSE_SIZE_Z` | 100,000 | Universe depth |
| `CIVILIZATION_COUNT` | 1,000 | Initial civilizations |
| `INITIAL_RESOURCES` | 1,000,000,000,000 | Total starting resources |
| `TIME_STEPS` | 10,000 | Maximum simulation steps |
| `TECH_EXPOSED_PROB` | 0.01 | Base tech exposure probability |
| `BASE_GROWTH_RATE` | 0.02 | Population growth rate |
| `STRIKE_THRESHOLD` | 0.6 | Attack success threshold |
| `RESOURCE_CONSUMPTION_RATE` | 0.0001 | Resource consumption per population |

## Simulation Output

Example log output:
```
===== SIMULATION START =====
Config:
Universe Size: 100000 x 100000 x 100000
Civilizations: 1000
Initial Resources: 1,000,000,000,000
Time Steps: 10000
===== SIMULATION BEGIN =====

[Step 1] Alive: 1000, Exposed: 0
Exposed: Civi#42 [Lvl:1500, Pop:120,000, Res:500,000]
[Step 2] Alive: 1000, Exposed: 1
Attack: Civi#123 -> Civi#42
Destroyed: Civi#42
Resource depleted: Civi#789
...
[Step 542] Alive: 1, Exposed: 1

===== WINNER =====
Civi#257 [Tech:14250, Pop:3,250,000, Res:82,500,000] [Exposed] @ (42345, 67890, 12345)

=== SIMULATION RESULTS ===
Surviving civilizations: 1
Civi#257 [Lvl:14250, Pop:3,250,000, Res:82,500,000] Exposed
```

## Key Algorithms

### Civilization Development
```java
void performDevelopment() {
    // Tech explosion chance (logarithmic with level)
    double exposureProb = TECH_EXPOSED_PROB * Math.log1p(level);
    if (RandomService.nextDouble() < exposureProb) {
        level += 1000 + RandomService.nextInt(5000);
        resources -= population * 0.1;
    }
    
    // Regular development
    level += 50 + RandomService.nextInt(100);
    
    // Population growth with carrying capacity
    double carryingCapacity = resources / (population + 1) * 1000;
    double growthRate = BASE_GROWTH_RATE * 
                      (1 - population / (carryingCapacity + 1));
    population += (int)(population * growthRate);
    
    // Resource consumption
    long consumption = (long)(population * RESOURCE_CONSUMPTION_RATE);
    resources -= consumption;
}
```

### Attack Calculation
```java
public boolean attack(Civilization target) {
    // Tech advantage calculation (normalized)
    double techAdvantage = (this.level - target.level) / 
                          (double) (this.level + target.level + 1);
    
    // Distance attenuation factor
    double distance = this.location.distance(target.location);
    double distanceFactor = 1 / (1 + distance * 0.0001);
    
    // Success probability calculation
    double successProbability = Math.max(0, techAdvantage * 2) * distanceFactor;
    
    return RandomService.nextDouble() < successProbability;
}
```

### Spatial Indexing
```java
public List<Civilization> getNearbyCivilizations(Point center, double range) {
    List<Civilization> result = new ArrayList<>();
    long squaredRange = (long)(range * range);
    
    // Calculate grid range for optimization
    int gridRange = (int) Math.ceil(range / GRID_SIZE);
    int centerX = (int) (center.getX() / GRID_SIZE);
    // ... calculate centerY and centerZ ...
    
    // Search neighboring grid cells (3D)
    for (int x = centerX - gridRange; x <= centerX + gridRange; x++) {
        for (int y = centerY - gridRange; y <= centerY + gridRange; y++) {
            for (int z = centerZ - gridRange; z <= centerZ + gridRange; z++) {
                String key = x + ":" + y + ":" + z;
                List<Civilization> cell = grid.get(key);
                if (cell != null) {
                    for (Civilization civ : cell) {
                        // Use squared distance for performance
                        if (civ.getLocation().squaredDistance(center) <= squaredRange) {
                            result.add(civ);
                        }
                    }
                }
            }
        }
    }
    return result;
}
```

## Contributing

Contributions are welcome! Please open issues or pull requests for:

- Performance optimizations
- Additional simulation parameters
- Visualization modules
- Statistical analysis tools
- Alternative interaction models

### Suggested Improvements(In future,if I have enough time :O )
1. **GUI Visualization**: Implement real-time visualization of civilization distribution
2. **External Configuration**: Add parameter configuration via YAML/JSON files
3. **Parallel Processing**: Develop multi-threaded simulation engine
4. **Statistical Package**: Create analysis tools for simulation results
5. **Advanced AI**: Implement machine learning for civilization decision-making

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Inspired by Liu Cixin's Remembrance of Earth's Past trilogy**  
*"In the cosmos, all civilizations are hunters in a dark forest. Like hunters in the dark forest, civilizations must remain hidden and strike first to survive..."*
