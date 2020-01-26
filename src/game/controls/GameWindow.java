package game.controls;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

import game.controls.GameController.GameState;

/**
 * GameWindow
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class GameWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7554685878132584373L;
	public static int WINDOW_LENGTH = 800;
	
	public static final int WINDOW_Y_OFFSET = 47;
	public static final int WINDOW_X_OFFSET = 18;
	
	private final GamePanel gamePanel;
	private final GameListener gameListener;
	public GameWindow(GraphicsConfiguration gc) {
		super(gc);
		
		final GameController gameController = new GameController(GameState.MAIN_MENU);
		this.gamePanel = new GamePanel(gameController);
		this.gameListener = new GameListener(gameController, this.gamePanel);
		
		this.setTitle("Free Flow Game");
		this.setPreferredSize(new Dimension(GameWindow.WINDOW_X_OFFSET + GameWindow.WINDOW_LENGTH, GameWindow.WINDOW_Y_OFFSET + GameWindow.WINDOW_LENGTH));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addMouseListener(this.gameListener);
		this.addMouseWheelListener(this.gameListener);
		this.addKeyListener(this.gameListener);
		
		this.addComponentListener(this.gamePanel);
		this.setContentPane(this.gamePanel);
		
		this.pack();
		this.setVisible(true);
	}

	public GamePanel getLevelPanel() {
		return gamePanel;
	}

	public GameListener getLevelListener() {
		return gameListener;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public GameListener getGameListener() {
		return gameListener;
	}

}