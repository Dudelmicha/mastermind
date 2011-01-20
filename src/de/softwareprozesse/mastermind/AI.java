package de.softwareprozesse.mastermind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
		if (possiblePatterns == null) {
			possiblePatterns = buildAllPatterns(generatePatterns());
		} else {
			updatePossiblePatterns();
		}
	}
	
	// TODO don't add impossible patterns
	private List<PatternBuilder> generatePatterns() {
		Pattern lastguess = mastermind.getLastGuessedPattern();
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		patternUsedToGeneratePossiblePatterns = lastguess;
		List<List<Integer>> combinations = Combinatorics.combination(lastresponse.getNumberOfCorrectPositionedPins(), Settings.NUMBER_OF_PEGS);
		List<PatternBuilder> res = new LinkedList<PatternBuilder>();
		for (List<Integer> intlist : combinations) {
			PatternBuilder pb = new PatternBuilder();
			for (int i : intlist)
				pb.setColor(lastguess.getColor(i), i);
			res.add(pb);
		}
		return permutateUnsetPositions(res);
	}
	
	private void updatePossiblePatterns() {
		removePatternsContainingImpossibleConstellations(mastermind.getLastGuessedPattern(), mastermind.getLastPatternAnalysis());
	}
	
	private List<PatternBuilder> permutateUnsetPositions(List<PatternBuilder> builders) {
		List<Integer> unsetPositions;
		List<List<Integer>> permutations;
		List<Color> colors;
		for (PatternBuilder pb : builders) {
			unsetPositions = pb.getUnsetPositions();
			colors = pb.getUnusedColors();
			permutations = Combinatorics.permutation(unsetPositions.size(), colors.size());
			for (List<Integer> permutation : permutations) {
				Iterator<Integer> it = permutation.iterator();
				for (int unsetPosition : unsetPositions) {
					Color nextColor = colors.get(it.next());
					pb.setColor(nextColor, unsetPosition);
				}
			}
			assert pb.getUnsetPositions().isEmpty();
			if (!isPatternPossible(pb))
				builders.remove(pb);
		}
		return builders;
	}
	
	private boolean isPatternPossible(PatternBuilder pb) {
		for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++)
			if (!isPossiblePositionForColor(pb.getColor(i), i))
				return false;
		return true;
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
	
	private List<Color> copyListAndRemoveElement(List<Color> l, Color c) {
		List<Color> newlist = new LinkedList<Color>(l);
		newlist.remove(c);
		return newlist;
	}
	
	private List<PatternBuilder> setAsElement(Color c, int pos, List<PatternBuilder> l) {
		for (PatternBuilder pb : l)
			pb.setColor(c, pos);
		return l;
	}
}
