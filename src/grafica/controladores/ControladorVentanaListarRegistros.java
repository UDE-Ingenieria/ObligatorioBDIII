package grafica.controladores;

import java.rmi.RemoteException;
import java.util.List;

import grafica.ventanas.VentanaListarRegistros;
import logica.IFachada;
import logica.excepciones.LogicaPersistenciaException;
import logica.valueObjects.VORegistroListado;

public class ControladorVentanaListarRegistros extends ControladorVentana<VentanaListarRegistros> {

	public ControladorVentanaListarRegistros(IFachada fachada, VentanaListarRegistros ventanaListarRegistros) {
		super(fachada, ventanaListarRegistros);
		this.ventana.addListarListener(e -> listarRegistros(ventana.getCedula()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
	}

	public void listarRegistros(String cedula) {

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

		try {
			List<VORegistroListado> registros = fachada.listarRegistros(cedulaA);
			ventana.mostrarRegistros(registros);
			if (registros.isEmpty()) {
				ventana.showWarning("El alumno no tiene registros.");
			}
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (LogicaPersistenciaException ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
