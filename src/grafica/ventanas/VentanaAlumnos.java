package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logica.valueObjects.VOAlumno;

public class VentanaAlumnos extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = crearCampo();
	private JTextField txtNombre = crearCampo();
	private JTextField txtApellido = crearCampo();
	private JButton btnGuardar = crearBotonPrimario("Guardar");
	private JButton btnLimpiar = crearBotonSecundario("Limpiar");

	private DefaultTableModel modelo = crearModeloTabla("Cédula", "Nombre", "Apellido");
	private JTable tabla = crearTabla(modelo);
	private JButton btnActualizar = crearBotonPrimario("Actualizar listado");

	public VentanaAlumnos() {
		btnLimpiar.addActionListener(e -> limpiarFormulario());

		add(alinearArriba(buildNuevoAlumnoPanel()), BorderLayout.WEST);
		add(buildListadoPanel(), BorderLayout.CENTER);
	}

	private JPanel buildNuevoAlumnoPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula:", txtCedula);
		agregarCampo(formulario, 1, "Nombre:", txtNombre);
		agregarCampo(formulario, 2, "Apellido:", txtApellido);
		return crearSeccionFormulario("Nuevo alumno", formulario, btnGuardar, btnLimpiar);
	}

	private JPanel buildListadoPanel() {
		return crearSeccion("Listado de alumnos", crearScrollTabla(tabla), btnActualizar);
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

	public void limpiarFormulario() {
		txtCedula.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtCedula.requestFocus();
	}

	public void mostrarAlumnos(List<VOAlumno> alumnos) {
		modelo.setRowCount(0);
		for (VOAlumno a : alumnos) {
			modelo.addRow(new Object[] { a.getCedula(), a.getNombre(), a.getApellido() });
		}
	}

	public void addGuardarListener(ActionListener l) {
		btnGuardar.addActionListener(l);
	}

	public void addActualizarListener(ActionListener l) {
		btnActualizar.addActionListener(l);
	}

}
