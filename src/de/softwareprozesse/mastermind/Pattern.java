package de.softwareprozesse.mastermind;

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
		
		public static Pattern createRandomPattern() {
			Random rnd = new Random();
			PatternBuilder pb = new PatternBuilder();
			List<List<Integer>> allPermutations = Combinatorics.permutation(Settings.NUMBER_OF_PEGS, Settings.NUMBER_OF_COLORS);
			int randomChoice = rnd.nextInt(allPermutations.size() - 1);
			int pos = 0;
			for (int i : allPermutations.get(randomChoice)) {
				pb.setColor(Color.fromInt(i + 1), pos);
				pos++;
			}
			return pb.build();
		}
		
		@Deprecated
		public int getNextUnsetPosition(int pos) {
			while (++pos < holes.length) {
				if (holes[pos] == null)
					return pos;
			}
			return -1;
		}
		
		public PatternBuilder setColor(Color color, int pos) {
			assert 0 <= pos && pos < Settings.NUMBER_OF_PEGS;
			holes[pos] = color;
			return this;
		}
		
		public Pattern build() {
			for (Color c : holes) {
				if (c == null)
					throw new IllegalStateException();
			}
			return new Pattern(this);
		}

		public List<Integer> getUnsetPositions() {
			List<Integer> res = new LinkedList<Integer>();
			for (int i = 0; i < holes.length; i++)
				if (holes[i] == null)
					res.add(i);
			return res;
		}

		public List<Color> getUnusedColors() {
			List<Color> res = Arrays.asList(Color.values());
			for (Color c : holes)
				if (c != null)
					res.remove(c);
			return res;
		}

		public Color getColor(int i) {
			return holes[i];
		}
	}
	
	private Pattern(PatternBuilder builder) {
		holes = builder.holes;
	}
	
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
	
	public List<Color> getNotContainingColors() {
		List<Color> res = new LinkedList<Color>();
		for (Color c : Color.values())
			if (!contains(c))
				res.add(c);
		return res;
	}
	
	public boolean contains(Color othercolor) {
		boolean res = false;
		for (Color c : holes)
			if (c.equals(othercolor))
				res = true;
		return res;
	}
	
	public boolean contains(Color othercolor, int pos) {
		return getColor(pos).equals(othercolor);
	}
	
	@Override
	public String toString() {
		String res = "";
		for (Color c : holes) {
			res += c.toString();
		}
		return res;
	}

	public List<Color> getColors() {
		return Arrays.asList(holes);
	}

	public boolean contains(List<Color> colors) {
		boolean res = true;
		for (Color c : colors) {
			if (!contains(c))
				res = false;
		}
		return res;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Pattern))
			return false;
		Pattern p = (Pattern) o;
		return Arrays.equals(holes, p.holes);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		for (Color c : holes)
			result = 31 * result + Integer.parseInt(c.toString());
		return result;
	}
}
