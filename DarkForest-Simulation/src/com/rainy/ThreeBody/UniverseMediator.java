package com.rainy.ThreeBody;

import java.util.*;

import com.rainy.tool.Point;

public class UniverseMediator {
	private List<Civilization> civilizations;
    private SpatialIndex spatialIndex;
    private double dynamicExposureProb = SimulationConfig.EXPOSED_PROBABILITY;
    
    public UniverseMediator() {
        this.spatialIndex = new SpatialIndex();
        this.civilizations = new ArrayList<>();
    }
    
    public void updateCivilizationList(List<Civilization> civilizations) {
        this.civilizations = civilizations;
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
    
    public void processAttacks() {
        Set<Civilization> toDestroy = new HashSet<>();
        
        for (Civilization target : civilizations) {
            if (target.isDestroyed() || !target.isExposed()) continue;
            
            List<Civilization> potentialHunters = findPotentialTargets(target);
            for (Civilization hunter : potentialHunters) {
                if (hunter.isDestroyed() || hunter == target) continue;
                
                if (hunter.attack(target)) {
                    toDestroy.add(target);
                    LogService.logInfo(String.format("Attack: Civi#%d -> Civi#%d", 
                            target.getId(), hunter.getId()));
                }
            }
        }
        
        toDestroy.forEach(Civilization::destroy);
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
    /*
	public void updateCivilizationList(List<Civilization> civilizations) {
		this.civilizations.addAll(civilizations);
	}
	*/
}