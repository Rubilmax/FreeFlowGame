package game;

import java.awt.GraphicsConfiguration;

import game.controls.GameWindow;

/**
 * Main class launching the game
 * ONE rule : connect nodes of the same color with flows filling every case!
 * 
 * Select a level with the MOUSE
 * Change level page with ARROWS or QD keys
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
