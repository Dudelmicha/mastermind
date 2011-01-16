package de.softwareprozesse.mastermind;

public class PatternAnalysis {

	private final int numberOfCorrectPositionedPins;
	private final int numberOfCorrectColoredPins;
	
	public PatternAnalysis(int numberOfCorrectPositionedPins, int numberOfCorrectColoredPins) {
		this.numberOfCorrectPositionedPins = numberOfCorrectPositionedPins;
		this.numberOfCorrectColoredPins = numberOfCorrectColoredPins;
	}

	public int getNumberOfCorrectPositionedPins() {
		return numberOfCorrectPositionedPins;
	}

	public int getNumberOfCorrectColoredPins() {
		return numberOfCorrectColoredPins;
	}
}
