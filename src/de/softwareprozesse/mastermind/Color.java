package de.softwareprozesse.mastermind;

import java.util.HashMap;
import java.util.Map;

public enum Color {

	BLUE(1), RED(2), GREEN(3), BLACK(4), WHITE(5);
	
	private final int number;
	
	Color(int number) {
		this.number = number;
	}
	
	private static final Map<Integer, Color> stringToEnum = new HashMap<Integer, Color>();
	static {
		for (Color c : values())
			stringToEnum.put(c.number, c);
	}
	
	public static Color fromInt(int number) {
		return stringToEnum.get(number);
	}
	
	@Override
	public String toString() {
		return "" + number;
	}

	public static String allColorsToString() {
		String res = "";
		Color[] colors = values();
		for (int i = 0; i < colors.length; i++) {
			res += colors[i].name() + " (" + colors[i].number + ")" + (i == colors.length - 1 ? "\n" : ", ");
		}
		return res;
	}
}
