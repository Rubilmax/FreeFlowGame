package game.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.models.Case;
import game.models.Level;
import game.models.Line;
import game.models.LineColor;

/**
 * Class controlling data flow between game objects and window, panel
 * 
 * @author Romain Milon
 * @author Rapha�l Breteau
 * @version 1.0
 *
 */
public class GameController {
	
	public enum GameState {
		MAIN_MENU, LEVEL, LEVEL_FINISHED;
	}

	private GameState state;
	private final List<Level> levels;
	private int levelId = -1;
	private Case selection;
	public GameController(GameState state) {
		this.state = state;
		this.levels = this.getLevelsData();
	}

	public Level getLevel() {
		if (this.getLevelId() >= 0 && this.getLevelId() < this.getLevels().size()) {
			return this.getLevels().get(this.getLevelId());
		}
		return null;
	}

	public Case getSelection() {
		return selection;
	}

	public void setSelection(Case selection) {
		this.selection = selection;
	}
	
	public void select(Case case1) {
		if (!case1.isExtremite() && !case1.hasLigne()) return;
		
		if (case1.isExtremite()) {
			LineColor lineColor = case1.getCouleur();
			Line line = this.getLevel().getLine(lineColor.toString());
			line.reset(case1);
		} else if (case1.hasLigne()) {
			Line line = case1.getLine();
			line.removeFrom(case1);
			line.add(case1);
		}
		
		this.setSelection(case1);
	}
	
	public void draw(Case next) {
		Case selection = this.getSelection();
		Line current = selection.getLine();// we need to retrieve current ligne before removing cases next's ligne (in case it's the same ligne)
		
		if (next.hasLigne()) {
			Line line = next.getLine();
			line.removeFrom(next);
		}
		
		if (next.isExtremite() && !current.getCouleur().equals(next.getCouleur())) {
			return;
		}
		
		current.add(next);
		if (next.isExtremite() && !next.equals(current.getCases().get(0))) this.setSelection(null); // if we have finished drawing the currently selected line
		else this.setSelection(next);
	}
	
	public void click(Case next) {
		Case prev = this.getSelection();
		if (prev == null || !prev.isNextTo(next) || (prev.isExtremite() && next.isExtremite() && !prev.getCouleur().equals(next.getCouleur()))) {
			this.select(next);
		} else {
			this.draw(next);
		}
		
		if (this.getLevel().isFinished()) {
			this.setState(GameState.LEVEL_FINISHED);
		}
	}
	
	public Case getCase(int x, int y) {
		int length = this.getLevel().getSquareLength();
		int space = GameWindow.WINDOW_LENGTH / length;
		return this.getLevel().getCase((y-32) / space, (x-3) / space);// offset determined by tests on Windows 10 Java 8
	}
	
	/**
	 * Sets level's id based on user's mouse (x, y) position
	 * @param x
	 * @param y
	 */
	public void selectLevel(int x, int y) {
		int i = (y - 3 - GamePanel.MENU_Y_OFFSET - GamePanel.MENU_Y_SPACE / 2) / GamePanel.MENU_Y_SPACE;
		int j = (x - 32) / GamePanel.MENU_X_SPACE;
		this.setLevelId(i * GamePanel.MENU_LENGTH + j);
		if (i >= 0 && j >= 0 && this.getLevel() != null) {
			if (this.getLevel().isFinished()) {
				this.getLevel().fill();
				this.setSelection(null);
			}
			this.setState(GameState.LEVEL);
		}
	}
	
	/**
	 * Retrieve levels data from ./levels.txt
	 */
	@SuppressWarnings("resource")
	public List<Level> getLevelsData() {
		List<Level> levels = new ArrayList<Level>();
		
		try {
			
			String path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			File file = new File(path + FileSystems.getDefault().getSeparator() + "levels.txt");
			
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0) levels.add(new Level(line));
			}
			
		} catch (FileNotFoundException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		return levels;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

}
