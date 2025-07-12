package com.rainy.ThreeBody;

import java.util.List;

public class ResourceAllocator {
    private long leftResources;
    
    public ResourceAllocator() {
        this.leftResources = (long) (SimulationConfig.INITIAL_RESOURCES * 
                                  SimulationConfig.BASE_RESOURCE_RATIO);
    }
    
    public void allocateResources(List<Civilization> civilizations) {
        List<Civilization> aliveCivs = civilizations.stream()
                .filter(c -> !c.isDestroyed())
                .toList();
        
        if (aliveCivs.isEmpty() || leftResources <= 0) return;
        
        // Pre-calculate total weight
        double totalWeight = aliveCivs.stream()
                .mapToDouble(civ -> Math.log1p(civ.getLevel()))
                .sum();
        
        // Calculate allocation ratio
        double allocationRatio = 0.01 * Math.sqrt(aliveCivs.size());
        
        for (Civilization civ : aliveCivs) {
            double weight = Math.log1p(civ.getLevel()) / totalWeight;
            long allocation = (long)(leftResources * allocationRatio * weight);
            allocation = Math.min(allocation, leftResources);
            
            if (allocation > 0) {
                civ.setResources(civ.getResources() + allocation);
                leftResources -= allocation;
            }
        }
    }
}