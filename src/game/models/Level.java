package game.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Class saving level data (Cases, Lines states, level's size)
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class Level {

	private String parameter;
	private int squareLength;
	
	private final HashMap<Character, LineColor> lineColors = new HashMap<Character, LineColor>(); // map characters of parameter string to their LineColor value, in case they have no native correspondance
	private final HashMap<String, Case> cases; // case on line l, column c is saved by key 'lc'
	private final HashMap<LineColor, Line> lines;
	
	public Level(int squareLength) {
		this.parameter = "";
		this.squareLength = squareLength;
		
		this.cases = new HashMap<String, Case>();
		this.lines = new HashMap<LineColor, Line>();

		this.fill();
	}
	
	public Level(String parameter) {
		this.parameter = parameter;
		this.squareLength = (int) Math.ceil(Math.sqrt(parameter.length()));
		
		this.cases = new HashMap<String, Case>();
		this.lines = new HashMap<LineColor, Line>();
		
		this.generateLineCodes();
		this.fill();
	}
	
	public LineColor getRandomUnusedLineColor() {
		List<LineColor> lineColors = new ArrayList<LineColor>(Arrays.asList(LineColor.values()));
		lineColors.removeAll(this.getLineColors().values());
		
		if (lineColors.size() > 0) {
			return lineColors.get(new Random().nextInt(lineColors.size()));
		}
		
		return null;
	}
	
	/**
	 * Fills lineCodes HashMap
	 */
	public void generateLineCodes() {
		// First round : we add characters that have a native LineColor
		for (char ch : this.getParameter().toCharArray()) {
			if (ch != '0' && !this.getLineColors().containsKey(ch)) {
				LineColor lineColor = null;
				
				try {
					
					lineColor = LineColor.valueOf(String.valueOf(ch));
					for (Character ch0 : this.getLineColors().keySet()) {
						LineColor lineColor0 = this.getLineColors().get(ch0);
						if (lineColor0.equals(lineColor) && ch != ch0) lineColor = null;
					}
					
				} catch(IllegalArgumentException e) {
				}
				
				if (lineColor != null) this.getLineColors().put(ch, lineColor);
			}
		}
		
		// Second round : we add characters if there are left LineColors
		for (char ch : this.getParameter().toCharArray()) {
			if (ch != '0' && !this.getLineColors().containsKey(ch)) {
				LineColor lineColor = this.getRandomUnusedLineColor();
				
				if (lineColor != null) this.getLineColors().put(ch, lineColor);
			}
		}
	}
	
	/**
	 * Fills the level according to each char of @cases (0 is an empty case, K is a node case of color K)
	 * @param cases
	 */
	public void fill() {
		this.getCases().clear();
		this.getLines().clear();
		
		if (this.parameter.length() > 0 && this.parameter.length() < this.squareLength * this.squareLength) {
			System.out.println(String.format("Parameter string (%s) is not long enough, level will be completed with empty cases", this.parameter));
		}
		
		for (int i = 0; i < this.getSquareLength(); i++) {
			for (int j = 0; j < this.getSquareLength(); j++) {
				int index = i * this.getSquareLength() + j;
				
				char ch = '0';
				if (index < this.parameter.length()) ch = this.parameter.charAt(index);
				
				LineColor lineColor = null;
				
				if (ch != '0') {
					
					if (this.getLineColors().containsKey(ch)) {
						lineColor = this.getLineColors().get(ch);
					} else System.out.println(String.format("Node %s in level (%s) has no color left => is replaced by empty case", String.valueOf(ch), this.getParameter()));
					
					if (lineColor != null && !this.getLines().containsKey(lineColor)) {
						Line line = new Line(lineColor);
						this.getLines().put(lineColor, line);
					}
				}

				Case case1 = new Case(j, i, lineColor);
				this.setCase(i, j, case1);
			}
		}
	}
	
	public Case getCase(int ligne, int colonne) {
		return this.getCases().get(String.valueOf(ligne) + "." + String.valueOf(colonne));
	}
	
	public Line getLine(LineColor color) {
		return this.getLines().get(color);
	}

	public HashMap<String, Case> getCases() {
		return cases;
	}

	public HashMap<LineColor, Line> getLines() {
		return lines;
	}
	
	public void setCase(int ligne, int colonne, Case newCase) {
		this.getCases().put(String.valueOf(ligne) + "." + String.valueOf(colonne), newCase);
	}
	
	public void removeCase(int ligne, int colonne) {
		String key1 = String.valueOf(ligne) + "." + String.valueOf(colonne);
		Case case1 = this.getCases().get(key1);
		if (case1 != null && case1.hasLine()) case1.getLine().reset(null);
		this.getCases().remove(key1);
	}

	public int getSquareLength() {
		return squareLength;
	}
	
	public void setSquareLength(int squareLength) {
		this.squareLength = squareLength;
	}
	
	public boolean isFinished() {
		for (Case case1 : this.getCases().values()) {
			if (!case1.hasLine()) return false;
		}
		
		for (Line line : this.getLines().values()) {
			if (line.getCases().size() == 1) return false; // for creation mode validation
		}
		
		return true;
	}
	
	public boolean isUnfinished() {
		for (Case case1 : this.getCases().values()) {
			if (case1.hasLine()) return true;
		}
		
		return false;
	}
	
	public int countExtremites(LineColor lineColor) {
		int count = 0;
		for (Case case1 : this.getCases().values()) {
			if (case1.isExtremite() && case1.getLineColor().equals(lineColor)) count ++;
		}
		return count;
	}

	public String getParameter() {
		String parameter = "";
		
		if (this.parameter.length() != this.getSquareLength() * this.getSquareLength()) {
			
			for (int i = 0; i < this.getSquareLength(); i++) {
				for (int j = 0; j < this.getSquareLength(); j++) {
					String ch = "0";
					
					Case case1 = this.getCase(i, j);
					if (case1.isExtremite()) ch = case1.getLineColor().toString();
					
					parameter += ch;
				}
			}
			
		} else parameter = this.parameter;
		
		return parameter;
	}
	
	public boolean isInCreation() {
		return this.parameter.equals("");
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public HashMap<Character, LineColor> getLineColors() {
		return lineColors;
	}
	
	public void zoom() {
		if (this.getSquareLength() > 5) {
			for (int k = 0; k < this.getSquareLength(); k++) {
				this.removeCase(this.getSquareLength() - 1, k);
				this.removeCase(k, this.getSquareLength() - 1);
			}
			
			this.setSquareLength(this.getSquareLength() - 1);
		}
	}
	
	public void dezoom() {
		if (this.getSquareLength() < 25) {
			for (int k = 0; k < this.getSquareLength() + 1; k++) {
				this.setCase(this.getSquareLength(), k, new Case(k, this.getSquareLength(), null));
				this.setCase(k, this.getSquareLength(), new Case(this.getSquareLength(), k, null));
			}
			
			this.setSquareLength(this.getSquareLength() + 1);
		}
	}

}
