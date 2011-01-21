package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.PatternAnalysis;
import de.softwareprozesse.mastermind.utils.Settings;

public class TextObjectConverter {

	public static Pattern buildPatternFromString(String s) {
		if (s.length() != Settings.NUMBER_OF_PEGS)
			throw new IllegalArgumentException("Numbers of pegs don't match. Given: " + s.length() + " .Expected: " + Settings.NUMBER_OF_PEGS);
		PatternBuilder b = new PatternBuilder();
		for (int i = 0; i < s.length(); i++) {
			b.setColor(Color.fromInt(Integer.parseInt(s.substring(i, i + 1))), i);
		}
		return b.build();
	}
	
	public static String patternAnalysisToString(PatternAnalysis pa) {
		String res = "";
		int i;
		for (i = 0; i < pa.getNumberOfCorrectPositionedPins(); i++)
			res += "s";
		for (; i < pa.getNumberOfCorrectColoredPins(); i++)
			res += "w";
		for (; i < Settings.NUMBER_OF_PEGS; i++)
			res += ".";
		return res;
	}

	public static PatternAnalysis buildPatternAnalysisFromString(String response) {
		return new PatternAnalysis(getNumberOfOccurrences('s', response), getNumberOfOccurrences('w', response));
	}

	private static int getNumberOfOccurrences(char c, String response) {
		int number = 0;
		for (int i = 0; i < response.length(); i++)
			if (response.charAt(i) == c)
				number++;
		return number;
	}
}
