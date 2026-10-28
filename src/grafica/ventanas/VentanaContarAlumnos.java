package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaContarAlumnos extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtActividad = new JTextField(15);
	private JTextField txtNota = new JTextField(15);
	private JTextField txtCantidad = crearCampoResultado();
	private JButton btnContar = new JButton("Contar");

	public VentanaContarAlumnos() {
		super("Contar alumnos", 380, 270);

		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Actividad:", txtActividad);
		agregarCampo(entrada, 1, "Nota:", txtNota);

		JPanel resultado = crearFormularioResultado();
		agregarCampo(resultado, 0, "Cantidad de alumnos:", txtCantidad);

		add(crearPanelConsulta(entrada, resultado), BorderLayout.CENTER);
		add(crearBotonera(btnContar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getActividad() {
		return txtActividad.getText().trim();
	}

	public String getNota() {
		return txtNota.getText().trim();
	}

	public void mostrarCantidad(int cantidad) {
		txtCantidad.setText(String.valueOf(cantidad));
	}

	public void limpiarResultado() {
		txtCantidad.setText("");
	}

	public void limpiar() {
		txtActividad.setText("");
		txtNota.setText("");
		limpiarResultado();
		txtActividad.requestFocus();
	}

	public void addContarListener(ActionListener l) {
		btnContar.addActionListener(l);
	}

}
