package de.softwareprozesse.mastermind.ai;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.model.Color;
import de.softwareprozesse.mastermind.model.Pattern;
import de.softwareprozesse.mastermind.model.PatternAnalysis;
import de.softwareprozesse.mastermind.model.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Settings;

public class AI {

	private final List<Pattern> possiblePatterns;
	private final Mastermind mastermind;
	private final List<Color> possibleColors;
	
	public AI(Mastermind mastermind) {
		this.mastermind = mastermind;
		possiblePatterns = new LinkedList<Pattern>();
		possibleColors = new LinkedList<Color>(Arrays.asList(Color.values()));
	}
	
	public Pattern pickPattern() {
		if (mastermind.getNumberOfGuesses() == 0)
			return PatternBuilder.createRandomPattern();
		else
			generatePossiblePatterns();
		return possiblePatterns.remove(0);
	}
	
	private void generatePossiblePatterns() {
		updatePossibleColors();
		if (possiblePatterns.isEmpty()) {
			possiblePatterns.addAll(generatePatternsBasedOnFirstGuess());
		} else {
			updatePossiblePatterns();
		}
		if (!mastermind.getLastPatternAnalysis().gotAllColorsRight())
			removePatternContainingSameColorsAsGuess(mastermind.getLastGuessedPattern().getColors());
	}

	private void updatePossibleColors() {
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		Pattern lastguess = mastermind.getLastGuessedPattern();
		if (lastresponse.gotAllColorsRight()) {
			removeExcessColors(lastguess);
		}
	}
	
	private List<Pattern> generatePatternsBasedOnFirstGuess() {
		Pattern lastguess = mastermind.getLastGuessedPattern();
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		List<List<Integer>> combinations = Combinatorics.combination(lastresponse.getNumberOfCorrectPositionedPins(), Settings.NUMBER_OF_PEGS);
		List<Pattern> res = new LinkedList<Pattern>();
		for (List<Integer> combination : combinations) {
			PatternGenerator pg = new PatternGenerator(combination, lastguess, possibleColors);
			res.addAll(pg.generatePatterns());
		}
		return res;
	}
	
	private void updatePossiblePatterns() {
		removePatternsContainingImpossibleConstellations();
		
	}
	
	private void removeExcessColors(Pattern guess) {
		if (gotExcessColors())
			for (Color c : guess.getNotContainingColors()) {
				removePatternsWithColor(c);
				possibleColors.remove(c);
			}
	}
	
	private void removePatternsContainingImpossibleConstellations() {
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		Pattern lastguess = mastermind.getLastGuessedPattern();
		if (lastresponse.isNoColorAtRightPosition()) {
			removePatternWithSamePositions(lastguess);
		}
	}
	
	/**
	 * Removes all patterns that have any color at the same position as
	 * the given pattern
	 * @param guess
	 */
	private void removePatternWithSamePositions(Pattern guess) {
		for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++) 
			removeAllPatternsWithColorAtPosition(guess.getColor(i), i);
	}
	
	private boolean gotExcessColors() {
		return possibleColors.size() != Settings.NUMBER_OF_PEGS;
	}
	
	//TODO refactor next 3 methods
	private void removePatternsWithColor(Color c) {
		List<Pattern> toBeRemoved = new LinkedList<Pattern>();
		for (Pattern p : possiblePatterns)
			if (p.contains(c))
				toBeRemoved.add(p);
		removePatterns(toBeRemoved);
	}
	
	private void removePatternContainingSameColorsAsGuess(List<Color> colors) {
		List<Pattern> toBeRemoved = new LinkedList<Pattern>();
		for (Pattern p : possiblePatterns)
			if (p.consistsOf(colors))
				toBeRemoved.add(p);
		removePatterns(toBeRemoved);
	}
	
	private void removeAllPatternsWithColorAtPosition(Color c, int pos) {
		List<Pattern> toBeRemoved = new LinkedList<Pattern>();
		for (Pattern p : possiblePatterns)
			if (p.contains(c, pos))
				toBeRemoved.add(p);
		removePatterns(toBeRemoved);
	}
	
	private void removePatterns(List<Pattern> toBeRemoved) {
		for (Pattern p : toBeRemoved)
			possiblePatterns.remove(p);
	}
}
