package grafica.ventanas;

import java.awt.Color;
import java.awt.Font;

/** Paleta y fuentes compartidas por las ventanas. */
public final class Estilo {

	public static final Color COLOR_PRIMARY = new Color(74, 144, 217);
	public static final Color COLOR_LIGHT_BG = new Color(235, 245, 251);
	public static final Color COLOR_PANEL_BG = new Color(214, 234, 248);
	public static final Color COLOR_WHITE = new Color(255, 255, 255);
	public static final Color COLOR_TEXT = new Color(44, 62, 80);
	public static final Color COLOR_ACCENT = new Color(46, 117, 182);
	public static final Color COLOR_TABLE_ALT = new Color(230, 243, 255);
	public static final Color COLOR_GRID = new Color(210, 230, 245);
	public static final Color COLOR_SECONDARY_BTN = new Color(189, 215, 238);

	public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 13);

	private Estilo() {
	}

}
