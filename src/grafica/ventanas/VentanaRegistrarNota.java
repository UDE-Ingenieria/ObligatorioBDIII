package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaRegistrarNota extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private JTextField txtActividad = new JTextField(15);
	private JTextField txtNota = new JTextField(15);
	private JButton btnRegistrar = new JButton("Registrar");

	public VentanaRegistrarNota() {
		super("Registrar nota", 380, 220);

		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula del alumno:", txtCedula);
		agregarCampo(formulario, 1, "Actividad:", txtActividad);
		agregarCampo(formulario, 2, "Nota:", txtNota);

		JPanel arriba = new JPanel(new BorderLayout());
		arriba.add(formulario, BorderLayout.NORTH);
		add(arriba, BorderLayout.CENTER);
		add(crearBotonera(btnRegistrar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public String getActividad() {
		return txtActividad.getText().trim();
	}

	public String getNota() {
		return txtNota.getText().trim();
	}

	public void limpiar() {
		txtCedula.setText("");
		txtActividad.setText("");
		txtNota.setText("");
		txtCedula.requestFocus();
	}

	public void addRegistrarListener(ActionListener l) {
		btnRegistrar.addActionListener(l);
	}

}
