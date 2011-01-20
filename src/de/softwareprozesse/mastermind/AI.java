package de.softwareprozesse.mastermind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Settings;

public class AI {

	private List<Pattern> possiblePatterns;
	private Pattern patternUsedToGeneratePossiblePatterns;
	private final Map<Color, List<Integer>> possiblePositions;
	private final Mastermind mastermind;
	private final List<Color> possibleColors;
	
	public AI(Mastermind mastermind) {
		this.mastermind = mastermind;
		possiblePositions = new HashMap<Color, List<Integer>>();
		possibleColors = Arrays.asList(Color.values());
		for (Color c : Color.values()) {
			List<Integer> l = new LinkedList<Integer>();
			for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++)
				l.add(i);
			possiblePositions.put(c, l);
		}
	}
	
	public Pattern pickPattern() {
		return possiblePatterns.get(0);
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
		if (gotAllColorsRight()) {
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

	private boolean gotAllColorsRight() {
		PatternAnalysis lastresponse = mastermind.getResponse(mastermind.getNumberOfGuesses());
		return lastresponse.getNumberOfCorrectColoredPins() + lastresponse.getNumberOfCorrectPositionedPins() == Settings.NUMBER_OF_PEGS;
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
	 * Generates the initial list containing all possible color patterns
	 * Initial means before the first guess, so that no further information
	 * is available.
	 * @return
	 */
	private List<Pattern> generateAllPossiblePatterns() {
		List<Pattern> possiblePatterns = createAllColorPermutations();
		return possiblePatterns;
	}

	
	private List<Pattern> createAllColorPermutations() {
		List<Color> allcolors = new LinkedList<Color>(Arrays.asList(Color.values()));
		return buildAllPatterns(permutate(new LinkedList<PatternBuilder>(), allcolors, 0));
		
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
	
	private boolean containsAnyPatternColorOnPosition(Pattern[] guesses, Color c, int pos) {
		boolean res = false;
		for (Pattern p : guesses)
			if (containsPatternColorOnPosition(p, c, pos))
				res = true;
		return res;			
	}
	
	private List<Pattern> getPatternsWithZeroRightPositions(Pattern[] guesses, PatternAnalysis[] responses) {
		List<Pattern> res = new LinkedList<Pattern>();
		for (int i = 0; i < responses.length; i++) 
			if (responses[i].getNumberOfCorrectPositionedPins() == 0)
				res.add(guesses[i]);
		return res;
	}
	
	private boolean containsPatternColorOnPosition(Pattern guess, Color c, int pos) {
		return guess.getColor(pos).equals(c);
	}
	
	// TODO don't add impossible patterns
	private List<PatternBuilder> generatePatterns(Pattern p, PatternAnalysis pa) {
		List<List<Integer>> combinations = Combinatorics.combination(pa.getNumberOfCorrectPositionedPins(), Settings.NUMBER_OF_PEGS);
		patternUsedToGeneratePossiblePatterns = p;
		List<PatternBuilder> res = new LinkedList<PatternBuilder>();
		for (List<Integer> intlist : combinations) {
			PatternBuilder pb = new PatternBuilder();
			for (int i : intlist)
				pb.setColor(p.getColor(i), i);
			res.add(pb);
		}
		return res;
	}
	
	private List<PatternBuilder> permutateUnsetPositions(List<PatternBuilder> builders, List<Color> colors) {
		List<Integer> unsetPositions;
		List<List<Integer>> permutations;
		for (PatternBuilder pb : builders) {
			unsetPositions = pb.getUnsetPositions();
			permutations = Combinatorics.permutation(unsetPositions.size(), colors.size());
			for (List<Integer> permutation : permutations) {
				Iterator<Integer> it = permutation.iterator();
				for (int unsetPosition : unsetPositions) {
					pb.setColor(colors.get(it.next()), unsetPosition);
				}
			}
		}
		return builders;
	}
	
	private List<PatternBuilder> permutate(List<PatternBuilder> l, List<Color> colors, int i) {
		if (i == Settings.NUMBER_OF_PEGS - 1) {
			for (Color c : colors) {
				PatternBuilder pb = new PatternBuilder();
				pb.setColor(c, i);
				l.add(pb);
			}
		} else {
			for (Color c : colors) {
				l.addAll(setAsElement(c, i, permutate(new LinkedList<PatternBuilder>(), copyListAndRemoveElement(colors, c), i + 1)));
			}
		}
		return l;
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

	private void updatePossiblePatterns() {
		Pattern lastguessed = mastermind.getLastGuessedPattern();
		PatternAnalysis lastresponse = mastermind.getLastPatternAnalysis();
		if (possiblePatterns == null) 
			generatePatterns(lastguessed, lastresponse);
		else
			removePatternsContainingImpossibleConstellations(lastguessed, lastresponse);
	}
	
	private void removePatternsContainingImpossibleConstellations(
			Pattern lastguess, PatternAnalysis lastresponse) {
		if (lastresponse.isNoColorAtRightPosition()) {
			removePatternWithSamePositions(lastguess);
		}
		removePatternsContainingImpossibleColors(lastguess, lastresponse);
	}
}
