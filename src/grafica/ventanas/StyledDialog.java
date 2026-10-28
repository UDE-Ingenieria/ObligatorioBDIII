package grafica.ventanas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class StyledDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public enum DialogType {
		SUCCESS, ERROR, WARNING
	}

	private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
	private static final Color SUCCESS_BG = new Color(234, 250, 241);
	private static final Color ERROR_COLOR = new Color(231, 76, 60);
	private static final Color ERROR_BG = new Color(253, 237, 236);
	private static final Color WARNING_COLOR = new Color(243, 156, 18);
	private static final Color WARNING_BG = new Color(254, 249, 231);

	private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font FONT_MESSAGE = new Font("Segoe UI", Font.PLAIN, 14);
	public static final Color COLOR_PRIMARY = new Color(74, 144, 217);
	public static final Color COLOR_LIGHT_BG = new Color(235, 245, 251);
	public static final Color COLOR_PANEL_BG = new Color(214, 234, 248);
	public static final Color COLOR_WHITE = new Color(255, 255, 255);
	public static final Color COLOR_TEXT = new Color(44, 62, 80);
	public static final Color COLOR_ACCENT = new Color(46, 117, 182);
	public static final Color COLOR_TABLE_ALT = new Color(230, 243, 255);
	public static final Color COLOR_SECONDARY_BTN = new Color(189, 215, 238);
	public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
	
	public StyledDialog(Window owner, DialogType type, String title, String message) {
		super(owner, title, ModalityType.APPLICATION_MODAL);
		setUndecorated(true);
		setSize(480, 200);
		setLocationRelativeTo(owner);

		Color accentColor = getAccentColor(type);
		Color bgColor = getBgColor(type);

		JPanel root = new JPanel(new BorderLayout());
		root.setBackground(COLOR_WHITE);
		root.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));

		JPanel accentBar = new JPanel();
		accentBar.setPreferredSize(new Dimension(6, 0));
		accentBar.setBackground(accentColor);
		root.add(accentBar, BorderLayout.WEST);

		JPanel contentPanel = new JPanel(new BorderLayout(16, 8));
		contentPanel.setBackground(COLOR_WHITE);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 16, 20));

		IconPanel iconPanel = new IconPanel(type, accentColor);
		iconPanel.setPreferredSize(new Dimension(52, 52));
		contentPanel.add(iconPanel, BorderLayout.WEST);

		JPanel textPanel = new JPanel(new BorderLayout(0, 6));
		textPanel.setBackground(COLOR_WHITE);

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(FONT_TITLE);
		lblTitle.setForeground(accentColor);
		textPanel.add(lblTitle, BorderLayout.NORTH);

		JTextArea lblMessage = new JTextArea(message);
		lblMessage.setFont(FONT_MESSAGE);
		lblMessage.setForeground(COLOR_TEXT);
		lblMessage.setBackground(COLOR_WHITE);
		lblMessage.setEditable(false);
		lblMessage.setFocusable(false);
		lblMessage.setLineWrap(true);
		lblMessage.setWrapStyleWord(true);
		lblMessage.setBorder(null);
		textPanel.add(lblMessage, BorderLayout.CENTER);

		contentPanel.add(textPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(bgColor);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		btnPanel.setOpaque(false);
		BotonEstilizado btnOk = new BotonEstilizado("Aceptar", accentColor, COLOR_WHITE);
		btnOk.setPreferredSize(new Dimension(100, 34));
		btnOk.addActionListener(e -> dispose());
		btnPanel.add(btnOk);

		bottomPanel.add(btnPanel, BorderLayout.EAST);

		root.add(contentPanel, BorderLayout.CENTER);
		root.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(root);
		getRootPane().registerKeyboardAction(
				e -> dispose(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		getRootPane().registerKeyboardAction(
				e -> dispose(),
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private Color getAccentColor(DialogType type) {
		switch (type) {
			case SUCCESS: return SUCCESS_COLOR;
			case ERROR: return ERROR_COLOR;
			case WARNING: return WARNING_COLOR;
			default: return COLOR_PRIMARY;
		}
	}

	private Color getBgColor(DialogType type) {
		switch (type) {
			case SUCCESS: return SUCCESS_BG;
			case ERROR: return ERROR_BG;
			case WARNING: return WARNING_BG;
			default: return COLOR_LIGHT_BG;
		}
	}

	private static class IconPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final DialogType type;
		private final Color color;

		IconPanel(DialogType type, Color color) {
			this.type = type;
			this.color = color;
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int size = Math.min(getWidth(), getHeight());
			int x = (getWidth() - size) / 2;
			int y = (getHeight() - size) / 2;

			Color lightBg = new Color(color.getRed(), color.getGreen(), color.getBlue(), 30);
			g2.setColor(lightBg);
			g2.fill(new Ellipse2D.Double(x, y, size, size));

			g2.setColor(color);
			g2.fill(new Ellipse2D.Double(x + 6, y + 6, size - 12, size - 12));

			g2.setColor(Color.WHITE);
			g2.setStroke(new java.awt.BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			int cx = getWidth() / 2;
			int cy = getHeight() / 2;

			switch (type) {
				case SUCCESS:
					g2.drawLine(cx - 8, cy, cx - 3, cy + 6);
					g2.drawLine(cx - 3, cy + 6, cx + 9, cy - 6);
					break;
				case ERROR:
					g2.drawLine(cx - 6, cy - 6, cx + 6, cy + 6);
					g2.drawLine(cx + 6, cy - 6, cx - 6, cy + 6);
					break;
				case WARNING:
					g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					g2.drawLine(cx, cy - 8, cx, cy + 2);
					g2.fillOval(cx - 2, cy + 6, 4, 4);
					break;
			}

			g2.dispose();
		}
	}

	public static void showSuccess(Component parent, String message) {
		show(parent, DialogType.SUCCESS, "\u00c9xito", message);
	}

	public static void showError(Component parent, String message) {
		show(parent, DialogType.ERROR, "Error", message);
	}

	public static void showWarning(Component parent, String message) {
		show(parent, DialogType.WARNING, "Advertencia", message);
	}

	private static void show(Component parent, DialogType type, String title, String message) {
		Window window = parent instanceof Window
				? (Window) parent
				: SwingUtilities.getWindowAncestor(parent);
		StyledDialog dialog = new StyledDialog(window, type, title, message);
		dialog.setVisible(true);
	}
}
