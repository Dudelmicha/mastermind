package de.softwareprozesse.mastermind.utils;

import java.util.LinkedList;
import java.util.List;

public class Combinatorics {

	public static boolean getNextCombination(int[] comb, int k, int n) {
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

	public static List<List<Integer>> combination(int k, int n) {
		List<List<Integer>> l = new LinkedList<List<Integer>>();
		int[] comb = new int[k];
		for (int i = 0; i < k; i++)
			comb[i] = i;
		do {
			l.add(intArrayToList(comb));
		} while (getNextCombination(comb, k, n));
		return l;
	}

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

	public static List<Integer> intArrayToList(int[] array) {
		List<Integer> res = new LinkedList<Integer>();
		for (int i : array)
			res.add(i);
		return res;
	}

	public static void main(String[] args) {
		System.out.println("Combination: ");
		List<List<Integer>> l = combination(2, 4);
		for (List<Integer> il : l) {
			for (int i : il)
				System.out.print(i);
			System.out.println();
		}
		
		System.out.println("Permutation: ");
		List<List<Integer>> l2 =  permutation(2, 3);
		for (List<Integer> il : l2) {
			for (int i : il)
				System.out.print(i);
			System.out.println();
		}
	}
}

