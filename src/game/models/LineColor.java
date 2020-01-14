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
	
	R(240, 52, 52),
	O(249, 105, 14),
	S(150, 40, 27),
	G(0, 177, 106),
	B(31, 58, 147),
	C(34, 167, 240),
	Q(255, 255, 126),
	P(103, 65, 114),
	M(191, 85, 236),
	W(242, 241, 239),
	J(29, 209, 161),
	T(1, 163, 164),
	E(255, 159, 243),
	Z(131, 149, 167),
	I(24, 44, 97),
	X(163, 203, 56),
	K(0, 98, 102),
	Y(241, 196, 15),
	D(253, 150, 68);

	private final Color color;
	private LineColor(int r, int g, int b) {
		this.color = new Color(r, g, b);
	}

	public Color getColor() {
		return color;
	}

}
