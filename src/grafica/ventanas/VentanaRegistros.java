package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;

public class VentanaRegistros extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtNotaCedula = crearCampo();
	private JTextField txtNotaActividad = crearCampo();
	private JTextField txtNotaNota = crearCampo();
	private JButton btnRegistrar = crearBotonPrimario("Registrar");
	private JButton btnLimpiarNota = crearBotonSecundario("Limpiar");

	private JTextField txtBuscarCedula = crearCampo();
	private JTextField txtBuscarNumero = crearCampo();
	private JTextField txtBuscarActividad = crearCampoResultado();
	private JTextField txtBuscarNota = crearCampoResultado();
	private JButton btnBuscar = crearBotonPrimario("Buscar");
	private JButton btnLimpiarBusqueda = crearBotonSecundario("Limpiar");

	private JTextField txtListarCedula = crearCampo();
	private DefaultTableModel modelo = crearModeloTabla("Número", "Actividad", "Nota");
	private JTable tabla = crearTabla(modelo);
	private JButton btnListar = crearBotonPrimario("Listar");
	private JButton btnLimpiarListado = crearBotonSecundario("Limpiar");

	public VentanaRegistros() {
		btnLimpiarNota.addActionListener(e -> limpiarRegistrarNota());
		btnLimpiarBusqueda.addActionListener(e -> limpiarObtenerRegistro());
		btnLimpiarListado.addActionListener(e -> limpiarListado());

		JPanel izquierda = new JPanel(new GridLayout(0, 1, 0, 10));
		izquierda.setOpaque(false);
		izquierda.add(buildRegistrarNotaPanel());
		izquierda.add(buildObtenerRegistroPanel());

		add(alinearArriba(izquierda), BorderLayout.WEST);
		add(buildListadoPanel(), BorderLayout.CENTER);
	}

	private JPanel buildRegistrarNotaPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula del alumno:", txtNotaCedula);
		agregarCampo(formulario, 1, "Actividad:", txtNotaActividad);
		agregarCampo(formulario, 2, "Nota:", txtNotaNota);
		return crearSeccionFormulario("Registrar nota", formulario, btnRegistrar, btnLimpiarNota);
	}

	private JPanel buildObtenerRegistroPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula del alumno:", txtBuscarCedula);
		agregarCampo(formulario, 1, "Número de registro:", txtBuscarNumero);
		agregarCampo(formulario, 2, "Actividad:", txtBuscarActividad);
		agregarCampo(formulario, 3, "Nota:", txtBuscarNota);
		return crearSeccionFormulario("Obtener registro", formulario, btnBuscar, btnLimpiarBusqueda);
	}

	private JPanel buildListadoPanel() {
		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Cédula del alumno:", txtListarCedula);

		JPanel contenido = new JPanel(new BorderLayout(0, 6));
		contenido.setOpaque(false);
		contenido.add(entrada, BorderLayout.NORTH);
		contenido.add(crearScrollTabla(tabla), BorderLayout.CENTER);
		return crearSeccion("Registros de un alumno", contenido, btnListar, btnLimpiarListado);
	}

	public String getNotaCedula() {
		return txtNotaCedula.getText().trim();
	}

	public String getNotaActividad() {
		return txtNotaActividad.getText().trim();
	}

	public String getNotaNota() {
		return txtNotaNota.getText().trim();
	}

	public String getBuscarCedula() {
		return txtBuscarCedula.getText().trim();
	}

	public String getBuscarNumero() {
		return txtBuscarNumero.getText().trim();
	}

	public String getListarCedula() {
		return txtListarCedula.getText().trim();
	}

	public void mostrarRegistro(VORegistro registro) {
		txtBuscarActividad.setText(registro.getActividad());
		txtBuscarNota.setText(String.valueOf(registro.getNota()));
	}

	public void mostrarRegistros(List<VORegistroListado> registros) {
		modelo.setRowCount(0);
		for (VORegistroListado r : registros) {
			modelo.addRow(new Object[] { r.getNumero(), r.getActividad(), r.getNota() });
		}
	}

	public void limpiarRegistrarNota() {
		txtNotaCedula.setText("");
		txtNotaActividad.setText("");
		txtNotaNota.setText("");
		txtNotaCedula.requestFocus();
	}

	public void limpiarResultadoRegistro() {
		txtBuscarActividad.setText("");
		txtBuscarNota.setText("");
	}

	public void limpiarObtenerRegistro() {
		txtBuscarCedula.setText("");
		txtBuscarNumero.setText("");
		limpiarResultadoRegistro();
		txtBuscarCedula.requestFocus();
	}

	public void limpiarListado() {
		txtListarCedula.setText("");
		modelo.setRowCount(0);
		txtListarCedula.requestFocus();
	}

	public void addRegistrarListener(ActionListener l) {
		btnRegistrar.addActionListener(l);
	}

	public void addBuscarListener(ActionListener l) {
		btnBuscar.addActionListener(l);
	}

	public void addListarListener(ActionListener l) {
		btnListar.addActionListener(l);
	}

}
