package com.rainy.ThreeBody;

public interface CivilizationState {
	
    void develop(Civilization civ);
    
    boolean canBeAttacked();
    
    boolean isVisible();
    
    String getStatus();


}