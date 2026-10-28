package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logica.valueObjects.VOAlumno;

public class VentanaListarAlumnos extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private DefaultTableModel modelo = crearModeloTabla("Cédula", "Nombre", "Apellido");
	private JTable tabla = new JTable(modelo);
	private JButton btnActualizar = new JButton("Actualizar");

	public VentanaListarAlumnos() {
		super("Listado de alumnos", 480, 360);

		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(crearBotonera(btnActualizar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public void mostrarAlumnos(List<VOAlumno> alumnos) {
		modelo.setRowCount(0);
		for (VOAlumno a : alumnos) {
			modelo.addRow(new Object[] { a.getCedula(), a.getNombre(), a.getApellido() });
		}
	}

	public void addActualizarListener(ActionListener l) {
		btnActualizar.addActionListener(l);
	}

}
