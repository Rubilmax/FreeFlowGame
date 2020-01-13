package game.models;

import java.util.HashMap;

/**
 * Class saving level data (Cases, Lines states, level's size)
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class Level {

	private final String parameter;
	private final int squareLength;
	
	private final HashMap<String, Case> cases; // case on line l, column c is saved by key 'lc'
	private final HashMap<String, Line> lines;
	public Level(String parameter, int squareLength) {
		this.parameter = parameter;
		this.squareLength = squareLength;
		
		this.cases = new HashMap<String, Case>();
		this.lines = new HashMap<String, Line>();
		
		this.fill();
	}
	
	public Level(String parameter) {
		this.parameter = parameter;
		this.squareLength = (int) Math.ceil(Math.sqrt(parameter.length()));
		
		this.cases = new HashMap<String, Case>();
		this.lines = new HashMap<String, Line>();
		
		this.fill();
	}
	
	/**
	 * Fills the level according to each char of @cases (0 is an empty case, K is a node case of color K)
	 * @param cases
	 */
	public void fill() {
		this.getCases().clear();
		this.getLines().clear();
		
		if (this.getParameter().length() < this.squareLength * this.squareLength) {
			System.out.println(String.format("Parameter string (%s) is not long enough, level will be completed with empty cases", this.getParameter()));
		}
		
		for (int i = 0; i < this.squareLength; i++) {
			for (int j = 0; j < this.squareLength; j++) {
				int index = i * this.squareLength + j;
				
				char ch = '0';
				if (index < this.getParameter().length()) ch = this.getParameter().charAt(index);
				
				LineColor lineColor = null;
				
				try {
					
					lineColor = LineColor.valueOf(String.valueOf(ch));
					
					if (!this.getLines().containsKey(lineColor.toString())) {
						Line line = new Line(lineColor);
						this.getLines().put(lineColor.toString(), line);
					}
					
				} catch(IllegalArgumentException exception) {
				}

				Case case1 = new Case(j, i, lineColor);
				this.addCase(i, j, case1);
			}
		}
	}
	
	public Case getCase(int ligne, int colonne) {
		return this.getCases().get(String.valueOf(ligne) + String.valueOf(colonne));
	}
	
	public Line getLine(String color) {
		return this.getLines().get(color);
	}

	public HashMap<String, Case> getCases() {
		return cases;
	}

	public HashMap<String, Line> getLines() {
		return lines;
	}
	
	public void addCase(int ligne, int colonne, Case newCase) {
		this.getCases().put(String.valueOf(ligne) + String.valueOf(colonne), newCase);
	}

	public int getSquareLength() {
		return squareLength;
	}
	
	public boolean isFinished() {
		for (Case case1 : this.getCases().values()) {
			if (!case1.hasLine()) return false;
		}
		return true;
	}

	public String getParameter() {
		return parameter;
	}

}
