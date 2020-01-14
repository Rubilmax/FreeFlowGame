package game.models;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Line
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public class Line {

	private final LineColor lineColor;
	private final Stack<Case> cases = new Stack<Case>(); // line path
	public Line(LineColor lineColor) {
		this.lineColor = lineColor;
	}
	
	public LineColor getLineColor() {
		return lineColor;
	}

	public Stack<Case> getCases() {
		return cases;
	}
	
	public void reset(Case case1) {
		for (Case case2 : this.getCases()) {
			case2.setLigne(null);
		}
		
		this.getCases().clear();
		if (case1 != null) {
			this.cases.push(case1);
			case1.setLigne(this);
		}
	}
	
	public void add(Case next) {
		this.cases.push(next);
		next.setLigne(this);
	}
	
	public void removeFrom(Case origin) {
		int index = this.getCases().indexOf(origin);
		if (index > -1) {
			// to avoid comodification we clone the removal list
			for (Case case1 : new ArrayList<Case>(this.getCases().subList(index, this.getCases().size()))) {
				case1.setLigne(null);
				this.getCases().remove(case1);
			}
		}
	}
	
}
