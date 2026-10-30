package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Base de las ventanas de cada entidad. Cada una es un panel dividido en secciones (una por operacion) que se
 * muestra en una pestania de la ventana principal.
 */
public abstract class VentanaBase extends JPanel {

	private static final long serialVersionUID = 1L;

	protected VentanaBase() {
		super(new BorderLayout(10, 10));
		setBackground(Estilo.COLOR_LIGHT_BG);
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
	}

	protected JPanel crearSeccion(String titulo, JComponent contenido, JButton... botones) {
		JPanel seccion = new JPanel(new BorderLayout(0, 6));
		seccion.setBackground(Estilo.COLOR_WHITE);
		seccion.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(Estilo.COLOR_PRIMARY, 16, 1),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(Estilo.FONT_TITLE);
		lblTitulo.setForeground(Estilo.COLOR_ACCENT);
		lblTitulo.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 0));
		seccion.add(lblTitulo, BorderLayout.NORTH);

		seccion.add(contenido, BorderLayout.CENTER);
		if (botones.length > 0) {
			seccion.add(crearBotonera(botones), BorderLayout.SOUTH);
		}
		return seccion;
	}

	// El formulario queda pegado arriba en vez de estirarse a lo alto de la seccion
	protected JPanel crearSeccionFormulario(String titulo, JPanel formulario, JButton... botones) {
		return crearSeccion(titulo, alinearArriba(formulario), botones);
	}

	protected JPanel alinearArriba(JComponent componente) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(componente, BorderLayout.NORTH);
		return panel;
	}

	protected JPanel crearFormulario() {
		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setOpaque(false);
		return formulario;
	}

	protected void agregarCampo(JPanel formulario, int fila, String etiqueta, JComponent campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = fila;
		c.insets = new Insets(4, 4, 4, 4);
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 0;
		JLabel lbl = new JLabel(etiqueta);
		lbl.setFont(Estilo.FONT_LABEL);
		lbl.setForeground(Estilo.COLOR_TEXT);
		formulario.add(lbl, c);

		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		formulario.add(campo, c);
	}

	protected JTextField crearCampo() {
		JTextField campo = new JTextField(15);
		campo.setFont(Estilo.FONT_LABEL);
		campo.setForeground(Estilo.COLOR_TEXT);
		return campo;
	}

	protected JTextField crearCampoResultado() {
		JTextField campo = crearCampo();
		campo.setEditable(false);
		campo.setBackground(Estilo.COLOR_LIGHT_BG);
		return campo;
	}

	protected BotonEstilizado crearBotonPrimario(String texto) {
		return new BotonEstilizado(texto, Estilo.COLOR_PRIMARY, Estilo.COLOR_WHITE);
	}

	protected BotonEstilizado crearBotonSecundario(String texto) {
		return new BotonEstilizado(texto, Estilo.COLOR_SECONDARY_BTN, Estilo.COLOR_TEXT);
	}

	protected DefaultTableModel crearModeloTabla(String... columnas) {
		return new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
	}

	protected JTable crearTabla(DefaultTableModel modelo) {
		JTable tabla = new JTable(modelo);
		tabla.setFillsViewportHeight(true);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setFont(Estilo.FONT_LABEL);
		tabla.setRowHeight(26);
		tabla.setGridColor(Estilo.COLOR_GRID);
		tabla.setSelectionBackground(Estilo.COLOR_PANEL_BG);
		tabla.setSelectionForeground(Estilo.COLOR_TEXT);
		tabla.setShowHorizontalLines(true);
		tabla.setShowVerticalLines(false);
		tabla.setIntercellSpacing(new Dimension(0, 1));

		tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				lbl.setBackground(Estilo.COLOR_PRIMARY);
				lbl.setForeground(Estilo.COLOR_WHITE);
				lbl.setFont(Estilo.FONT_TITLE);
				lbl.setHorizontalAlignment(JLabel.CENTER);
				lbl.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 0, 2, 0, Estilo.COLOR_ACCENT),
						BorderFactory.createEmptyBorder(6, 8, 6, 8)));
				lbl.setOpaque(true);
				return lbl;
			}
		});

		tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? Estilo.COLOR_WHITE : Estilo.COLOR_TABLE_ALT);
				}
				c.setForeground(Estilo.COLOR_TEXT);
				setHorizontalAlignment(JLabel.CENTER);
				return c;
			}
		});
		return tabla;
	}

	protected JScrollPane crearScrollTabla(JTable tabla) {
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.getViewport().setBackground(Estilo.COLOR_WHITE);
		scroll.setBorder(BorderFactory.createLineBorder(Estilo.COLOR_PANEL_BG, 1));
		return scroll;
	}

	protected JPanel crearBotonera(JButton... botones) {
		JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
		botonera.setOpaque(false);
		for (JButton boton : botones) {
			botonera.add(boton);
		}
		return botonera;
	}

	public void showSuccess(String message) {
		StyledDialog.showSuccess(this, message);
	}

	public void showError(String message) {
		StyledDialog.showError(this, message);
	}

	public void showWarning(String message) {
		StyledDialog.showWarning(this, message);
	}

}
