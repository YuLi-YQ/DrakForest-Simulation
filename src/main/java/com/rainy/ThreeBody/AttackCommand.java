package com.rainy.ThreeBody;

import com.rainy.tool.Point;

public final class AttackCommand {
    private final int hunterId;
    private final int targetId;
    private final int startStep;
    private final int travelTime;
    private final Point originalTargetPoint;
    private final int hunterLevel;
    private final long originalSquareDistance;
    
    
    public AttackCommand(int hunterId, int targetId, int currentStep, 
                         Point targetLocation, int travelTime, int hunterLevel, long squareDistance) {
        this.hunterId = hunterId;
        this.targetId = targetId;
        this.startStep = currentStep;
        this.travelTime = travelTime;
        this.originalTargetPoint = targetLocation;
        this.hunterLevel = hunterLevel;
		this.originalSquareDistance = squareDistance;
    }
    
    public boolean isArrive(int currentStep) {
        return currentStep >= startStep + travelTime;
    }
    
    public int getHunterId() {
        return hunterId;
    }
    
    public int getTargetId() {
        return targetId;
    }
    
    public Point getOriginalTargetPoint() {
        return originalTargetPoint;
    }
    
    public int getHunterLevel() {
        return hunterLevel;
    }
    
    @Override
    public String toString() {
        return String.format("AttackCmd[%d->%d @ step %d]", hunterId, targetId, startStep);
    }

	public int getStartStep() {
		return startStep;
	}

	public int getArrivalStep() {
		return startStep + travelTime;
	}
	
	public long getOriginalSquareDistance() {
        return originalSquareDistance;
    }
	
}