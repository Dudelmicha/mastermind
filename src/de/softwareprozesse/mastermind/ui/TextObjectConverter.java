package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.model.Color;
import de.softwareprozesse.mastermind.model.Pattern;
import de.softwareprozesse.mastermind.model.PatternAnalysis;
import de.softwareprozesse.mastermind.model.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Settings;

public class TextObjectConverter {
        /**
         * checks the string, and builds an valid pattern, if the string is valid
         * @param s the specified string to build an valid pattern
         * @return the builded pattern
         */
	public static Pattern buildPatternFromString(String s) {
		if (s.length() != Settings.NUMBER_OF_PEGS)
			throw new IllegalArgumentException("Numbers of pegs don't match. Given: " + s.length() + " .Expected: " + Settings.NUMBER_OF_PEGS);
		PatternBuilder b = new PatternBuilder();
		for (int i = 0; i < s.length(); i++) {
			b.setColor(Color.fromInt(Integer.parseInt(s.substring(i, i + 1))), i);
		}
		return b.build();
	}
	/**
         *
         * @param pa the analysis-object of an pattern
         * @return builds a string containing the result of the analysis
         */
	public static String patternAnalysisToString(PatternAnalysis pa) {
		String res = "";
		int i;
		for (i = 0; i < pa.getNumberOfCorrectPositionedPins(); i++)
			res += "s";
		for (i = 0; i < pa.getNumberOfCorrectColoredPins(); i++)
			res += "w";
		for (i = 0; i < Settings.NUMBER_OF_PEGS - pa.getNumberOfCorrectPositionedPins() - pa.getNumberOfCorrectColoredPins(); i++)
			res += ".";
		return res;
	}
        /**
         *
         * @param response = given response of the user
         * @return the patternanalysis-object of the given response
         */
	public static PatternAnalysis buildPatternAnalysisFromString(String response) {
		return new PatternAnalysis(getNumberOfOccurrences('s', response), getNumberOfOccurrences('w', response));
	}
/**
 *
 * @param c the letter of query
 * @param response the given response which will be analysed
 * @return the number of c in the response
 */
	private static int getNumberOfOccurrences(char c, String response) {
		int number = 0;
		for (int i = 0; i < response.length(); i++)
			if (response.charAt(i) == c)
				number++;
		return number;
	}
}
