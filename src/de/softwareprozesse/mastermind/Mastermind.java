package de.softwareprozesse.mastermind;

import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Settings;

public class Mastermind {

	private final Pattern solution;
	private int numberOfGuesses;
	private final Pattern[] guesses;
	private final PatternAnalysis[] responses;
	
	public Mastermind(Pattern solution) {
		numberOfGuesses = 0;
		this.solution = solution;
		guesses = new Pattern[Settings.MAX_GUESSES];
		responses = new PatternAnalysis[Settings.MAX_GUESSES];
	}
	
	public PatternAnalysis commitGuess(Pattern p) {
		numberOfGuesses++;
		setGuess(p, numberOfGuesses);
		return setResponse(p.analyze(solution), numberOfGuesses);
	}
	
	public Pattern createGuess() {
		Pattern p;
		if (getNumberOfGuesses() == 0)
			p = PatternBuilder.createRandomPattern();
		else {
			PatternAnalysis lastresponse = getResponse(getNumberOfGuesses());
			if (lastresponse.getNumberOfCorrectColoredPins() + lastresponse.getNumberOfCorrectPositionedPins() < Settings.NUMBER_OF_PEGS)
				;
		}
			
		return p;
	}
	
	private void getRightColors() {
		
	}
	
	/**
	 * checks if game is finished
	 * @return
	 */
	public boolean isGameFinished() {
		return hasWon() || numberOfGuesses == Settings.MAX_GUESSES;
	}

	public boolean hasWon() {
		PatternAnalysis lastresponse = getResponse(getNumberOfGuesses());
		return lastresponse.getNumberOfCorrectPositionedPins() == Settings.NUMBER_OF_PEGS;
	}
	
	public int getNumberOfGuesses() {
		return numberOfGuesses;
	}
	
	/**
	 * gets the past guessed patterns
	 * Index 1 returns the first pattern.
	 * @param i
	 * @return
	 */
	public Pattern getGuess(int i) {
		assert 1 <= i && i <= numberOfGuesses;
		return guesses[i - 1];
	}
	
	private Pattern setGuess(Pattern p, int i) {
		return guesses[i - 1] = p;
	}
	
	public PatternAnalysis getResponse(int i) {
		assert 1 <= i && i <= numberOfGuesses;
		return responses[i - 1];
	}
	
	private PatternAnalysis setResponse(PatternAnalysis pa, int i) {
		return responses[i - 1] = pa;
	}
}
