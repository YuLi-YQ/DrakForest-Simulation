package com.rainy.ThreeBody;

public final class SimulationConfig {
    // Universe dimensions
    public static final int UNIVERSE_SIZE_X = 100_000;
    public static final int UNIVERSE_SIZE_Y = 100_000;
    public static final int UNIVERSE_SIZE_Z = 100_000;
    
    // Civilization parameters
    public static final int CIVILIZATION_COUNT = 1000;
    public static final long INITIAL_RESOURCES = 1_000_000_000_000L;
    public static final double BASE_RESOURCE_RATIO = 0.9;
    public static final double EXPOSED_PROBABILITY = 0.01;
    public static final int TIME_STEPS = 10_000;
    
    // Technology parameters
    public static final double STRIKE_THRESHOLD = 0.6;
    public static final double TECH_EXPOSED_PROB = 0.01;
    public static final double BASE_GROWTH_RATE = 0.02;
    public static final double RESOURCE_CONSUMPTION_RATE = 0.0001;
    public static final double TECH_BOOST_MULTIPLIER = 1.3;
    public static final double TECH_DEVELOP_MULTIPLIER = 1.05;
    public static final double POPULATION_RESOURCE_RATIO = 0.0001;
    public static final double MAX_LEVEL = 1_000_000;
    public static final double LEVEL_DECAY_FACTOR = 0.99;
    public static final double POPULATION_RESOURCE_RATIO_INIT = 0.0001;
    
    private SimulationConfig() {} // Prevent instantiation
}