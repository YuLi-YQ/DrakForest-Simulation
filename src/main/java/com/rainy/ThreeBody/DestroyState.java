package com.rainy.ThreeBody;

public class DestroyState implements CivilizationState {

	@Override
	public void develop(Civilization civ) {
		//Do nothing.
	}

	@Override
	public boolean canBeAttacked() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public String getStatus() {
		return "Destroyed";
	}

}
