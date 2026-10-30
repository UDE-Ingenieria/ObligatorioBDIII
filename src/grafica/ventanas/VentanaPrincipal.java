package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane pestanias = new JTabbedPane();

	public VentanaPrincipal() {
		super("Bedelía");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel contenido = new JPanel(new BorderLayout());
		contenido.setBorder(new EmptyBorder(5, 5, 5, 5));
		contenido.setBackground(Estilo.COLOR_LIGHT_BG);
		setContentPane(contenido);

		pestanias.setFont(Estilo.FONT_TITLE);
		pestanias.setBackground(Estilo.COLOR_PANEL_BG);
		pestanias.setForeground(Estilo.COLOR_TEXT);
		add(pestanias, BorderLayout.CENTER);

		setSize(900, 620);
		setMinimumSize(new Dimension(800, 560));
		setLocationRelativeTo(null);
	}

	public void agregarPestania(String titulo, String descripcion, VentanaBase ventana) {
		pestanias.addTab(titulo, null, ventana, descripcion);
	}

	public void showError(String message) {
		StyledDialog.showError(this, message);
	}

}
