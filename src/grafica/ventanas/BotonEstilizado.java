package grafica.ventanas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class BotonEstilizado extends JButton {

	private static final long serialVersionUID = 1L;
	private Color colorFondo;
	private Color colorTexto;
	private Color colorHover;
	private boolean hovering = false;

	public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);

	public BotonEstilizado(String text, Color colorFondo, Color colorTexto) {
		super(text);
		this.colorFondo = colorFondo;
		this.colorTexto = colorTexto;
		this.colorHover = colorFondo.darker();

		setFont(FONT_BUTTON);
		setForeground(colorTexto);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setOpaque(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				hovering = true;
				repaint();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				hovering = false;
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (getModel().isPressed()) {
			g2.setColor(colorHover.darker());
		} else if (hovering) {
			g2.setColor(colorHover);
		} else {
			g2.setColor(colorFondo);
		}

		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
		g2.dispose();

		super.paintComponent(g);
	}
}



