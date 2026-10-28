package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.valueObjects.VORegistro;

public class VentanaObtenerRegistro extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private JTextField txtNumero = new JTextField(15);
	private JTextField txtActividad = crearCampoResultado();
	private JTextField txtNota = crearCampoResultado();
	private JButton btnBuscar = new JButton("Buscar");

	public VentanaObtenerRegistro() {
		super("Obtener registro", 380, 270);

		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Cédula del alumno:", txtCedula);
		agregarCampo(entrada, 1, "Número de registro:", txtNumero);

		JPanel resultado = crearFormularioResultado();
		agregarCampo(resultado, 0, "Actividad:", txtActividad);
		agregarCampo(resultado, 1, "Nota:", txtNota);

		add(crearPanelConsulta(entrada, resultado), BorderLayout.CENTER);
		add(crearBotonera(btnBuscar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public String getNumero() {
		return txtNumero.getText().trim();
	}

	public void mostrarRegistro(VORegistro registro) {
		txtActividad.setText(registro.getActividad());
		txtNota.setText(String.valueOf(registro.getNota()));
	}

	public void limpiarResultado() {
		txtActividad.setText("");
		txtNota.setText("");
	}

	public void limpiar() {
		txtCedula.setText("");
		txtNumero.setText("");
		limpiarResultado();
		txtCedula.requestFocus();
	}

	public void addBuscarListener(ActionListener l) {
		btnBuscar.addActionListener(l);
	}

}
