package com.rainy.ThreeBody;

import java.util.*;

import com.rainy.tool.Point;

public class UniverseMediator {
	private List<Civilization> civilizations;
    private SpatialIndex spatialIndex;
    private double dynamicExposureProb = SimulationConfig.EXPOSED_PROBABILITY;
    
    private final Map<Integer, Civilization> civilizationMap = new HashMap<>();
    private final PriorityQueue<AttackCommand> attackCommands = 
    	    new PriorityQueue<>(Comparator.comparingInt(AttackCommand::getArrivalStep));
    
    public UniverseMediator() {
        this.spatialIndex = new SpatialIndex();
        this.civilizations = new ArrayList<>();
    }
    
    public void updateCivilizationList(List<Civilization> civilizations) {
        this.civilizations = civilizations;
        civilizationMap.clear();
        civilizations.forEach(civ -> civilizationMap.put(civ.getId(), civ));
        
        
        spatialIndex = new SpatialIndex();
        
        for (Civilization civ : civilizations) {
            spatialIndex.addCivilization(civ);
        }
    }
    
    public void onCivilizationExposed(Civilization civ) {
        LogService.logEvent("Civilization exposed", civ);
    }
    
    public void onCivilizationDestroyed(Civilization civ) {
        LogService.logEvent("Civilization destroyed", civ);
    }
    
    public void onCivilizationMoved(Civilization civ, Point oldLocation) {
        spatialIndex.updateCivilization(civ, oldLocation);
    }
    
    public List<Civilization> findPotentialTargets(Civilization hunter) {
        return spatialIndex.getNearbyCivilizations(
            hunter.getLocation(), 
            hunter.getStrikeRange()
        );
    }
    
    public void exposeRandomCivilizations() {
        for (Civilization civ : civilizations) {
            if (!civ.isDestroyed() && !civ.isExposed()) {
                if (RandomService.nextDouble() < dynamicExposureProb) {
                    civ.expose();
                }
            }
        }
    }
    
    public void updateExposureProbability() {
        long alive = civilizations.stream().filter(c -> !c.isDestroyed()).count();
        dynamicExposureProb = SimulationConfig.EXPOSED_PROBABILITY * 
                              (1 + 5 * (1 - alive / (double) SimulationConfig.CIVILIZATION_COUNT));
    }
    
    public void processAttacks(int currentStep, AttackOrder attackOrder) {
    	
    	while (!attackCommands.isEmpty() && attackCommands.peek().getArrivalStep() <= currentStep) {
    	    AttackCommand cmd = attackCommands.poll();
    	    attackOrder.resolve(cmd, currentStep);
    	}
        
        for (Civilization target : getAliveCivilizations()) {
            if (target.isDestroyed() || !target.isExposed()) continue;
            
            List<Civilization> potentialHunters = findPotentialTargets(target);
            for (Civilization hunter : potentialHunters) {
                if (hunter.isDestroyed() || hunter == target) continue;
                
                int travelTime = (int) (hunter.distance(target) / SimulationConfig.LIGHT_SPEED);
                AttackCommand cmd = new AttackCommand(
                    hunter.getId(),
                    target.getId(),
                    currentStep,
                    target.getLocation(),
                    travelTime,
                    hunter.getLevel(),
                    hunter.getLocation().squareDistance(target.getLocation())
                );
                
                attackCommands.add(cmd);
                //LogService.logInfo(String.format("Attack launched: Civi#%d -> Civi#%d (ETA step %d)", 
                  //      hunter.getId(), target.getId(), currentStep + travelTime));
            }
        }
    }
    
    public long countAliveCivilizations() {
        return civilizations.stream().filter(c -> !c.isDestroyed()).count();
    }
    
    public long countExposedCivilizations() {
        return civilizations.stream().filter(Civilization::isExposed).count();
    }
    
    public List<Civilization> getAliveCivilizations() {
        return civilizations.stream().filter(c -> !c.isDestroyed()).toList();
    }
    
    public Map<Integer, Civilization> getCivilizationMap() {
        return Collections.unmodifiableMap(civilizationMap);
    }
    
}