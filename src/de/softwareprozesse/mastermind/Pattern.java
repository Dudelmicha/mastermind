package de.softwareprozesse.mastermind;

import java.util.Random;

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
			for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++)
				pb.setColor(Color.fromInt(rnd.nextInt(Settings.NUMBER_OF_COLORS) + 1), i);
			return pb.build();
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
	}
	
	private Pattern(PatternBuilder builder) {
		holes = builder.holes;
	}
	
	protected Color getColor(int pos) {
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
			if (getColor(i).equals(solution.getColor(i)))
				correctPos++;
			else
				for (int j = (i + 1) % holes.length; j != i; j = (j + 1) % holes.length) {
					if (getColor(i).equals(solution.getColor(j))) {
						correctColor++;
						break;
					}
				}
		}
		return new PatternAnalysis(correctPos, correctColor);
	}
	
	@Override
	public String toString() {
		String res = "";
		for (Color c : holes) {
			res += c.toString();
		}
		return res;
	}
}
