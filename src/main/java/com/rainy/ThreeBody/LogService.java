package com.rainy.ThreeBody;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;



/**
 * A tool log sever for simulation.
 * @author Rainy
 * @date 2025/7/12
 * @version 1.0
 * */
public class LogService {
    private static Logger logger;
    
    static {
        try {
            logger = Logger.getLogger("UniverseSimulation");
            FileHandler fileHandler = new FileHandler("simulation.log", true);
            fileHandler.setFormatter(new CompactFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console logging
            
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CompactFormatter());
            logger.addHandler(consoleHandler);
            
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize logger", e);
        }
    }
    
    public static void logStart() {
    	logInfo("---SIMULATION CONFIG---");
    	
    	logInfo("Config:");
        logInfo(String.format("--Universe Size: %d x %d x %d", 
            SimulationConfig.UNIVERSE_SIZE_X,
            SimulationConfig.UNIVERSE_SIZE_Y,
            SimulationConfig.UNIVERSE_SIZE_Z));
        
        logInfo(String.format("--Civilizations: %d", SimulationConfig.CIVILIZATION_COUNT));
        logInfo(String.format("--Initial Resources: %,d", SimulationConfig.INITIAL_RESOURCES));
        logInfo(String.format("--Time Steps: %d", SimulationConfig.TIME_STEPS));
        
        logInfo("Tech Params:");
        logInfo(String.format("--Light Speed: %d", SimulationConfig.LIGHT_SPEED));
        logInfo(String.format("--Base Growth Rate: %.4f", SimulationConfig.BASE_GROWTH_RATE));
        logInfo(String.format("--Resource Consumption Rate: %.6f", SimulationConfig.RESOURCE_CONSUMPTION_RATE));
        logInfo(String.format("--Exposure Probability: %.4f", SimulationConfig.EXPOSED_PROBABILITY));
        logInfo(String.format("--Tech Exposed Prob: %.4f", SimulationConfig.TECH_EXPOSED_PROB));
        logInfo(String.format("--Strike Threshold: %.1f", SimulationConfig.STRIKE_THRESHOLD));
        
        logInfo("Allocation Params:");
        logInfo(String.format("--Base Resource Ratio: %.1f", SimulationConfig.BASE_RESOURCE_RATIO));
        
        logInfo("---SIMULATION START---");
    }
    
    public static void logInfo(String message) {
        logger.info(message);
    }
    
    public static void logEvent(String event, Civilization civ) {
        logInfo(event + ": " + civ.toCompactString());
    }
    
    public static void logStep(int step, long alive, long exposed) {
        logInfo(String.format("[Step %d] Alive: %d, Exposed: %d", step, alive, exposed));
    }
    
    public static void logWinner(Civilization winner) {
        logInfo("---WINNER---");
        logInfo(winner.toString());
    }
    
    public static void logResults(List<Civilization> civilizations) {
        long alive = civilizations.stream().filter(c -> !c.isDestroyed()).count();
        logInfo("---SIMULATION RESULTS---");
        logInfo("Surviving civilizations: " + alive);
        
        civilizations.stream()
            .filter(c -> !c.isDestroyed())
            .forEach(c -> logInfo(c.toCompactString()));
    }
}