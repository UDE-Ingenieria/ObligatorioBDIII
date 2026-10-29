package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaActividadMasCursada;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;

public class ControladorVentanaActividadMasCursada extends ControladorVentana<VentanaActividadMasCursada> {

	public ControladorVentanaActividadMasCursada(IFachada fachada, VentanaActividadMasCursada ventanaActividadMasCursada) {
		super(fachada, ventanaActividadMasCursada);
		this.ventana.addConsultarListener(e -> actividadMasCursada(ventana.getCedula()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
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

		ventana.limpiarResultado();
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
