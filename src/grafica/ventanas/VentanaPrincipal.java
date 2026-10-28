package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class VentanaPrincipal extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JButton btnNuevoAlumno = new JButton("Nuevo alumno");
	private JButton btnListarAlumnos = new JButton("Listar alumnos");
	private JButton btnRegistrarNota = new JButton("Registrar nota");
	private JButton btnObtenerRegistro = new JButton("Obtener registro");
	private JButton btnListarRegistros = new JButton("Listar registros");
	private JButton btnPromedioNotas = new JButton("Promedio de notas");
	private JButton btnAlumnoMayorPromedio = new JButton("Alumno con mayor promedio");
	private JButton btnContarAlumnos = new JButton("Contar alumnos");
	private JButton btnActividadMasCursada = new JButton("Actividad más cursada");

	public VentanaPrincipal() {
		super("Bedelía", 0, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(crearSeccion("Alumnos", btnNuevoAlumno, btnListarAlumnos));
		menu.add(crearSeccion("Registros", btnRegistrarNota, btnObtenerRegistro, btnListarRegistros));
		menu.add(crearSeccion("Consultas", btnPromedioNotas, btnAlumnoMayorPromedio, btnContarAlumnos,
				btnActividadMasCursada));
		add(menu, BorderLayout.CENTER);

		pack();
		setSize(Math.max(getWidth(), 320), getHeight());
		setLocationRelativeTo(null);
	}

	private JPanel crearSeccion(String titulo, JButton... botones) {
		JPanel seccion = new JPanel(new GridLayout(0, 1, 0, 6));
		seccion.setBorder(BorderFactory.createTitledBorder(titulo));
		for (JButton boton : botones) {
			seccion.add(boton);
		}
		seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, seccion.getPreferredSize().height));
		return seccion;
	}

	public void addNuevoAlumnoListener(ActionListener l) {
		btnNuevoAlumno.addActionListener(l);
	}

	public void addListarAlumnosListener(ActionListener l) {
		btnListarAlumnos.addActionListener(l);
	}

	public void addRegistrarNotaListener(ActionListener l) {
		btnRegistrarNota.addActionListener(l);
	}

	public void addObtenerRegistroListener(ActionListener l) {
		btnObtenerRegistro.addActionListener(l);
	}

	public void addListarRegistrosListener(ActionListener l) {
		btnListarRegistros.addActionListener(l);
	}

	public void addPromedioNotasListener(ActionListener l) {
		btnPromedioNotas.addActionListener(l);
	}

	public void addAlumnoMayorPromedioListener(ActionListener l) {
		btnAlumnoMayorPromedio.addActionListener(l);
	}

	public void addContarAlumnosListener(ActionListener l) {
		btnContarAlumnos.addActionListener(l);
	}

	public void addActividadMasCursadaListener(ActionListener l) {
		btnActividadMasCursada.addActionListener(l);
	}

}
