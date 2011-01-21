package de.softwareprozesse.mastermind.ai;

import java.util.Arrays;
import java.util.Iterator;
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

	private List<Pattern> possiblePatterns;
	private Pattern patternUsedToGeneratePossiblePatterns;
	private final Mastermind mastermind;
	private final List<Color> possibleColors;
	
	public AI(Mastermind mastermind) {
		this.mastermind = mastermind;
		possibleColors = Arrays.asList(Color.values());
	}
	
	public Pattern pickPattern() {
		generatePossiblePatterns();
		return possiblePatterns.get(0);
	}
	
	private void generatePossiblePatterns() {
		updatePossibleColors();
		if (possiblePatterns == null) {
			possiblePatterns = buildAllPatterns(generatePatterns());
		} else {
			updatePossiblePatterns();
		}
	}
	
	private void updatePossibleColors() {
		// TODO Auto-generated method stub
		
	}
	
	private void removePatternsContainingImpossibleConstellations(
			Pattern lastguess, PatternAnalysis lastresponse) {
		if (lastresponse.isNoColorAtRightPosition()) {
			removePatternWithSamePositions(lastguess);
		}
		removePatternsContainingImpossibleColors(lastguess, lastresponse);
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
	
	private void removePatternsContainingImpossibleColors(Pattern guess, PatternAnalysis response) {
		if (response.gotAllColorsRight()) {
			removeExcessColors(guess);
		} else {
			removePatternsWithColors(guess.getColors());
		}
	}
	
	private void removeExcessColors(Pattern guess) {
		if (gotExcessColors())
			for (Color c : guess.getNotContainingColors()) {
				removePatternsWithColor(c);
				possibleColors.remove(c);
			}
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
	
	private boolean isPossiblePositionForColor(Color c, int pos) {
		for (Pattern p : mastermind.getGuesses())
			if (mastermind.getCorrespondingAnalysis(p).isNoColorAtRightPosition() && containsPatternColorOnPosition(p, c, pos))
				return false;
		return true;			
	}
	
	private boolean containsPatternColorOnPosition(Pattern guess, Color c, int pos) {
		return guess.getColor(pos).equals(c);
	}
}
