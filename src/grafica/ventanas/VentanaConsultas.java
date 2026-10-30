package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.valueObjects.VOActividad;
import logica.valueObjects.VOAlumnoProm;

public class VentanaConsultas extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtPromedioCedula = crearCampo();
	private JTextField txtPromedio = crearCampoResultado();
	private JButton btnCalcularPromedio = crearBotonPrimario("Calcular");
	private JButton btnLimpiarPromedio = crearBotonSecundario("Limpiar");

	private JTextField txtMayorCedula = crearCampoResultado();
	private JTextField txtMayorNombre = crearCampoResultado();
	private JTextField txtMayorApellido = crearCampoResultado();
	private JTextField txtMayorPromedio = crearCampoResultado();
	private JButton btnConsultarMayor = crearBotonPrimario("Consultar");
	private JButton btnLimpiarMayor = crearBotonSecundario("Limpiar");

	private JTextField txtContarActividad = crearCampo();
	private JTextField txtContarNota = crearCampo();
	private JTextField txtContarCantidad = crearCampoResultado();
	private JButton btnContar = crearBotonPrimario("Contar");
	private JButton btnLimpiarContar = crearBotonSecundario("Limpiar");

	private JTextField txtCursadaCedula = crearCampo();
	private JTextField txtCursadaActividad = crearCampoResultado();
	private JTextField txtCursadaVeces = crearCampoResultado();
	private JButton btnConsultarCursada = crearBotonPrimario("Consultar");
	private JButton btnLimpiarCursada = crearBotonSecundario("Limpiar");

	public VentanaConsultas() {
		btnLimpiarPromedio.addActionListener(e -> limpiarPromedio());
		btnLimpiarMayor.addActionListener(e -> limpiarResultadoMayorPromedio());
		btnLimpiarContar.addActionListener(e -> limpiarContar());
		btnLimpiarCursada.addActionListener(e -> limpiarActividadMasCursada());

		JPanel secciones = new JPanel(new GridLayout(0, 2, 10, 10));
		secciones.setOpaque(false);
		secciones.add(buildPromedioPanel());
		secciones.add(buildMayorPromedioPanel());
		secciones.add(buildContarPanel());
		secciones.add(buildActividadMasCursadaPanel());

		add(secciones, BorderLayout.NORTH);
	}

	private JPanel buildPromedioPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula del alumno:", txtPromedioCedula);
		agregarCampo(formulario, 1, "Promedio:", txtPromedio);
		return crearSeccionFormulario("Promedio de notas", formulario, btnCalcularPromedio, btnLimpiarPromedio);
	}

	private JPanel buildMayorPromedioPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula:", txtMayorCedula);
		agregarCampo(formulario, 1, "Nombre:", txtMayorNombre);
		agregarCampo(formulario, 2, "Apellido:", txtMayorApellido);
		agregarCampo(formulario, 3, "Promedio:", txtMayorPromedio);
		return crearSeccionFormulario("Alumno con mayor promedio", formulario, btnConsultarMayor, btnLimpiarMayor);
	}

	private JPanel buildContarPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Actividad:", txtContarActividad);
		agregarCampo(formulario, 1, "Nota:", txtContarNota);
		agregarCampo(formulario, 2, "Cantidad de alumnos:", txtContarCantidad);
		return crearSeccionFormulario("Contar alumnos", formulario, btnContar, btnLimpiarContar);
	}

	private JPanel buildActividadMasCursadaPanel() {
		JPanel formulario = crearFormulario();
		agregarCampo(formulario, 0, "Cédula del alumno:", txtCursadaCedula);
		agregarCampo(formulario, 1, "Actividad:", txtCursadaActividad);
		agregarCampo(formulario, 2, "Veces:", txtCursadaVeces);
		return crearSeccionFormulario("Actividad más cursada", formulario, btnConsultarCursada, btnLimpiarCursada);
	}

	public String getPromedioCedula() {
		return txtPromedioCedula.getText().trim();
	}

	public String getContarActividad() {
		return txtContarActividad.getText().trim();
	}

	public String getContarNota() {
		return txtContarNota.getText().trim();
	}

	public String getCursadaCedula() {
		return txtCursadaCedula.getText().trim();
	}

	public void mostrarPromedio(double promedio) {
		txtPromedio.setText(String.format("%.2f", promedio));
	}

	public void mostrarAlumnoMayorPromedio(VOAlumnoProm alumno) {
		txtMayorCedula.setText(String.valueOf(alumno.getCedula()));
		txtMayorNombre.setText(alumno.getNombre());
		txtMayorApellido.setText(alumno.getApellido());
		txtMayorPromedio.setText(String.format("%.2f", alumno.getPromedio()));
	}

	public void mostrarCantidad(int cantidad) {
		txtContarCantidad.setText(String.valueOf(cantidad));
	}

	public void mostrarActividad(VOActividad actividad) {
		txtCursadaActividad.setText(actividad.getActividad());
		txtCursadaVeces.setText(String.valueOf(actividad.getVeces()));
	}

	public void limpiarResultadoPromedio() {
		txtPromedio.setText("");
	}

	public void limpiarPromedio() {
		txtPromedioCedula.setText("");
		limpiarResultadoPromedio();
		txtPromedioCedula.requestFocus();
	}

	public void limpiarResultadoMayorPromedio() {
		txtMayorCedula.setText("");
		txtMayorNombre.setText("");
		txtMayorApellido.setText("");
		txtMayorPromedio.setText("");
	}

	public void limpiarResultadoContar() {
		txtContarCantidad.setText("");
	}

	public void limpiarContar() {
		txtContarActividad.setText("");
		txtContarNota.setText("");
		limpiarResultadoContar();
		txtContarActividad.requestFocus();
	}

	public void limpiarResultadoActividadMasCursada() {
		txtCursadaActividad.setText("");
		txtCursadaVeces.setText("");
	}

	public void limpiarActividadMasCursada() {
		txtCursadaCedula.setText("");
		limpiarResultadoActividadMasCursada();
		txtCursadaCedula.requestFocus();
	}

	public void addCalcularPromedioListener(ActionListener l) {
		btnCalcularPromedio.addActionListener(l);
	}

	public void addConsultarMayorPromedioListener(ActionListener l) {
		btnConsultarMayor.addActionListener(l);
	}

	public void addContarListener(ActionListener l) {
		btnContar.addActionListener(l);
	}

	public void addConsultarActividadMasCursadaListener(ActionListener l) {
		btnConsultarCursada.addActionListener(l);
	}

}
