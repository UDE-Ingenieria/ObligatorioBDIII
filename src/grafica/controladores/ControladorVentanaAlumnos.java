package grafica.controladores;

import java.rmi.RemoteException;
import java.util.List;

import grafica.ventanas.VentanaAlumnos;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;

public class ControladorVentanaAlumnos {

	private IFachada fachada;
	private VentanaAlumnos ventana;

	public ControladorVentanaAlumnos(IFachada fachada, VentanaAlumnos ventana) {
		this.fachada = fachada;
		this.ventana = ventana;
		this.ventana.addGuardarListener(e -> registrarNuevoAlumno(ventana.getCedula(), ventana.getNombre(), ventana.getApellido()));
		this.ventana.addActualizarListener(e -> listarAlumnos());
	}

	public void registrarNuevoAlumno(String cedula, String nombre, String apellido) {

		if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA;
		try {
			cedulaA = Integer.parseInt(cedula);
			if (cedulaA <= 0) {
				ventana.showWarning("La cédula debe de ser mayor a 0");
				return;
			}

		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula debe ser un número entero.");
			return;
		}

		VOAlumno vo = new VOAlumno(cedulaA, nombre, apellido);

		try {
			fachada.nuevoAlumno(vo);
			ventana.showSuccess("Alumno registrado exitosamente.");
			ventana.limpiarFormulario();
			listarAlumnos();
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
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
