package game.models;

/**
 * Class modelling a case, part of a level
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class Case {
	
	private final int x;
	private final int y;
	private final LineColor lineColor; // is not null if Case is a node
	private Line line; // is not null if Case has a Line
	public Case(int x, int y, LineColor lineColor) {
		this.x = x;
		this.y = y;
		this.lineColor = lineColor;
	}

	public LineColor getCouleur() {
		return lineColor;
	}

	public boolean isExtremite() {
		return this.lineColor != null;
	}
	
	public boolean hasLine() {
		return this.getLine() != null;
	}

	public Line getLine() {
		return line;
	}

	public void setLigne(Line line) {
		this.line = line;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isNextTo(Case dest) {
		int diffX = Math.abs(this.getX() - dest.getX());
		int diffY = Math.abs(this.getY() - dest.getY());
		return diffX + diffY == 1;
	}

}
