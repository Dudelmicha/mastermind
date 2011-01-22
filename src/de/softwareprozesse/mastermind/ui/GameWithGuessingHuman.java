package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Color;
import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.Pattern;
import de.softwareprozesse.mastermind.Pattern.PatternBuilder;

import java.util.Scanner;

public class GameWithGuessingHuman extends Game {

	public GameWithGuessingHuman(Mastermind mastermind) {
		super(mastermind);
		startGame();
	}
		
	void startGame() {
		System.out.println("Game got started. You are guessing...");
		while (!mastermind.isGameFinished()) {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
			mastermind.commitGuess(TextObjectConverter.buildPatternFromString(parseGuess()));
			printResponse();
		}
		printWinner();
	}

	private void printResponse() {
		System.out.print(TextObjectConverter.patternAnalysisToString(mastermind.getLastPatternAnalysis()));
		System.out.println();
	}

	private String parseGuess() {
		return scanner.next("\\d{4,}+");
	}

	private static void printWelcomeMessage() {
		System.out.println("Welcome to Mastermind");
		System.out.println("These are the available colors and their numerical representation: ");
		System.out.println(Color.allColorsToString());
	}
	
	private void printWinner() {
		if (mastermind.hasWon()) 
			System.out.println("Congratulations. You have guess the code.");
		else
			System.out.println("You're code was too clever for the AI. Now there will be cake.");
	}
	
	public static void main(String[] args) {
		Pattern solution;
		Scanner scanner = new Scanner(System.in);
		byte answer;		
		do {
			System.out.println("Would you like to be the codemaker (1) or " +
				"the codebreaker (2): ");
			answer = scanner.nextByte();
		} while (answer != 1 && answer != 2);
		printWelcomeMessage();
		if (answer == 1) {
			System.out.println("Alright. Please create a code.");
			solution = null;
			new GameWithGuessingHuman(new Mastermind(solution, false));
		} else {
			solution = PatternBuilder.createRandomPattern();
			System.out.println("Okay. A code has been created for you. Please, enter your first guess. ");
			new GameWithGuessingAI(new Mastermind(solution, true));
		}	
	}
}