package game;

import java.awt.GraphicsConfiguration;

import game.controls.GameWindow;

/**
 * Main class launching the game
 * 
 * Select a level with the MOUSE
 * Control flows with the MOUSE, ARROWS or with ZQSD keys
 * Pause current game and return to main menu with ESCAPE
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class Game {

	public static GraphicsConfiguration gc;
	public static void main(String[] args) {
		new GameWindow(gc);
	}

}
