package game.controls;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.controls.GameController.GameState;
import game.models.Case;
import game.models.Line;

/**
 * Class displaying game state
 * 
 * @author Romain Milon
 * @author Rapha�l Breteau
 * @version 1.0
 *
 */
public class GamePanel extends JPanel {

	public static final int MENU_LENGTH = 5;
	public static final int MENU_X_MARGIN = GameWindow.WINDOW_LENGTH / 20;
	public static final int MENU_Y_OFFSET = GameWindow.WINDOW_LENGTH / 3;
	
	public static final int MENU_X_SPACE = (GameWindow.WINDOW_LENGTH - 2 * GamePanel.MENU_X_MARGIN) / GamePanel.MENU_LENGTH;
	public static final int MENU_Y_SPACE = (GameWindow.WINDOW_LENGTH - GamePanel.MENU_Y_OFFSET) / GamePanel.MENU_LENGTH;
	
	public static final Color BACKGROUND_COLOR = new Color(46, 49, 49);
	public static final Color SELECT_COLOR = new Color(58, 61, 61);
	public static final Color WHITE = new Color(255, 255, 255);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4433010075620700221L;
	private final GameController controleur;
	
	public GamePanel(GameController controleur) {
		this.controleur = controleur;
	}
	
	public GameController getController() {
		return controleur;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(GamePanel.BACKGROUND_COLOR);
		g.fillRect(0, 0, GameWindow.WINDOW_LENGTH, GameWindow.WINDOW_LENGTH);
		
		if (this.getController().getState().equals(GameState.MAIN_MENU)) {
			// Title
			g.setColor(GamePanel.WHITE);
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 9));
			g.drawString("Free Flow Game", GameWindow.WINDOW_LENGTH / 9, GameWindow.WINDOW_LENGTH / 5);
			
			// Levels
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 30));
			
			for (int i = 0; i <= this.getController().getLevels().size() / GamePanel.MENU_LENGTH; i++) {
				for (int j = 0; j < Math.min(this.getController().getLevels().size() - i * GamePanel.MENU_LENGTH, GamePanel.MENU_LENGTH); j++) {
					int levelId = i * GamePanel.MENU_LENGTH + j;
					
					if (this.getController().getLevels().get(levelId).isFinished()) {
						g.setColor(new Color(0, 177, 106));
						g.fillOval(j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 4 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 4 + GamePanel.MENU_Y_OFFSET, GamePanel.MENU_X_SPACE / 2, GamePanel.MENU_X_SPACE / 2);
					}
					
					g.setColor(GamePanel.WHITE);
					g.drawOval(j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 4 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 4 + GamePanel.MENU_Y_OFFSET, GamePanel.MENU_X_SPACE / 2, GamePanel.MENU_X_SPACE / 2);
					String number = String.valueOf(levelId + 1);
					g.drawString(number, j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 2 - number.length() * GamePanel.MENU_X_SPACE / 18 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 2 + GamePanel.MENU_Y_SPACE / 6 + GamePanel.MENU_Y_OFFSET);
					
				}
			}
		} else if (this.getController().getState().equals(GameState.LEVEL)) {
			int length = this.getController().getLevel().getSquareLength();
			int space = GameWindow.WINDOW_LENGTH / length;
			
			// Background of selected case
			Case selection = this.getController().getSelection();
			if (selection != null) {
				g.setColor(GamePanel.SELECT_COLOR);
				g.fillRect(selection.getX() * space, selection.getY() * space, space, space);
			}
			
			// Level separations
			g.setColor(new Color(255, 255, 255));
			for (int i = 0; i < length-1; i++) {
				g.drawLine(0, (i+1) * space, GameWindow.WINDOW_LENGTH, (i+1) * space);
				g.drawLine((i+1) * space, 0, (i+1) * space, GameWindow.WINDOW_LENGTH);
			}
			
			// Level nodes
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					Case case1 = this.getController().getLevel().getCase(i, j);
					if (case1.isExtremite()) {
						g.setColor(case1.getCouleur().getColor());
						g.fillOval(j * space + space / 4, i * space + space / 4, space / 2, space / 2);
					}
				}
			}
			
			// Line segments
			int step = 5;
			int unit = space / step;
			
			for (Line line : this.getController().getLevel().getLines().values()) {
				if (line.getCases().size() > 1) {
					g.setColor(line.getCouleur().getColor());
					
					int x = line.getCases().get(0).getX();
					int y = line.getCases().get(0).getY();
					for (Case ligneCase : line.getCases().subList(1, line.getCases().size())) {
						int i = ligneCase.getX();
						int j = ligneCase.getY();
						
						if (i == x) {
							g.fillRoundRect(x * space + (step / 2) * unit, Math.min(y, j) * space + (step / 2) * unit, unit, (step + 1) * unit, unit, unit);
						} else if (j == y) {
							g.fillRoundRect(Math.min(x, i) * space + (step / 2) * unit, y * space + (step / 2) * unit, (step + 1) * unit, unit, unit, unit);
						}
						
						x = i; y = j;
					}
				}
			}
		} else if (this.getController().getState().equals(GameState.LEVEL_FINISHED)) {
			g.setColor(GamePanel.WHITE);
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 9));
			g.drawString("Niveau termin�", GameWindow.WINDOW_LENGTH / 8, GameWindow.WINDOW_LENGTH / 3);
			
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 20));
			g.drawString("> Menu principal", 5 * GameWindow.WINDOW_LENGTH / 16, 2 * GameWindow.WINDOW_LENGTH / 3 + GameWindow.WINDOW_LENGTH / 18);
			g.drawRect(GameWindow.WINDOW_LENGTH / 4, 2 * GameWindow.WINDOW_LENGTH / 3, GameWindow.WINDOW_LENGTH / 2, GameWindow.WINDOW_LENGTH / 12);
		}
	}

}
