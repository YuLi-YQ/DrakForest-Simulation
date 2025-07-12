package com.rainy.ThreeBody;

public class ExposedState implements CivilizationState {

	@Override
	public void develop(Civilization civ) {
		civ.performDevelopment();
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
		return "Exposed";
	}

}
