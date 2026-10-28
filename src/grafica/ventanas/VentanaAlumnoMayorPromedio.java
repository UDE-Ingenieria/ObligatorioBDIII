package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.valueObjects.VOAlumnoProm;

public class VentanaAlumnoMayorPromedio extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = crearCampoResultado();
	private JTextField txtNombre = crearCampoResultado();
	private JTextField txtApellido = crearCampoResultado();
	private JTextField txtPromedio = crearCampoResultado();
	private JButton btnConsultar = new JButton("Consultar");

	public VentanaAlumnoMayorPromedio() {
		super("Alumno con mayor promedio", 380, 250);

		JPanel resultado = crearFormularioResultado();
		agregarCampo(resultado, 0, "Cédula:", txtCedula);
		agregarCampo(resultado, 1, "Nombre:", txtNombre);
		agregarCampo(resultado, 2, "Apellido:", txtApellido);
		agregarCampo(resultado, 3, "Promedio:", txtPromedio);

		JPanel arriba = new JPanel(new BorderLayout());
		arriba.add(resultado, BorderLayout.NORTH);
		add(arriba, BorderLayout.CENTER);
		add(crearBotonera(btnConsultar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public void mostrarAlumno(VOAlumnoProm alumno) {
		txtCedula.setText(String.valueOf(alumno.getCedula()));
		txtNombre.setText(alumno.getNombre());
		txtApellido.setText(alumno.getApellido());
		txtPromedio.setText(String.format("%.2f", alumno.getPromedio()));
	}

	public void limpiarResultado() {
		txtCedula.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtPromedio.setText("");
	}

	public void addConsultarListener(ActionListener l) {
		btnConsultar.addActionListener(l);
	}

}
