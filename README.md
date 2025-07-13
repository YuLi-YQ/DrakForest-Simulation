# ThreeBody Universe Simulation (Version 3.0)

🌌 A cosmic civilization simulation based on the Dark Forest theory from Liu Cixin's "Three-Body Problem" trilogy

## 🚀 Major Updates in v3.0

### 1. Realistic Attack Mechanics
   • Light-speed delayed strikes (travel time = distance / LIGHT_SPEED)

   • Position tolerance checks (POSITION_TOLERANCE)

   • Attack command queue with priority scheduling

### 2. Civilization Mobility
   • Random movement with MOVE_PROBABILITY

   • Maximum displacement (MAX_MOVE_DISTANCE)

   • Spatial index updates during movement

### 3. Enhanced State System
   • New SealedState (unused in current logic)

   • State-driven behavior via CivilizationState interface

   • Destroyed civilizations stop development

### 4. Optimized Resource Allocation
   • Logarithmic weight distribution

   • Dynamic allocation ratio based on surviving civilizations

   • Resource consumption tied to population

### 5. Performance Improvements
   • Parallel stream processing for development

   • Optimized 3D spatial indexing (GRID_SIZE auto-calculation)

   • Thread-safe random number generation

## ⚙️ New Configuration Parameters
| Parameter | Default Value | Description |
|-----------|---------------|-------------|
| 'LIGHT_SPEED' | 30,000 Speed of | attack propagation (units/step) |
| 'MOVE_PROBABILITY' | 0.05 | Chance for civilization to move each step |
| 'MAX_MOVE_DISTANCE' | 5,000 | Maximum displacement per move |
| 'POSITION_TOLERANCE' | 50 | Allowed target position deviation |
| 'MIN_ATTACK_RESOURCES' | 100,000 | Minimum resources to launch attack |
| 'ATTACK_RESOURCE_COST' | 50,000 | Resource cost per attack |
| 'COUNTER_ATTACK_PROB' | 0.2 | Probability of counter-attacks |

## 🧠 Key Algorithms

### 1. Light-Speed Attack System
```java
// AttackCommand.java
public AttackCommand(int hunterId, int targetId, int currentStep, 
                     Point targetLocation, int travelTime, ...) {
    this.arrivalStep = currentStep + travelTime; // Light-speed delay
}

// AttackOrder.java
if (currentDistance <= POSITION_TOLERANCE) {
    // Calculate hit probability
    double successProbability = techAdvantage * distanceFactor;
    if (success) target.destroy();
}
```

### 2. Civilization Movement
```java
// UniverseSimulation.java
if (RandomService.nextDouble() < MOVE_PROBABILITY) {
    int dx = random.nextInt(MAX_MOVE_DISTANCE*2) - MAX_MOVE_DISTANCE;
    civ.setLocation(new Point(x+dx, y+dy, z+dz));
}
```

### 3. Spatial Indexing Optimizations
```java
// SpatialIndex.java
private static final int GRID_SIZE = Math.max(5_000, 
    Math.min(50_000, 
        UNIVERSE_SIZE / (int)Math.cbrt(CIVILIZATION_COUNT)
    )
);

public List<Civilization> getNearby(Point center, double range) {
    // Grid-based neighbor detection
    int gridRange = (int) Math.ceil(range / GRID_SIZE);
    // ... 3D grid traversal ...
}
```

### 4. Thread-Safe Randomness
```java
// RandomService.java
private static final ThreadLocal<Random> localRandom = 
    ThreadLocal.withInitial(() -> new Random(System.nanoTime()));
```

## 📊 Simulation Workflow

### 1. Initialization

• Generate civilizations with unique positions

• Allocate initial resources (BASE_RESOURCE_RATIO)

### 2. Per Time Step

• Random civilization exposure

• Parallel development execution

• Resource allocation

• Movement processing

• Light-speed attack resolution

### 3. Attack Lifecycle

```graph LR
  A[Launch Attack] --> B{In-flight?}
  B -->|Yes| C[Add to Queue]
  B -->|No| D[Immediate Resolution]
  C --> E{Arrival Step?}
  E -->|Reached| F[Check Position Tolerance]
  F -->|Valid| G[Resolve Attack]
  F -->|Moved| H[Attack Missed]
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/rainy/ThreeBody/
│   │   ├── AttackCommand.java       # Light-speed attack container
│   │   ├── AttackOrder.java         # Attack resolution system
│   │   ├── SealedState.java         # New civilization state
│   │   ├── UniverseMediator.java    # Enhanced event handling
│   │   ├── Civilization.java        # Core civilization model
│   │   ├── CivilizationState.java    # State interface
│   │   ├── DestroyedState.java      # Destroyed state
│   │   ├── ExposedState.java        # Exposed state
│   │   ├── HiddenState.java         # Hidden state
│   │   ├── LogService.java          # Logging service
│   │   ├── RandomGenerator.java     # Random utilities
│   │   ├── RandomService.java       # Central random service
│   │   ├── ResourceAllocator.java   # Resource distribution
│   │   ├── SimulationConfig.java    # Simulation parameters
│   │   ├── SpatialIndex.java        # 3D spatial index
│   │   ├── UniverseSimulation.java  # Main controller
│   │   └── CompactFormatter.java    # Log formatting
│   └── resources/                         
test/                                
.gitignore                           
README.md                             
LICENSE
```

## 🚦 Getting Started

Prerequisites

• Java 17 or higher

Installation & Running

## Clone the repository
```bash
git clone https://github.com/<your-username>/ThreeBody-Simulation.git
cd ThreeBody-Simulation
```

## Compile and run
```bash
javac -d bin src/main/java/com/rainy/ThreeBody/*.java src/main/java/com/rainy/tool/Point.java
java -cp bin com.rainy.ThreeBody.Test
```

## 📜 Simulation Output Example
```

---SIMULATION CONFIG---
Config:
--Universe Size: 1000000 x 1000000 x 1000000
--Civilizations: 1000
--Initial Resources: 1,000,000,000,000
--Time Steps: 10000
---SIMULATION START---

[Step 1] Alive: 1000, Exposed: 0
Exposed: Civi#42 [Lvl:1500, Pop:120,000, Res:500,000]
[Step 2] Alive: 1000, Exposed: 1
Delayed attack: Civi#123 -> Civi#42 (launched at step 1)
[Step 3] Alive: 999, Exposed: 1
...
[Step 542] Alive: 1, Exposed: 1

---WINNER---
Civi#257 [Tech:14250, Pop:3,250,000, Res:82,500,000] [Exposed] @ (42345, 67890, 12345)

---SIMULATION RESULTS---
Surviving civilizations: 1
Civi#257 [Lvl:14250, Pop:3,250,000, Res:82,500,000] Exposed
```

## 🤝 Contributing

### Contributions welcome! Suggested improvements:

1. **GUI Visualization**: Real-time visualization of civilization distribution
2. **Sealed State Implementation**: Integrate SealedState into game logic
3. **Counter-Attack System**: Implement COUNTER_ATTACK_PROB mechanics
4. **Position Prediction**: Add trajectory prediction for moving targets
5. **Resource-Based Attacks**: Implement ATTACK_RESOURCE_COST consumption



**Inspired by Liu Cixin's Remembrance of Earth's Past trilogy**

*"In the dark forest of the cosmos, every civilization is a hunter... moving silently through the void, striking across light-years, hiding when exposed." - Adapted from Liu Cixin*
