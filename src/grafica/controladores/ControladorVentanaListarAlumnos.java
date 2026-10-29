package grafica.controladores;

import java.rmi.RemoteException;
import java.util.List;

import grafica.ventanas.VentanaListarAlumnos;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;

public class ControladorVentanaListarAlumnos extends ControladorVentana<VentanaListarAlumnos> {

	public ControladorVentanaListarAlumnos(IFachada fachada, VentanaListarAlumnos ventanaListarAlumnos) {
		super(fachada, ventanaListarAlumnos);
		this.ventana.addActualizarListener(e -> listarAlumnos());
	}

	@Override
	protected void prepararApertura() {
		listarAlumnos();
	}

	public void listarAlumnos() {
		try {
			List<VOAlumno> alumnos = fachada.listarAlumnos();
			ventana.mostrarAlumnos(alumnos);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
