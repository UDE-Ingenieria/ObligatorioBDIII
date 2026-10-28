package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public abstract class VentanaBase extends JFrame {

	private static final long serialVersionUID = 1L;

	protected VentanaBase(String titulo, int ancho, int alto) {
		super(titulo);
		JPanel contenido = new JPanel(new BorderLayout(0, 10));
		contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		setContentPane(contenido);
		setSize(ancho, alto);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	protected JPanel crearFormulario() {
		return new JPanel(new GridBagLayout());
	}

	protected JPanel crearFormularioResultado() {
		JPanel formulario = crearFormulario();
		formulario.setBorder(BorderFactory.createTitledBorder("Resultado"));
		return formulario;
	}

	protected void agregarCampo(JPanel formulario, int fila, String etiqueta, JComponent campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = fila;
		c.insets = new Insets(4, 4, 4, 4);
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 0;
		formulario.add(new JLabel(etiqueta), c);

		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		formulario.add(campo, c);
	}

	protected JPanel crearPanelConsulta(JComponent entrada, JComponent resultado) {
		JPanel debajo = new JPanel(new BorderLayout());
		debajo.add(resultado, BorderLayout.NORTH);

		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.add(entrada, BorderLayout.NORTH);
		panel.add(debajo, BorderLayout.CENTER);
		return panel;
	}

	protected JTextField crearCampoResultado() {
		JTextField campo = new JTextField(15);
		campo.setEditable(false);
		return campo;
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

	protected JPanel crearBotonera(JButton... botones) {
		JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (JButton boton : botones) {
			botonera.add(boton);
		}
		return botonera;
	}

	protected JButton crearBotonCerrar() {
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener(e -> dispose());
		return cerrar;
	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, getTitle(), JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, getTitle(), JOptionPane.ERROR_MESSAGE);
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
