package com.rainy.ThreeBody;

import java.util.Map;

public class AttackOrder {
	
    
 
	
	
	private final Map<Integer, Civilization> civilizationMap;
    
    public AttackOrder(Map<Integer, Civilization> civilizationMap) {
        this.civilizationMap = civilizationMap;
    }
    
    public void resolve(AttackCommand command, int currentStep) {
        Civilization target = civilizationMap.get(command.getTargetId());
        
        if (target == null || target.isDestroyed()) {
            //LogService.logInfo(String.format("Attack aborted: Target Civi#%d already destroyed (launched at step %d)", 
                   // command.getTargetId(), command.getStartStep()));
            return;
        }
        
        double currentDistance = command.getOriginalTargetPoint().distance(target.getLocation());
        double positionTolerance = SimulationConfig.POSITION_TOLERANCE;
        
        if (currentDistance <= positionTolerance) {
            int targetLevel = target.getLevel();
            double techAdvantage = (command.getHunterLevel() - targetLevel) / 
                                  (double) (command.getHunterLevel() + targetLevel + 1);
            
            double distance = command.getOriginalTargetPoint().distance(target.getLocation());
            double distanceFactor = 1 / (1 + distance * 0.0001);
            
            double successProbability = Math.max(0, techAdvantage * 2) * distanceFactor;
            
            if (RandomService.nextDouble() < successProbability) {
                target.destroy();
                LogService.logInfo(String.format("Delayed attack: Civi#%d -> Civi#%d (launched at step %d)", 
                        command.getHunterId(), command.getTargetId(), command.getStartStep()));
            } else {
                //LogService.logInfo(String.format("Attack missed: Attack from Civi#%d to Civi#%d failed", 
                  //      command.getHunterId(), command.getTargetId()));
            }
        } else {
            //LogService.logInfo(String.format("Attack missed: Civi#%d moved (%.2f units) since step %d", 
              //      command.getTargetId(), currentDistance, command.getStartStep()));
        }
    }
	
}
