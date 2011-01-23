package de.softwareprozesse.mastermind.utils;

import java.util.LinkedList;
import java.util.List;

import de.softwareprozesse.mastermind.model.Color;

public class Functions {

	public static List<Color> intersection(List<Color> l1,
			List<Color> l2) {
		List<Color> res = new LinkedList<Color>();
		for (Color c : l1)
			if (l2.contains(c))
				res.add(c);
		return res;
	}
}
