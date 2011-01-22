package de.softwareprozesse.mastermind.ai;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.PatternAnalysis;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Settings;

public class PatternGenerator {

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
			colors = intersection(pb.getUnusedColors(), possibleColors);
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
}
