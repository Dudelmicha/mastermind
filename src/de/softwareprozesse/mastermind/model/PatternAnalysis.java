package de.softwareprozesse.mastermind.model;

import de.softwareprozesse.mastermind.utils.Settings;

public class PatternAnalysis {

	private final int numberOfCorrectPositionedPins;
	private final int numberOfCorrectColoredPins;
	
	public PatternAnalysis(int numberOfCorrectPositionedPins, int numberOfCorrectColoredPins) {
		this.numberOfCorrectPositionedPins = numberOfCorrectPositionedPins;
		this.numberOfCorrectColoredPins = numberOfCorrectColoredPins;
	}

	public boolean isNoColorAtRightPosition() {
		return numberOfCorrectPositionedPins == 0;
	}
	
	public boolean gotAllColorsRight() {
		return getNumberOfCorrectColoredPins() + getNumberOfCorrectPositionedPins() == Settings.NUMBER_OF_PEGS;
	}
	
	public int getNumberOfCorrectPositionedPins() {
		return numberOfCorrectPositionedPins;
	}

	public int getNumberOfCorrectColoredPins() {
		return numberOfCorrectColoredPins;
	}
}
