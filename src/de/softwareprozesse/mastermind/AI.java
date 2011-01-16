package de.softwareprozesse.mastermind;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.softwareprozesse.mastermind.utils.Settings;

public class AI {

	private Map<Color, List<Integer>> possiblePositions;
	
	public AI() {
		possiblePositions = new HashMap<Color, List<Integer>>();
		for (Color c : Color.values()) {
			List<Integer> l = new LinkedList<Integer>();
			for (int i = 0; i < Settings.NUMBER_OF_PEGS; i++)
				l.add(i);
			possiblePositions.put(c, l);
		}
	}
}
