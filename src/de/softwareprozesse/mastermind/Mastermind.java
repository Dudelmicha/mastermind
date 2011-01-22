package de.softwareprozesse.mastermind;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.softwareprozesse.mastermind.Pattern.PatternBuilder;
import de.softwareprozesse.mastermind.ai.AI;
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
	
	// TODO nicht jedes Pattern kann analysiert werden
	public void commitGuess(Pattern p) {
		setGuess(p);
		numberOfGuesses++;
		if (solution != null)
			commitResponse(p.analyze(solution));
		
	}
	
	public void commitResponse(PatternAnalysis pa) {
		responses[numberOfGuesses - 1] = pa;
	}
	
	/*public void createAndCommitGuess() {
		Pattern p;
		if (numberOfGuesses == 0)
			p = PatternBuilder.createRandomPattern();
		else
			p = ai.pickPattern();
		commitGuess(p);
	}*/
		
	public void createAndCommitGuess() {
		Pattern p;
		p = PatternBuilder.createRandomPattern();
		commitGuess(p);
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
	
	private Pattern setGuess(Pattern p) {
		return guesses[numberOfGuesses] = p;
	}
	
	public PatternAnalysis getResponse(int i) {
		assert 1 <= i && i <= numberOfGuesses;
		return responses[i - 1];
	}
	
	public Pattern getLastGuessedPattern() {
		return getGuess(numberOfGuesses);
	}

	public PatternAnalysis getLastPatternAnalysis() {
		return getResponse(numberOfGuesses);
	}

	public List<Pattern> getGuesses() {
		return new LinkedList<Pattern>(Arrays.asList(guesses));
	}

	public PatternAnalysis getCorrespondingAnalysis(Pattern p) {
		PatternAnalysis res = null;
		for (int i = 0; i < numberOfGuesses; i++)
			if (guesses[i].equals(p))
				res = responses[i];
		return res;
	}
}
