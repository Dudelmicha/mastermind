package de.softwareprozesse.mastermind.ui;

import de.softwareprozesse.mastermind.Mastermind;
import de.softwareprozesse.mastermind.Pattern;

public class GameWithGuessingAI extends Game {
	
	public GameWithGuessingAI(Mastermind mastermind) {
		super(mastermind);
		startGame();
	}

	void startGame() {
		System.out.println("Game got started. AI is guessing...");
		while (!mastermind.isGameFinished()) {
			System.out.print((mastermind.getNumberOfGuesses() + 1) + ".\t");
			mastermind.createAndCommitGuess();
			printGuess(mastermind.getLastGuessedPattern());
			mastermind.commitResponse(TextObjectConverter.buildPatternAnalysisFromString(parseResponse()));
			
		}
		printWinner();
	}
	
	private void printGuess(Pattern guess) {
		System.out.print(guess.toString());
		System.out.println("\t");
	}
	
	private String parseResponse() {
		return scanner.next("s*+w*+'\\.*+");
	}

	private void printWinner() {
		if (mastermind.hasWon()) 			
			System.out.println("I'm sorry, but your code got cracked. The AI won!"); 
		else
			System.out.println("You're code was too clever for the AI. Now there will be cake.");
	}	
}
