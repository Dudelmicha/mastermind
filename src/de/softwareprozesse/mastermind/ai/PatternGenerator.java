package de.softwareprozesse.mastermind.ai;

import java.util.Iterator;
import java.util.LinkedList;
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
		result = new LinkedList<PatternBuilder>();
		PatternBuilder pb = new PatternBuilder();
		for (int i : indizesAsumedToHaveRightColor)
			pb.setColor(patternUsedToGeneratePossiblePatterns.getColor(i), i);
		result.add(pb);
	}
	
	public List<Pattern> generatePatterns() {
		return permutateUnsetPositions(result);
	}
	
	private List<Pattern> permutateUnsetPositions(List<PatternBuilder> builders) {
		List<Integer> unsetPositions;
		List<List<Integer>> permutations;
		List<Color> colorsToPermutateOver;
		List<Pattern> result = new LinkedList<Pattern>();
		for (PatternBuilder pb : builders) {
			unsetPositions = pb.getUnsetPositions();
			colorsToPermutateOver = Functions.intersection(pb.getUnusedColors(), possibleColors);
			permutations = Combinatorics.permutation(unsetPositions.size(), colorsToPermutateOver.size());
			for (List<Integer> permutation : permutations) {
				PatternBuilder pbnew = new PatternBuilder(pb);
				Iterator<Integer> it = permutation.iterator();
				for (int unsetPosition : unsetPositions) {
					Color nextColor = colorsToPermutateOver.get(it.next());
					if (isPositionPossibleForColor(nextColor, unsetPosition))
						pbnew.setColor(nextColor, unsetPosition);
				}
				if (pbnew.getUnsetPositions().isEmpty())
					result.add(pbnew.build());
			}	
		}
		return result;
	}

	private boolean isPositionPossibleForColor(Color c, int pos) {
		return !patternUsedToGeneratePossiblePatterns.contains(c, pos);	
	}
}
