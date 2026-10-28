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

import logica.valueObjects.VORegistroListado;

public class VentanaListarRegistros extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private DefaultTableModel modelo = crearModeloTabla("Número", "Actividad", "Nota");
	private JTable tabla = new JTable(modelo);
	private JButton btnListar = new JButton("Listar");

	public VentanaListarRegistros() {
		super("Registros de un alumno", 480, 380);

		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Cédula del alumno:", txtCedula);

		add(entrada, BorderLayout.NORTH);
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(crearBotonera(btnListar, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public void mostrarRegistros(List<VORegistroListado> registros) {
		modelo.setRowCount(0);
		for (VORegistroListado r : registros) {
			modelo.addRow(new Object[] { r.getNumero(), r.getActividad(), r.getNota() });
		}
	}

	public void limpiar() {
		txtCedula.setText("");
		modelo.setRowCount(0);
		txtCedula.requestFocus();
	}

	public void addListarListener(ActionListener l) {
		btnListar.addActionListener(l);
	}

}
