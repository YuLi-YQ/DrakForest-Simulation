package com.rainy.ThreeBody;

public class HiddenState implements CivilizationState{

	@Override
	public void develop(Civilization civ) {
		civ.performDevelopment();
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
		return "Hidden";
	}

}
