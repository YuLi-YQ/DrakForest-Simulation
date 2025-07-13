package com.rainy.ThreeBody;

public class SealedState implements CivilizationState {

	@Override
	public void develop(Civilization civ) {
		
	}

	@Override
	public boolean canBeAttacked() {
		return true;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public String getStatus() {
		return "Sealed";
	}

}
