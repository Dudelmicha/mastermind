package de.softwareprozesse.mastermind;

import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.utils.Settings;

public class Mastermind {

	private final Pattern solution;
	private int numberOfGuesses;
	private final Pattern[] guesses;
	private final PatternAnalysis[] responses;
	private final AI ai;
	
	public Mastermind(Pattern solution, boolean ai) {
		numberOfGuesses = 0;
		this.solution = solution;
		this.ai = (ai ? new AI(this) : null);
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
		else
			p = ai.pickPattern();
		return p;
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
