package de.softwareprozesse.mastermind.ui;


import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Settings;

public class TextInterface {

	public Pattern buildPattern(String s) {
		if (s.length() != Settings.NUMBER_OF_PEGS)
			throw new IllegalArgumentException("Numbers of pegs don't match. Given: " + s.length() + " .Expected: " + Settings.NUMBER_OF_PEGS);
		PatternBuilder b = new PatternBuilder();
		for (int i = 0; i < s.length(); i++) {
			b.setColor(Color.fromInt(Integer.parseInt(s.substring(i, i + 1))), i);
		}
		return b.build();
	}
}
