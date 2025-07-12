package com.rainy.ThreeBody;

import com.rainy.tool.Point;
import java.util.Arrays;
import java.util.List;



public class CivilizationFactory {
    private final UniverseMediator mediator;
    
    public CivilizationFactory(UniverseMediator mediator) {
        this.mediator = mediator;
    }
    
    public List<Civilization> createCivilizations(int count) {
        Civilization[] civs = new Civilization[count];
        
        int[] x = RandomGenerator.generateUniqueRandom(
            count, 0, SimulationConfig.UNIVERSE_SIZE_X);
        int[] y = RandomGenerator.generateUniqueRandom(
            count, 0, SimulationConfig.UNIVERSE_SIZE_Y);
        int[] z = RandomGenerator.generateUniqueRandom(
            count, 0, SimulationConfig.UNIVERSE_SIZE_Z);
        
        long totalResources = (long) (SimulationConfig.INITIAL_RESOURCES * 
                                   (1 - SimulationConfig.BASE_RESOURCE_RATIO));
        long[] resources = RandomGenerator.generateNonNegativeLong(count, totalResources);
        
        for (int i = 0; i < count; i++) {
            Point location = Point.valueOf(x[i], y[i], z[i]);
            civs[i] = new Civilization(i + 1, location, resources[i], mediator);
        }
        
        return Arrays.asList(civs);
    }
}