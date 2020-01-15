package game.controls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Comparator;
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
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class GameController {
	
	public enum GameState {
		MAIN_MENU, LEVEL, LEVEL_FINISHED, LEVEL_ADD;
	}
	
	private String levelsPath;

	private GameState state;
	private final List<Level> levels;
	private int levelId = -1;
	private int pageId = 0;
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
	
	public void select(Case next) {
		boolean creation = this.getState().equals(GameState.LEVEL_ADD);
		if (!next.isExtremite() && !next.hasLine() && !creation) return;

		if (next.isExtremite()) {
			if (creation && next.equals(this.getSelection())) {
				next.setLineColor(null);
				next.setLine(null);
				next = null;
			} else {
				LineColor lineColor = next.getLineColor();
				Line line = this.getLevel().getLine(lineColor);
				line.reset(next);
			}
		} else if (next.hasLine()) {
			if (creation) {
				LineColor currentLineColor = next.getLine().getLineColor();
				if (this.getLevel().countExtremites(currentLineColor) < 2) {
					next.setLineColor(currentLineColor);
					next = null;
				}
			} else {
				Line line = next.getLine();
				line.removeFrom(next);
				line.add(next);
			}
		} else if (creation) {
			LineColor lineColor = this.getLevel().getRandomUnusedLineColor();
			if (lineColor != null) {
				this.getLevel().getLineColors().put(lineColor.toString().charAt(0), lineColor);
				next.setLineColor(lineColor);
				Line line = new Line(lineColor);
				this.getLevel().getLines().put(lineColor, line);
				line.add(next);
			}
		}
		
		this.setSelection(next);
	}
	
	public void draw(Case next) {
		Case selection = this.getSelection();
		Line current = selection.getLine();// we need to retrieve current line before removing cases next's line (in case it's the same ligne)
		
		if (next.hasLine()) {
			Line line = next.getLine();
			line.removeFrom(next);
		}
		
		if (next.isExtremite() && !current.getLineColor().equals(next.getLineColor())) {
			return;
		}
		
		if (!next.hasLine()) current.add(next);
		if (next.isExtremite() && !next.equals(current.getCases().get(0))) this.setSelection(null); // if we have finished drawing the currently selected line
		else this.setSelection(next);
	}
	
	public void action(boolean click, Case next) {
		boolean creation = this.getState().equals(GameState.LEVEL_ADD);
		Case prev = this.getSelection();

		if (prev == null || prev.equals(next) || !prev.isNextTo(next) || (prev.isExtremite() && next.isExtremite() && !prev.getLineColor().equals(next.getLineColor())) || (prev.hasLine() && next.isExtremite() && !prev.getLine().getLineColor().equals(next.getLineColor()))) {
			if (click || (creation && !prev.isNextTo(next))) this.select(next);
		} else {
			this.draw(next);
		}
		
		if (this.getLevel().isFinished()) {
			if(creation) {
				for (Line line : this.getLevel().getLines().values()) {
					line.getCases().get(0).setLineColor(line.getLineColor());
					line.getCases().get(line.getCases().size() - 1).setLineColor(line.getLineColor());
				}
			}
			
			this.getLevel().setParameter(this.getLevel().getParameter());
			if (creation) this.saveLevelsData();
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
		int i = (y - 3 - GamePanel.MENU_Y_OFFSET - GamePanel.MENU_Y_SPACE / 4) / GamePanel.MENU_Y_SPACE;
		int j = (x - 32) / GamePanel.MENU_X_SPACE;
		this.setLevelId(i * GamePanel.MENU_X_LENGTH + j + this.getPageId() * GamePanel.MENU_X_LENGTH * GamePanel.MENU_Y_LENGTH);
		if (i >= 0 && j >= 0 && y >= GamePanel.MENU_Y_OFFSET * 1.1) {
			if (this.getLevelId() == this.getLevels().size()) this.getLevels().add(new Level(5));

			if (this.getLevel() != null) {
				this.setSelection(null);
				if (this.getLevel().isFinished()) this.getLevel().fill();
				
				if (this.getLevel().isInCreation()) this.setState(GameState.LEVEL_ADD);
				else this.setState(GameState.LEVEL);
			}
		}
	}
	
	/**
	 * Retrieve levels data from ./levels.txt
	 */
	public List<Level> getLevelsData() {
		List<Level> levels = new ArrayList<Level>();
		
		String separator = FileSystems.getDefault().getSeparator();
		
		String executionPath = "";
		
		try {
			
			executionPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			
		} catch (URISyntaxException e) {
		}

		Scanner scanner = null;
		
		try {

			this.levelsPath = executionPath + separator + ".." + separator + "levels.txt";
			scanner = new Scanner(new File(this.getLevelsPath()));
			
		} catch (FileNotFoundException e) {
			
			try {

				this.levelsPath = executionPath + separator + ".." + separator + "src" + separator + "levels.txt";
				scanner = new Scanner(new File(this.getLevelsPath()));
				
			} catch (FileNotFoundException e1) {
				
				this.setLevelsPath(null);
				
			}
			
		}
		
		if (scanner != null) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0) levels.add(new Level(line));
			}
			
		}
		
		return levels;
	}
	
	public void saveLevelsData() {
		if (this.getLevelsPath() != null) {

			BufferedWriter writer = null;
			
	        try {

	            writer = new BufferedWriter(new FileWriter(new File(this.getLevelsPath())));
	            
	            this.getLevels().sort(Comparator.comparing(Level::getSquareLength));	            
	            for (Level level : this.getLevels()) {
	            	writer.write(level.getParameter() + System.lineSeparator());
	            }
	            
	        } catch (Exception e) {
	        } finally {
	        	
                try {
                	
					if (writer != null) writer.close();
					
				} catch (IOException e) {
				}
                
	        }
		}
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

	protected void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = Math.min(this.getMaxPageId(), Math.max(0, pageId));
	}
	
	public int getMaxPageId() {
		return this.getLevels().size() / (GamePanel.MENU_X_LENGTH * GamePanel.MENU_Y_LENGTH);
	}

	public String getLevelsPath() {
		return levelsPath;
	}

	public void setLevelsPath(String levelsPath) {
		this.levelsPath = levelsPath;
	}

}
