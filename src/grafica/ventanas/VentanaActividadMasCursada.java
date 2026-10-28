package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.valueObjects.VOActividad;

public class VentanaActividadMasCursada extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private JTextField txtActividad = crearCampoResultado();
	private JTextField txtVeces = crearCampoResultado();
	private JButton btnConsultar = new JButton("Consultar");

	public VentanaActividadMasCursada() {
		super("Actividad más cursada", 380, 270);

		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Cédula del alumno:", txtCedula);

		JPanel resultado = crearFormularioResultado();
		agregarCampo(resultado, 0, "Actividad:", txtActividad);
		agregarCampo(resultado, 1, "Veces:", txtVeces);

		add(crearPanelConsulta(entrada, resultado), BorderLayout.CENTER);
		add(crearBotonera(btnConsultar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public void mostrarActividad(VOActividad actividad) {
		txtActividad.setText(actividad.getActividad());
		txtVeces.setText(String.valueOf(actividad.getVeces()));
	}

	public void limpiarResultado() {
		txtActividad.setText("");
		txtVeces.setText("");
	}

	public void limpiar() {
		txtCedula.setText("");
		limpiarResultado();
		txtCedula.requestFocus();
	}

	public void addConsultarListener(ActionListener l) {
		btnConsultar.addActionListener(l);
	}

}
