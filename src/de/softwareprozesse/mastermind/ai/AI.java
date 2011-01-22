package de.softwareprozesse.mastermind.ai;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.PatternAnalysis;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Settings;

public class AI {

	private final List<Pattern> possiblePatterns;
	private final Mastermind mastermind;
	private final List<Color> possibleColors;
	
	public AI(Mastermind mastermind) {
		this.mastermind = mastermind;
		possiblePatterns = new LinkedList<Pattern>();
		possibleColors = Arrays.asList(Color.values());
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
			possiblePatterns.addAll(buildAllPatterns(generatePatternsBasedOnFirstGuess()));
		} else {
			updatePossiblePatterns();
		}
	}

	private void updatePossibleColors() {
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		Pattern lastguess = mastermind.getLastGuessedPattern();
		if (lastresponse.gotAllColorsRight()) {
			removeExcessColors(lastguess);
		} else {
			removePatternsWithColors(lastguess.getColors());
		}
	}
	
	private List<PatternBuilder> generatePatternsBasedOnFirstGuess() {
		Pattern lastguess = mastermind.getLastGuessedPattern();
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		List<List<Integer>> combinations = Combinatorics.combination(lastresponse.getNumberOfCorrectPositionedPins(), Settings.NUMBER_OF_PEGS);
		List<PatternBuilder> res = new LinkedList<PatternBuilder>();
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
		return possibleColors.size() == Settings.NUMBER_OF_PEGS;
	}
	
	//TODO refactor next 3 methods
	private void removePatternsWithColor(Color c) {
		for (Pattern p : possiblePatterns)
			if (p.contains(c))
				possiblePatterns.remove(p);
	}
	
	private void removePatternsWithColors(List<Color> colors) {
		for (Pattern p : possiblePatterns)
			if (p.contains(colors))
				possiblePatterns.remove(p);
	}
	
	private void removeAllPatternsWithColorAtPosition(Color c, int pos) {
		for (Pattern p : possiblePatterns)
			if (p.contains(c, pos))
				possiblePatterns.remove(p);
	}
	
		
	/**
	 * Transforms a list of PatternBuilder objects into a list of Pattern
	 * objects by calling the build method for each of them
	 * @param l list of PatternBuilder objects
	 * @return list of Pattern objects
	 */
	private List<Pattern> buildAllPatterns(List<PatternBuilder> l) {
		List<Pattern> res = new LinkedList<Pattern>();
		for (PatternBuilder pb : l)
			res.add(pb.build());
		return res;
	}
}
