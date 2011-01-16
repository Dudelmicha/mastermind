package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;

import java.util.Scanner;

public class Game {

	private Mastermind mastermind;
	
	private Game(Mastermind mastermind) {
		this.mastermind = mastermind;
		startGame();
	}
	
	public static void main(String[] args) {
		Pattern solution;
		Scanner scanner = new Scanner(System.in);
		byte answer;
		System.out.println("Welcome to Mastermind");
		do {
			System.out.println("Would you like to be the codemaker (1) or " +
				"the codebreaker (2): ");
			answer = scanner.nextByte();
		} while (answer != 1 && answer != 2);
		
		if (answer == 1) {
			System.out.println("Alright. Please create a code.");
			solution = null;
		} else {
			solution = PatternBuilder.createRandomPattern();
			System.out.println("Okay. A code has been created for you. Please, enter your first guess. ");
			
		}
		System.out.println("These are the available colors and their numerical representation: ");
		System.out.println(Color.allColorsToString());
		new Game(new Mastermind(solution));
	}
	
	private void startGame() {
		System.out.println("Game got started.");
		while (!mastermind.isGameFinished()) {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
		}
	}
}
