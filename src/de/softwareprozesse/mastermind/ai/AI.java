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
	/**
         *
         * @param mastermind the current gamesetting
         */
	public AI(Mastermind mastermind) {
		this.mastermind = mastermind;
		possiblePatterns = new LinkedList<Pattern>();
		possibleColors = new LinkedList<Color>(Arrays.asList(Color.values()));
	}
	/**
         *
         * @return if there is no knowledge about the pattern will be taken
         * if there is some knowledge about valid patterns, which could be correct,
         * on of the solutions would taken and removed on the list
         */
	public Pattern pickPattern() {
		if (mastermind.getNumberOfGuesses() == 0)
			return PatternBuilder.createRandomPattern();
		else
			generatePossiblePatterns();
		return possiblePatterns.remove(0);
	}
	/**
         * sets the list of valid and maybe correct solutions
         */
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
        /**
        * will update the possible colors on the unsetted positions
        */
	private void updatePossibleColors() {
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		Pattern lastguess = mastermind.getLastGuessedPattern();
		if (lastresponse.gotAllColorsRight()) {
			removeExcessColors(lastguess);
		}
	}
	/**
         *
         * @return a list of all patterns, which could be correct responding on the first guess
         */
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
	/**
         * removes all impossible constallations of the currrent patternknowledge
         */
	private void updatePossiblePatterns() {
		removePatternsContainingImpossibleConstellations();
		
	}
	/**
         *
         * @param guess removes colors which cant be correct
         */
	private void removeExcessColors(Pattern guess) {
		if (gotExcessColors())
			for (Color c : guess.getNotContainingColors()) {
				removePatternsWithColor(c);
				possibleColors.remove(c);
			}
	}
	/**
         * removes aöö imposible Constellations based on the lastresponse and lastguess
         */
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
	/**
         *
         * @return is the number of possiblecolors the same like number of holes?
         */
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
