package grafica.ventanas;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaNuevoAlumno extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private JTextField txtNombre = new JTextField(15);
	private JTextField txtApellido = new JTextField(15);
	private JButton btnGuardar = new JButton("Guardar");

	public VentanaNuevoAlumno() {
		super("Nuevo alumno", 380, 220);

		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula:", txtCedula);
		agregarCampo(formulario, 1, "Nombre:", txtNombre);
		agregarCampo(formulario, 2, "Apellido:", txtApellido);

		JPanel arriba = new JPanel(new BorderLayout());
		arriba.add(formulario, BorderLayout.NORTH);
		add(arriba, BorderLayout.CENTER);
		add(crearBotonera(btnGuardar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public String getNombre() {
		return txtNombre.getText().trim();
	}

	public String getApellido() {
		return txtApellido.getText().trim();
	}

	public void limpiar() {
		txtCedula.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtCedula.requestFocus();
	}

	public void addGuardarListener(ActionListener l) {
		btnGuardar.addActionListener(l);
	}

}
