package grafica.controladores;

import java.awt.Point;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;

import grafica.ventanas.VentanaActividadMasCursada;
import grafica.ventanas.VentanaAlumnoMayorPromedio;
import grafica.ventanas.VentanaContarAlumnos;
import grafica.ventanas.VentanaListarAlumnos;
import grafica.ventanas.VentanaListarRegistros;
import grafica.ventanas.VentanaNuevoAlumno;
import grafica.ventanas.VentanaObtenerRegistro;
import grafica.ventanas.VentanaPrincipal;
import grafica.ventanas.VentanaPromedioNotas;
import grafica.ventanas.VentanaRegistrarNota;
import logica.IFachada;

public class ControladorPrincipal {

	// private IFachada fachada;
	private VentanaPrincipal ventana;

	private ControladorVentanaNuevoAlumno controladorNuevoAlumno;
	private ControladorVentanaListarAlumnos controladorListarAlumnos;
	private ControladorVentanaRegistrarNota controladorRegistrarNota;
	private ControladorVentanaObtenerRegistro controladorObtenerRegistro;
	private ControladorVentanaListarRegistros controladorListarRegistros;
	private ControladorVentanaPromedioNotas controladorPromedioNotas;
	private ControladorVentanaAlumnoMayorPromedio controladorAlumnoMayorPromedio;
	private ControladorVentanaContarAlumnos controladorContarAlumnos;
	private ControladorVentanaActividadMasCursada controladorActividadMasCursada;

	public ControladorPrincipal(VentanaPrincipal ventana) {
		this.ventana = ventana;
		initCapaLogica();
/*
		controladorNuevoAlumno = new ControladorVentanaNuevoAlumno(fachada, new VentanaNuevoAlumno());
		controladorListarAlumnos = new ControladorVentanaListarAlumnos(fachada, new VentanaListarAlumnos());
		controladorRegistrarNota = new ControladorVentanaRegistrarNota(fachada, new VentanaRegistrarNota());
		controladorObtenerRegistro = new ControladorVentanaObtenerRegistro(fachada, new VentanaObtenerRegistro());
		controladorListarRegistros = new ControladorVentanaListarRegistros(fachada, new VentanaListarRegistros());
		controladorPromedioNotas = new ControladorVentanaPromedioNotas(fachada, new VentanaPromedioNotas());
		controladorAlumnoMayorPromedio = new ControladorVentanaAlumnoMayorPromedio(fachada, new VentanaAlumnoMayorPromedio());
		controladorContarAlumnos = new ControladorVentanaContarAlumnos(fachada, new VentanaContarAlumnos());
		controladorActividadMasCursada = new ControladorVentanaActividadMasCursada(fachada, new VentanaActividadMasCursada());
*/
		registrarListeners();
	}

	private void initCapaLogica() {
		try {
			Properties p = new Properties();
			String nomArch = "config/config.properties";
			FileInputStream fis = new FileInputStream(nomArch);
			p.load(fis);
			fis.close();
			String ip = p.getProperty("ipServidor");
			String puerto = p.getProperty("puertoServidorRMI");
			String timeout = p.getProperty("timeoutMs", "5000");
			System.setProperty("sun.rmi.transport.tcp.responseTimeout", timeout);
			System.setProperty("sun.rmi.transport.connectionTimeout", timeout);
			String ruta = "//" + ip + ":" + puerto + "/Fachada";
			//fachada = (IFachada) Naming.lookup(ruta);
		} catch (Exception e) {
			ventana.showError("No se pudo conectar al servidor remoto.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void registrarListeners() {
		ventana.addNuevoAlumnoListener(e -> controladorNuevoAlumno.abrir(ubicacionVentanas()));
		ventana.addListarAlumnosListener(e -> controladorListarAlumnos.abrir(ubicacionVentanas()));
		ventana.addRegistrarNotaListener(e -> controladorRegistrarNota.abrir(ubicacionVentanas()));
		ventana.addObtenerRegistroListener(e -> controladorObtenerRegistro.abrir(ubicacionVentanas()));
		ventana.addListarRegistrosListener(e -> controladorListarRegistros.abrir(ubicacionVentanas()));
		ventana.addPromedioNotasListener(e -> controladorPromedioNotas.abrir(ubicacionVentanas()));
		ventana.addAlumnoMayorPromedioListener(e -> controladorAlumnoMayorPromedio.abrir(ubicacionVentanas()));
		ventana.addContarAlumnosListener(e -> controladorContarAlumnos.abrir(ubicacionVentanas()));
		ventana.addActividadMasCursadaListener(e -> controladorActividadMasCursada.abrir(ubicacionVentanas()));
	}

	private Point ubicacionVentanas() {
		return new Point(ventana.getX() + ventana.getWidth() + 10, ventana.getY());
	}

	/* 
	 * public IFachada getCapaLogica() {
		return fachada;
	}
	*/
}
