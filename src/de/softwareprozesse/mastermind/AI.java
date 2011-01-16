package de.softwareprozesse.mastermind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Settings;

public class AI {

	private final List<Pattern> possiblePattern;
	private final Map<Color, List<Integer>> possiblePositions;
	
	public AI() {
		possiblePattern = generateAllPossiblePatterns();
		possiblePositions = new HashMap<Color, List<Integer>>();
		for (Color c : Color.values()) {
			List<Integer> l = new LinkedList<Integer>();
			for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++)
				l.add(i);
			possiblePositions.put(c, l);
		}
	}

	public void removePatterns(Pattern guess, PatternAnalysis response) {
		
	}
	
	private void removeAllPatternsWithColor(Color c) {
		for (Pattern p : possiblePattern)
			if (p.contains(c))
				possiblePattern.remove(p);
	}
	
	private void removeAllPatterns(Color c, int pos) {
		for (Pattern p : possiblePattern)
			if (p.contains(c, pos))
				possiblePattern.remove(p);
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
}
