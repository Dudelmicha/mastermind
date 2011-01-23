package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.model.Pattern;

public class GameWithGuessingAI extends Game {
	
	private static final String REGEX_FOR_FEEDBACK_TO_PATTERN = "[sw\\.]{4,}+";
	
	public GameWithGuessingAI(Mastermind mastermind) {
		super(mastermind);
		startGame();
	}

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
	
	private void printGuess(Pattern guess) {
		System.out.print(guess.toString());
		System.out.print("\t");
	}
	
	private String parseResponse() {
		return scanner.next(REGEX_FOR_FEEDBACK_TO_PATTERN);
	}

	private void printWinner() {
		if (mastermind.hasWon()) 			
			System.out.println("I'm sorry, but your code got cracked. The AI won!"); 
		else
			System.out.println("You're code was too clever for the AI. Now there will be cake.");
	}	
}
