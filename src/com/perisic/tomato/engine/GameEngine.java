package com.perisic.tomato.engine;

import java.awt.image.BufferedImage;

/**
 * Main class where the games are coming from.
 *
 */
public class GameEngine {
	String thePlayer = null;

	/**
	 * Each player has their own game engine.
	 * 
	 * @param player
	 */
	public GameEngine(String player) {
		thePlayer = player;
	}

	int counter = 0;
	int score = 0;
	int tries = 0;
	GameServer theGames = new GameServer();
	Game current = null;

	/**
	 * Retrieves a game. This basic version only has two games that alternate.
	 */
	public BufferedImage nextGame() {
		Game g = theGames.getRandomGame();
		if (g != null) {
			current = g;
			counter++;
		}
		if (current == null) {
			return null;
		}
		return current.getImage();

	}

	/**
	 * Checks if the parameter i is a solution to the game URL. If so, score is
	 * increased by one.
	 * 
	 * @param game
	 * @param i
	 * @return
	 */
	public boolean checkSolution( int i) {
		tries++;
		if (current == null) {
			return false;
		}
		if (i == current.getSolution()) {
			score++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Retrieves the score.
	 * 
	 * @param player
	 * @return
	 */
	public int getScore() {
		return score;
	}

	public int getTries() {
		return tries;
	}

	public String getPlayer() {
		return thePlayer;
	}

}
