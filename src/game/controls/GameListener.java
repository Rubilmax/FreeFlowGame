package game.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import game.controls.GameController.GameState;
import game.models.Case;

/**
 * Class listening to keys and mouse for game controls
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class GameListener extends MouseAdapter implements KeyListener, MouseWheelListener {
	
	private final GameController controleur;
	private final GamePanel panel;
	
	public GameListener(GameController controleur, GamePanel panel) {
		this.controleur = controleur;
		this.panel = panel;
	}
	
	public GameController getController() {
		return controleur;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		switch (this.getController().getState()) {
		case MAIN_MENU:
			this.getController().selectLevel(event.getX(), event.getY());
			break;
		case LEVEL: case LEVEL_ADD:
			Case next = this.getController().getCase(event.getX(), event.getY());
			this.getController().action(true, next);
			break;
		case LEVEL_FINISHED:
			this.getController().setState(GameState.MAIN_MENU);
			break;
		}
		
		this.getPanel().repaint();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch(this.getController().getState()) {
		case MAIN_MENU:
			switch (event.getKeyCode()) {
			case KeyEvent.VK_Q: case KeyEvent.VK_LEFT:
				this.getController().setPageId(this.getController().getPageId() - 1);
				break;
			case KeyEvent.VK_D: case KeyEvent.VK_RIGHT:
				this.getController().setPageId(this.getController().getPageId() + 1);
				break;
			default:
				break;
			}
			break;
		case LEVEL: case LEVEL_ADD:
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE) this.getController().setState(GameState.MAIN_MENU);
			
			Case prev = this.getController().getSelection();
			if (prev != null) {
				Case next = null;
				switch (event.getKeyCode()) {
				case KeyEvent.VK_Z: case KeyEvent.VK_UP:
					next = this.getController().getLevel().getCase(prev.getY() - 1, prev.getX());
					break;
				case KeyEvent.VK_Q: case KeyEvent.VK_LEFT:
					next = this.getController().getLevel().getCase(prev.getY(), prev.getX() - 1);
					break;
				case KeyEvent.VK_S: case KeyEvent.VK_DOWN:
					next = this.getController().getLevel().getCase(prev.getY() + 1, prev.getX());
					break;
				case KeyEvent.VK_D: case KeyEvent.VK_RIGHT:
					next = this.getController().getLevel().getCase(prev.getY(), prev.getX() + 1);
					break;
				case KeyEvent.VK_ENTER: case KeyEvent.VK_SPACE:
					next = this.getController().getLevel().getCase(prev.getY(), prev.getX());
					break;
				default:
					break;
				}
				if (next != null) this.getController().action(false, next);
			}
			break;
		case LEVEL_FINISHED:
			if (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_SPACE) {
				this.getController().setState(GameState.MAIN_MENU);
			}
			break;
		}
		
		this.getPanel().repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		int units = event.getUnitsToScroll();
		
		switch (this.getController().getState()) {
		case LEVEL_ADD:
			if (units > 0) this.getController().getLevel().dezoom();
			else this.getController().getLevel().zoom();
			break;
		case MAIN_MENU:
			if (units > 0) this.getController().setPageId(this.getController().getPageId() + 1);
			else this.getController().setPageId(this.getController().getPageId() - 1);
			break;
		default:
			break;
		}
		
		this.getPanel().repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public GamePanel getPanel() {
		return panel;
	}

}
