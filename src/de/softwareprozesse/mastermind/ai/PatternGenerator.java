package de.softwareprozesse.mastermind.ai;

import java.util.Iterator;
import java.util.List;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Functions;

public class PatternGenerator {

	private Pattern patternUsedToGeneratePossiblePatterns;
	private List<Integer> indizesAsumedToHaveRightColor;
	private List<PatternBuilder> result;
	private List<Color> possibleColors;
	
	public PatternGenerator(List<Integer> combination, Pattern lastguess, List<Color> possibleColors) {
		patternUsedToGeneratePossiblePatterns = lastguess;
		indizesAsumedToHaveRightColor = combination;
		this.possibleColors = possibleColors;
		setColorsAmusedToBeRight();
	}

	private void setColorsAmusedToBeRight() {
		PatternBuilder pb = new PatternBuilder();
		for (int i : indizesAsumedToHaveRightColor)
			pb.setColor(patternUsedToGeneratePossiblePatterns.getColor(i), i);
		result.add(pb);
	}
	
	public List<PatternBuilder> generatePatterns() {
		return permutateUnsetPositions(result);
	}
	
	private List<PatternBuilder> permutateUnsetPositions(List<PatternBuilder> builders) {
		List<Integer> unsetPositions;
		List<List<Integer>> permutations;
		List<Color> colorsToPermutateOver;
		boolean patternPossible;
		for (PatternBuilder pb : builders) {
			unsetPositions = pb.getUnsetPositions();
			colorsToPermutateOver = Functions.intersection(pb.getUnusedColors(), possibleColors);
			permutations = Combinatorics.permutation(unsetPositions.size(), colorsToPermutateOver.size());
			patternPossible = true;
			for (List<Integer> permutation : permutations) {
				Iterator<Integer> it = permutation.iterator();
				for (int unsetPosition : unsetPositions) {
					Color nextColor = colorsToPermutateOver.get(it.next());
					if (isPositionPossibleForColor(nextColor, unsetPosition))
						pb.setColor(nextColor, unsetPosition);
					else
						patternPossible = false;
				}
			}
			assert pb.getUnsetPositions().isEmpty();
			if (!patternPossible)
				builders.remove(pb);
		}
		return builders;
	}

	private boolean isPositionPossibleForColor(Color c, int pos) {
		return !patternUsedToGeneratePossiblePatterns.contains(c, pos);	
	}
}
