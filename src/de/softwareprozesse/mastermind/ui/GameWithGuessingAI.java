package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.model.Pattern;

public class GameWithGuessingAI extends Game {
	
	private static final String REGEX_FOR_FEEDBACK_TO_PATTERN = "[sw\\.]{4,}+";
	/**
         * generated an game, with a guessing AI, and starts it
         * @param mastermind the current game
         */
	public GameWithGuessingAI(Mastermind mastermind) {
		super(mastermind);
		startGame();
	}
        /**
         * starts the gameloop with an guessing AI
         */
	void startGame() {
		System.out.println("Game got started. AI is guessing...");
		do {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
			mastermind.createAndCommitGuess();
			printGuess(mastermind.getLastGuessedPattern());
			mastermind.commitResponse(TextObjectConverter.buildPatternAnalysisFromString(parseResponse()));
		} while (!mastermind.isGameFinished());
		printWinner();
	}
	/**
         *
         * @param guess which ist guessed by the AI
         */
	private void printGuess(Pattern guess) {
		System.out.print(guess.toString());
		System.out.print("\t");
	}
	/**
         *
         * @return the current Analysispattern of the humanplayer
         */
	private String parseResponse() {
		return scanner.next(REGEX_FOR_FEEDBACK_TO_PATTERN);
	}
        /**
         * if the AI have guessed the correct code, a loosing-game-text will appear
         * else it writes an Congratulationstext to the console
         */
	private void printWinner() {
		if (mastermind.hasWon()) 			
			System.out.println("I'm sorry, but your code got cracked. The AI won!"); 
		else
			System.out.println("You're code was too clever for the AI. Now there will be cake.");
	}	
}
