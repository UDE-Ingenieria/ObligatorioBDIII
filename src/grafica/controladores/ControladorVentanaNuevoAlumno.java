package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaNuevoAlumno;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;

public class ControladorVentanaNuevoAlumno extends ControladorVentana<VentanaNuevoAlumno> {

	public ControladorVentanaNuevoAlumno(IFachada fachada, VentanaNuevoAlumno ventanaNuevoAlumno) {
		super(fachada, ventanaNuevoAlumno);
		this.ventana.addGuardarListener(e -> registrarNuevoAlumno(ventana.getCedula(), ventana.getNombre(), ventana.getApellido()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
	}

	public void registrarNuevoAlumno(String cedula, String nombre, String apellido) {

		if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA;
		try {
			cedulaA = Integer.parseInt(cedula);
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula debe ser un número entero.");
			return;
		}

		VOAlumno vo = new VOAlumno(cedulaA, nombre, apellido);

		try {
			fachada.nuevoAlumno(vo);
			ventana.showSuccess("Alumno registrado exitosamente.");
			ventana.limpiar();
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
