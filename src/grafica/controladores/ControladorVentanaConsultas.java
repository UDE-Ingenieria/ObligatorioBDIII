package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaConsultas;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VOAlumnoProm;

public class ControladorVentanaConsultas {

	private IFachada fachada;
	private VentanaConsultas ventana;

	public ControladorVentanaConsultas(IFachada fachada, VentanaConsultas ventana) {
		this.fachada = fachada;
		this.ventana = ventana;
		this.ventana.addCalcularPromedioListener(e -> promedioNotas(ventana.getPromedioCedula()));
		this.ventana.addConsultarMayorPromedioListener(e -> alumnoMayorPromedio());
		this.ventana.addContarListener(e -> contarAlumnos(ventana.getContarActividad(), ventana.getContarNota()));
		this.ventana.addConsultarActividadMasCursadaListener(e -> actividadMasCursada(ventana.getCursadaCedula()));
	}

	public void promedioNotas(String cedula) {

		if (cedula.isEmpty()) {
			ventana.showWarning("Debe ingresar la cédula del alumno.");
			return;
		}

		int cedulaA;
		try {
			cedulaA = Integer.parseInt(cedula);
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula debe ser un número entero.");
			return;
		}

		ventana.limpiarResultadoPromedio();
		try {
			double promedio = fachada.promedioNotas(cedulaA);
			ventana.mostrarPromedio(promedio);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}

	public void alumnoMayorPromedio() {
		ventana.limpiarResultadoMayorPromedio();
		try {
			VOAlumnoProm alumno = fachada.alumnoMayorPromedio();
			if (alumno == null) {
				ventana.showWarning("Todavía no hay notas registradas.");
				return;
			}
			ventana.mostrarAlumnoMayorPromedio(alumno);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}

	public void contarAlumnos(String actividad, String nota) {

		if (actividad.isEmpty() || nota.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int notaA;
		try {
			notaA = Integer.parseInt(nota);
		} catch(NumberFormatException e) {
			ventana.showWarning("La nota debe ser un número entero.");
			return;
		}

		ventana.limpiarResultadoContar();
		try {
			int cantidad = fachada.contarAlumnos(actividad, notaA);
			ventana.mostrarCantidad(cantidad);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}

	public void actividadMasCursada(String cedula) {

		if (cedula.isEmpty()) {
			ventana.showWarning("Debe ingresar la cédula del alumno.");
			return;
		}

		int cedulaA;
		try {
			cedulaA = Integer.parseInt(cedula);
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula debe ser un número entero.");
			return;
		}

		ventana.limpiarResultadoActividadMasCursada();
		try {
			VOActividad actividad = fachada.actividadMasCursada(cedulaA);
			ventana.mostrarActividad(actividad);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
