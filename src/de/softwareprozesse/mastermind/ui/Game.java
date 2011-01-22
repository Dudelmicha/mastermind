package de.softwareprozesse.mastermind.ui;

import java.util.Scanner;

import de.softwareprozesse.mastermind.Mastermind;

public abstract class Game {

	Mastermind mastermind;
	Scanner scanner;
	
	public Game(Mastermind mastermind) {
		this.mastermind = mastermind;
		this.scanner = new Scanner(System.in);
	}
	
	abstract void startGame();
}
