package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;

import java.util.Scanner;

public class Game {

	private Mastermind mastermind;
	private boolean ai;
	private Scanner scanner;
	
	private Game(Mastermind mastermind, boolean ai) {
		this.mastermind = mastermind;
		this.ai = ai;
		this.scanner = new Scanner(System.in);
		startGame();
	}
	
	public static void main(String[] args) {
		Pattern solution;
		Scanner scanner = new Scanner(System.in);
		byte answer;
		boolean ai;
		System.out.println("Welcome to Mastermind");
		do {
			System.out.println("Would you like to be the codemaker (1) or " +
				"the codebreaker (2): ");
			answer = scanner.nextByte();
		} while (answer != 1 && answer != 2);
		
		if (answer == 1) {
			System.out.println("Alright. Please create a code.");
			solution = null;
			ai = false;
		} else {
			solution = PatternBuilder.createRandomPattern();
			System.out.println("Okay. A code has been created for you. Please, enter your first guess. ");
			ai = true;
			
		}
		System.out.println("These are the available colors and their numerical representation: ");
		System.out.println(Color.allColorsToString());
		new Game(new Mastermind(solution, ai), ai);
	}
	
	private void startGame() {
		System.out.println("Game got started.");
		while (!mastermind.isGameFinished()) {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
			if (ai) {
				printGuess(mastermind.createGuess());
				mastermind.setResponse(TextObjectConverter.buildPatternAnalysisFromString(parseResponse()));
			} else {
				mastermind.commitGuess(TextObjectConverter.buildPatternFromString(parseGuess()));
				printResponse();
			}
		}
		printWinner();
	}

	private String parseResponse() {
		return scanner.next("s*+w*+'\\.*+");
	}

	private void printGuess(Pattern guess) {
		System.out.print(guess.toString());
		System.out.println("\t");
		
	}

	private void printResponse() {
		System.out.print(TextObjectConverter.patternAnalysisToString(mastermind.getLastPatternAnalysis()));
		System.out.println();
	}

	private String parseGuess() {
		return scanner.next("\\d{4,}+");
	}

	private void printWinner() {
		if (mastermind.hasWon()) {
			if (ai)
				System.out.println("I'm sorry, but your code got cracked. The AI won!");
			else
				System.out.println("Congratulations. You have guess the code.");
		} else {
			if (ai)
				System.out.println("You're code was too clever for the AI. Now there will be cake.");
			else
				System.out.println("You lost. Please try again next time.");
		}
	}
}
