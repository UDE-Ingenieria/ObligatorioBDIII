package grafica.ventanas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

public class RoundedBorder extends AbstractBorder {

	private static final long serialVersionUID = 1L;
	private Color color;
	private int radius;
	private int thickness;

	public RoundedBorder(Color color, int radius, int thickness) {
		this.color = color;
		this.radius = radius;
		this.thickness = thickness;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		for (int i = 0; i < thickness; i++) {
			g2.drawRoundRect(x + i, y + i, width - 1 - 2 * i, height - 1 - 2 * i, radius, radius);
		}
		g2.dispose();
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = radius / 2;
		insets.top = radius / 2;
		insets.right = radius / 2;
		insets.bottom = radius / 2;
		return insets;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}
}
