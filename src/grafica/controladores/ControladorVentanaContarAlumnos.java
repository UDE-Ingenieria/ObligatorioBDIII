package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaContarAlumnos;
import logica.IFachada;
import logica.excepciones.Persistencia;

public class ControladorVentanaContarAlumnos extends ControladorVentana<VentanaContarAlumnos> {

	public ControladorVentanaContarAlumnos(IFachada fachada, VentanaContarAlumnos ventanaContarAlumnos) {
		super(fachada, ventanaContarAlumnos);
		this.ventana.addContarListener(e -> contarAlumnos(ventana.getActividad(), ventana.getNota()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
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

		ventana.limpiarResultado();
		try {
			int cantidad = fachada.contarAlumnos(actividad, notaA);
			ventana.mostrarCantidad(cantidad);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
