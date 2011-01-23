package de.softwareprozesse.mastermind.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.softwareprozesse.mastermind.utils.Combinatorics;
import de.softwareprozesse.mastermind.utils.Settings;

/**
 * Represents a pattern of four or five colored pegs.
 * @author christian
 *
 */
public class Pattern {

	private final Color[] holes;
		
	public static class PatternBuilder {
		
		private final Color[] holes;
		
		public PatternBuilder() {
			this.holes = new Color[Settings.NUMBER_OF_PEGS];
		}
		
		public PatternBuilder(PatternBuilder pb) {
			this.holes = new Color[Settings.NUMBER_OF_PEGS];
			for (int i = 0; i < holes.length; i++)
				holes[i] = pb.holes[i];
		}
/**
 *
 * @return the calculated Pattern
 */
		public static Pattern createRandomPattern() {
			Random rnd = new Random();
			PatternBuilder pb = new PatternBuilder();
                        //                                                                  size of one result permutation=4                       5
			List<List<Integer>> allPermutations = Combinatorics.permutation(    Settings.NUMBER_OF_PEGS,
                        //                                                                  number of elements used in the permutation=5
                                                                                            Settings.NUMBER_OF_COLORS);
                        //                 begin count at 0 so maxpos = size()-1
			int randomChoice = rnd.nextInt(allPermutations.size() - 1);
			int pos = 0;
                        // get the 4 results of the Permutation generated before
			for (int i : allPermutations.get(randomChoice)) {
                        // Enum count from 0 to the number of elements used in the permutation=5
                        // setting the patternpositions with the Enum Color
				pb.setColor(Color.fromInt(i + 1), pos);
				pos++;
			}
                        //builrd and returns the pattern
			return pb.build();
		}
				/**
                                 *
                                 * @param color = the Color at the specified hole
                                 * @param pos = the position of the hole
                                 * @return the current Patternbuilder
                                 */
		public PatternBuilder setColor(Color color, int pos) {
                        //checks if the position is legal
			assert 0 <= pos && pos < Settings.NUMBER_OF_PEGS;
                        //sets the hole
			holes[pos] = color;
			return this;
		}
		//checks the holes and return the Pattern, if the Pattern is legal
		public Pattern build() {
			for (Color c : holes) {
				if (c == null)
					throw new IllegalStateException();
			}
			return new Pattern(this);
		}
                /**
                 *
                 * @return  the positions of unconfigured holes,
                 *          but maybe unchecked false-items
                 */
		public List<Integer> getUnsetPositions() {
			List<Integer> res = new LinkedList<Integer>();
			for (int i = 0; i < holes.length; i++)
				if (holes[i] == null)
					res.add(i);
			return res;
		}
/**
 *
 * @return the Colors which are unused
 */
		public List<Color> getUnusedColors() {
			List<Color> res = new LinkedList<Color>();
//                      add all valid colors
			res.addAll(Arrays.asList(Color.values()));
			for (Color c : holes)
//                            check the used holes
				if (c != null)
//                                    remove the used color from the list
					res.remove(c);
//                        return unremoved colors (unused Colors)
			return res;
		}
/**
 *
 * @param pos = the specified position
 * @return the color on the specified position
 */
		public Color getColor(int pos) {
			return holes[pos];
		}
	}
	/**
         *
         * @param builder has the knowledge of the builded holes, an sets them
         */
	private Pattern(PatternBuilder builder) {
		holes = builder.holes;
	}
        /**
         * @see PatternBuilder.getColor
         * @param pos = the specified position
         * @return the color on the specified position
         */
	
	public Color getColor(int pos) {
		assert 0 <= pos && pos < Settings.NUMBER_OF_PEGS;
		return holes[pos];
	}
	
	/**
	 * calculates both the number of pegs that have only the correct color
	 * and the number of pegs that have both color and position right
	 * @param solution the pattern to be guessed
	 * @return a PatternAnalysis object containing aggregating both numbers
	 */
	public PatternAnalysis analyze(Pattern solution) {
		int correctPos = 0;
		int correctColor = 0;
		for (int i = 0; i < holes.length; i++) {
			if (solution.contains(getColor(i), i))
				correctPos++;
			else
				if (solution.contains(getColor(i)))
					correctColor++;
		}
		return new PatternAnalysis(correctPos, correctColor);
	}
	/**
         *
         * @return the list of colors aren't used in this pattern
         */
	public List<Color> getNotContainingColors() {
		List<Color> res = new LinkedList<Color>();
		for (Color c : Color.values())
			if (!contains(c))
				res.add(c);
		return res;
	}
	/**
         *
         * @param othercolor = the searched color
         * @return is the color used in this pattern
         */
	public boolean contains(Color othercolor) {
		boolean res = false;
		for (Color c : holes)
			if (c.equals(othercolor))
				res = true;
		return res;
	}
	/**
         *
         * @param othercolor = the searched color
         * @param pos = the specified position
         * @return is the color used on this position?
         */
	public boolean contains(Color othercolor, int pos) {
		return getColor(pos).equals(othercolor);
	}
	/**
         *
         * @return the Colors used in this pattern
         */
	public List<Color> getColors() {
		return Arrays.asList(holes);
	}
        /**
         *
         * @param colors a list of colors, which maybe used in this pattern
         * @return are the colors used in this pattern?
         */
	public boolean consistsOf(List<Color> colors) {
		boolean res = true;
		for (Color c : colors) {
			if (!contains(c))
				res = false;
		}
		return res;
	}
	/**
         *
         * @return a string containing the colors of the holes
         */
	@Override
	public String toString() {
		String res = "";
		for (Color c : holes) {
			res += c.toString();
		}
		return res;
	}
	/**
         *
         * @param o which could be a pattern
         * @return is o the same pattern like this?
         */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Pattern))
			return false;
		Pattern p = (Pattern) o;
		return Arrays.equals(holes, p.holes);
	}
	/**
         *
         * @return builds an hashcode of this pattern, based on the holes
         */
	@Override
	public int hashCode() {
		int result = 17;
		for (Color c : holes)
			result = 31 * result + Integer.parseInt(c.toString());
		return result;
	}
}
