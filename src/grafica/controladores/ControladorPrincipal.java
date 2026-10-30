package grafica.controladores;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;

import grafica.ventanas.VentanaAlumnos;
import grafica.ventanas.VentanaConsultas;
import grafica.ventanas.VentanaPrincipal;
import grafica.ventanas.VentanaRegistros;
import logica.IFachada;

public class ControladorPrincipal {

	private IFachada fachada;
	private VentanaPrincipal ventana;

	private ControladorVentanaAlumnos controladorAlumnos;
	private ControladorVentanaRegistros controladorRegistros;
	private ControladorVentanaConsultas controladorConsultas;

	public ControladorPrincipal(VentanaPrincipal ventana) {
		this.ventana = ventana;
		initCapaLogica();

		VentanaAlumnos ventanaAlumnos = new VentanaAlumnos();
		controladorAlumnos = new ControladorVentanaAlumnos(fachada, ventanaAlumnos);
		ventana.agregarPestania("Alumnos", "Alta y listado de alumnos", ventanaAlumnos);

		VentanaRegistros ventanaRegistros = new VentanaRegistros();
		controladorRegistros = new ControladorVentanaRegistros(fachada, ventanaRegistros);
		ventana.agregarPestania("Registros", "Notas registradas por alumno", ventanaRegistros);

		VentanaConsultas ventanaConsultas = new VentanaConsultas();
		controladorConsultas = new ControladorVentanaConsultas(fachada, ventanaConsultas);
		ventana.agregarPestania("Consultas", "Promedios y estadísticas", ventanaConsultas);

		controladorAlumnos.listarAlumnos();
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
			fachada = (IFachada) Naming.lookup(ruta);
		} catch (Exception e) {
			ventana.showError("No se pudo conectar al servidor remoto.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public IFachada getCapaLogica() {
		return fachada;
	}
}
