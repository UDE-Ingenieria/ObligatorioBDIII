package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaPromedioNotas;
import logica.IFachada;
import logica.excepciones.LogicaPersistenciaException;

public class ControladorVentanaPromedioNotas extends ControladorVentana<VentanaPromedioNotas> {

	public ControladorVentanaPromedioNotas(IFachada fachada, VentanaPromedioNotas ventanaPromedioNotas) {
		super(fachada, ventanaPromedioNotas);
		this.ventana.addCalcularListener(e -> promedioNotas(ventana.getCedula()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
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

		ventana.limpiarResultado();
		try {
			double promedio = fachada.promedioNotas(cedulaA);
			ventana.mostrarPromedio(promedio);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (LogicaPersistenciaException ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
