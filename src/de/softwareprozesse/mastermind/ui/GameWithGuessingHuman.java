package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.model.Color;
import de.softwareprozesse.mastermind.model.Pattern;
import de.softwareprozesse.mastermind.model.Pattern.PatternBuilder;

import java.util.Scanner;

public class GameWithGuessingHuman extends Game {

	private static final String REGEX_FOR_PATTERN_REPRESENTED_BY_NUMBERS = "\\d{4,}+";
	/**
         * generated an game, with a guessing human, and starts it
         * @param mastermind
         */
	public GameWithGuessingHuman(Mastermind mastermind) {
		super(mastermind);
		startGame();
	}
        /**
         * contains the gameloop of an game with a guessing human
         */
	void startGame() {
		System.out.println("Game got started. You are guessing...");
		do {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
			mastermind.commitGuess(TextObjectConverter.buildPatternFromString(parseGuess()));
			printResponse();
		} while (!mastermind.isGameFinished());
		printWinner();
	}
        /**
         * prints the response of the ai
         */
	private void printResponse() {
		System.out.print(TextObjectConverter.patternAnalysisToString(mastermind.getLastPatternAnalysis()));
		System.out.println();
	}
        /**
         *
         * @return returns the next parsed human response
         */
	private String parseGuess() {
		return scanner.next(REGEX_FOR_PATTERN_REPRESENTED_BY_NUMBERS);
	}
        /**
         * prints an welcome message to a game with a guessing player
         */
	private static void printWelcomeMessage() {
		System.out.println("Welcome to Mastermind");
		System.out.println("These are the available colors and their numerical representation: ");
		System.out.println(Color.allColorsToString());
	}
	/**
         * if the player has guessed the correct code, it writes an Congratulationstext,
         * else a loosing-game-text
         */
	private void printWinner() {
		if (mastermind.hasWon()) 
			System.out.println("Congratulations. You have guess the code.");
		else
			System.out.println("You're code was too clever for the AI. Now there will be cake.");
	}
	/**
         * starts the programm and determines, whcih part the user want to play
         * @param args = ignored
         */
	public static void main(String[] args) {
		Pattern solution;
		Scanner scanner = new Scanner(System.in);
		byte answer;
                //Loop until answer is 1 or 2
		do {
			System.out.print("Would you like to be the codemaker (1) or " +
				"the codebreaker (2): ");
			answer = scanner.nextByte();
		} while (answer != 1 && answer != 2);
		printWelcomeMessage();
                //Loop was exited --> answer is 1 or 2, so if(answer==1)...else... will match
		if (answer == 1) {
			System.out.println("Alright. Please create a code.");
			solution = null;
			new GameWithGuessingAI(new Mastermind(solution, true));
		
		} else {
			solution = PatternBuilder.createRandomPattern();
			System.out.println("Okay. A code has been created for you. Please, enter your first guess. ");
			new GameWithGuessingHuman(new Mastermind(solution, false));
		}	
	}
}
