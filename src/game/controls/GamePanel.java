package game.controls;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import game.models.Case;
import game.models.Line;

/**
 * Class displaying game state
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class GamePanel extends JPanel {

	public static final int MENU_X_LENGTH = 5;
	public static final int MENU_Y_LENGTH = 4;
	
	public static final int MENU_X_MARGIN = GameWindow.WINDOW_LENGTH / 20;
	public static final int MENU_Y_OFFSET = GameWindow.WINDOW_LENGTH / 3;
	
	public static final int MENU_X_SPACE = (GameWindow.WINDOW_LENGTH - 2 * GamePanel.MENU_X_MARGIN) / GamePanel.MENU_X_LENGTH;
	public static final int MENU_Y_SPACE = (GameWindow.WINDOW_LENGTH - GamePanel.MENU_Y_OFFSET) / GamePanel.MENU_Y_LENGTH;
	
	public static final Color BACKGROUND_COLOR = new Color(46, 49, 49);
	public static final Color SELECT_COLOR = new Color(58, 61, 61);
	public static final Color FINISHED_COLOR = new Color(0, 177, 106);
	public static final Color UNFINISHED_COLOR = new Color(249, 105, 14);
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
		
		switch (this.getController().getState()) {
		case MAIN_MENU:
			// Title
			g.setColor(GamePanel.WHITE);
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 9));
			g.drawString("Free Flow Game", GameWindow.WINDOW_LENGTH / 9, GameWindow.WINDOW_LENGTH / 5);

			// Pages
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 30));
			g.drawString("PAGE " + String.valueOf(this.getController().getPageId() + 1), 27 * GameWindow.WINDOW_LENGTH / 60, 4 * GameWindow.WINDOW_LENGTH / 15);
			if (this.getController().getPageId() < this.getController().getMaxPageId()) g.drawString(">", 35 * GameWindow.WINDOW_LENGTH / 60, 4 * GameWindow.WINDOW_LENGTH / 15);
			if (this.getController().getPageId() > 0) g.drawString("<", 24 * GameWindow.WINDOW_LENGTH / 60, 4 * GameWindow.WINDOW_LENGTH / 15);
			
			// Levels
			int offsetId = this.getController().getPageId() * GamePanel.MENU_X_LENGTH * GamePanel.MENU_Y_LENGTH;
			for (int i = 0; i <= this.getController().getLevels().size() / GamePanel.MENU_X_LENGTH; i++) {
				for (int j = 0; j < Math.min(this.getController().getLevels().size() - i * GamePanel.MENU_X_LENGTH - offsetId, GamePanel.MENU_X_LENGTH); j++) {
					int levelId = i * GamePanel.MENU_X_LENGTH + j + offsetId;
					
					// Level backgrounds
					if (this.getController().getLevels().get(levelId).isFinished()) g.setColor(GamePanel.FINISHED_COLOR);
					else if (this.getController().getLevels().get(levelId).isUnfinished()) g.setColor(GamePanel.UNFINISHED_COLOR);
					else g.setColor(GamePanel.SELECT_COLOR);
					g.fillOval(j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 4 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 4 + GamePanel.MENU_Y_OFFSET, GamePanel.MENU_X_SPACE / 2, GamePanel.MENU_X_SPACE / 2);
					
					g.setColor(GamePanel.WHITE);
					g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 30));
					
					// Level icon
					g.drawOval(j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 4 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 4 + GamePanel.MENU_Y_OFFSET, GamePanel.MENU_X_SPACE / 2, GamePanel.MENU_X_SPACE / 2);
					String number = String.valueOf(levelId + 1);
					g.drawString(number, j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 2 - number.length() * GamePanel.MENU_X_SPACE / 21 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 2 + GamePanel.MENU_Y_SPACE / 15 + GamePanel.MENU_Y_OFFSET);

					// Level size
					String length = String.valueOf(this.getController().getLevels().get(levelId).getSquareLength());
					String size = length + "x" + length;
					g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 60));
					g.drawString(size, j * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 2 - size.length() * GamePanel.MENU_X_SPACE / 41 + GamePanel.MENU_X_MARGIN, i * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 2 + GamePanel.MENU_Y_SPACE / 6 + GamePanel.MENU_Y_OFFSET);
					
				}
			}

			if (this.getController().getPageId() == this.getController().getMaxPageId()) {
				// Level add icon
				int remainder = this.getController().getLevels().size() % (GamePanel.MENU_X_LENGTH * GamePanel.MENU_Y_LENGTH);
				int p = remainder / GamePanel.MENU_X_LENGTH;
				int q = remainder % GamePanel.MENU_X_LENGTH;
				
				g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 15));
				g.drawOval(q * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 4 + GamePanel.MENU_X_MARGIN, p * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 4 + GamePanel.MENU_Y_OFFSET, GamePanel.MENU_X_SPACE / 2, GamePanel.MENU_X_SPACE / 2);
				g.drawString("+", q * GamePanel.MENU_X_SPACE + GamePanel.MENU_X_SPACE / 2 - 2 * GamePanel.MENU_X_SPACE / 17 + GamePanel.MENU_X_MARGIN, p * GamePanel.MENU_Y_SPACE + GamePanel.MENU_Y_SPACE / 2 + GamePanel.MENU_Y_SPACE / 7 + GamePanel.MENU_Y_OFFSET);
			}
			
			break;
		case LEVEL: case LEVEL_ADD:
			int length = this.getController().getLevel().getSquareLength();
			int space = GameWindow.WINDOW_LENGTH / length;
			
			// Background of selected case
			Case selection = this.getController().getSelection();
			if (selection != null) {
				g.setColor(GamePanel.SELECT_COLOR);
				g.fillRect(selection.getX() * space, selection.getY() * space, space, space);
			}
			
			// Case separations
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
						g.setColor(case1.getLineColor().getColor());
						g.fillOval(j * space + space / 4, i * space + space / 4, space / 2, space / 2);
					}
				}
			}
			
			// Line segments
			int step = 5;
			int unit = space / step;
			
			for (Line line : this.getController().getLevel().getLines().values()) {
				if (line.getCases().size() > 1) {
					g.setColor(line.getLineColor().getColor());
					
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
			break;
		case LEVEL_FINISHED:
			g.setColor(GamePanel.WHITE);
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 9));
			g.drawString("Niveau terminé", GameWindow.WINDOW_LENGTH / 8, GameWindow.WINDOW_LENGTH / 3);
			
			g.setFont(new Font("Segoe UI", Font.PLAIN, GameWindow.WINDOW_LENGTH / 20));
			g.drawString("> Menu principal", 5 * GameWindow.WINDOW_LENGTH / 16, 2 * GameWindow.WINDOW_LENGTH / 3 + GameWindow.WINDOW_LENGTH / 18);
			g.drawRect(GameWindow.WINDOW_LENGTH / 4, 2 * GameWindow.WINDOW_LENGTH / 3, GameWindow.WINDOW_LENGTH / 2, GameWindow.WINDOW_LENGTH / 12);
			break;
		}
	}

}
