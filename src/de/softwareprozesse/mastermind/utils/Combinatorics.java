package de.softwareprozesse.mastermind.utils;

import java.util.LinkedList;
import java.util.List;

public class Combinatorics {

	/**
	 * Calculates all possibilities to pick k elements from a set of n elements
	 * The elements are the number from zero to n-1
	 * This algorithm was copied and slightly modified from the book
	 * 'C/C++ Von den Grundlagen zur professionellen Programmierung'
	 * 
	 * @param k	number of elements to pick
	 * @param n	size of the set to choose from
	 * @return all combinations "n over k"
	 */
	public static List<List<Integer>> combination(int k, int n) {
		List<List<Integer>> l = new LinkedList<List<Integer>>();
		if (k == 0) {
			l.add(new LinkedList<Integer>());
			return l;
		}
		int[] comb = new int[k];
		for (int i = 0; i < k; i++)
			comb[i] = i;
		do {
			l.add(intArrayToList(comb));
		} while (getNextCombination(comb, k, n));
		return l;
	}
	
	private static boolean getNextCombination(int[] comb, int k, int n) {
		int i = k - 1;
		comb[i]++;
		while ((i > 0) && (comb[i] >= n - k + 1 + i)) {
			i--;
			comb[i]++;
		}

		if (comb[0] > n - k)
			return false;

		for (i = i + 1; i < k; i++) {
			comb[i] = comb[i - 1] + 1;
		}
		return true;
	}

	/**
	 * Calculates all k permutations of the number 0 to n-1
	 * This algorithm was copied and slightly modified from the book
	 * 'C/C++ Von den Grundlagen zur professionellen Programmierung'
	 * 
	 * @param k	size of one result permutation
	 * @param n number of elements used in the permutation
	 * @return all permutation as list of integer lists
	 */
	public static List<List<Integer>> permutation(int k, int n) {
		return permutationHelper(k, n, new int[k], 0, new LinkedList<List<Integer>>());
	}
	
	private static List<List<Integer>> permutationHelper(int k, int n, int[] array, int x, List<List<Integer>> res) {
		int max;
		
		if (x < k) {
			max = x > 0 ? array[x-1] : -1;
			for (int i = max + 1; i < n - k + x + 1; i++) {
				array[x] = i;
				permutationHelper(k, n, array, x + 1, res);
			}
		} else {
			res.addAll(perm(k, array, 0, new LinkedList<List<Integer>>()));
		}
		return res;
	}
	
	private static List<List<Integer>> perm(int number, int[] array, int start, List<List<Integer>> res) {
		int sav;
		
		if (start < number) {
			sav = array[start];
			for (int i = start; i < number; i++) {
				array[start] = array[i];
				array[i] = sav;
				perm(number, array, start + 1, res);
				array[i] = array[start];
			}
			array[start] = sav;
		} else {
			List<Integer> l = new LinkedList<Integer>();
			for (int i : array)
				l.add(i);
			res.add(l);
		}
		return res;
			
	}

	private static List<Integer> intArrayToList(int[] array) {
		List<Integer> res = new LinkedList<Integer>();
		for (int i : array)
			res.add(i);
		return res;
	}
}

