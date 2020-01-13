package game.models;

import java.awt.Color;

/**
 * LineColor
 * 
 * @author Romain Milon
 * @author Raphaël Breteau
 * @version 1.0
 *
 */
public enum LineColor {
	
	R(new Color(240, 52, 52)),
	O(new Color(249, 105, 14)),
	S(new Color(150, 40, 27)),
	G(new Color(0, 177, 106)),
	B(new Color(31, 58, 147)),
	C(new Color(34, 167, 240)),
	Y(new Color(255, 255, 126)),
	P(new Color(103, 65, 114)),
	M(new Color(191, 85, 236)),
	W(new Color(242, 241, 239)),
	J(new Color(123, 239, 178));

	private final Color color;
	private LineColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

}
