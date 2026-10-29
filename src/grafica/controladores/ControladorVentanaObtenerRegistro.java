package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaObtenerRegistro;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VORegistro;

public class ControladorVentanaObtenerRegistro extends ControladorVentana<VentanaObtenerRegistro> {

	public ControladorVentanaObtenerRegistro(IFachada fachada, VentanaObtenerRegistro ventanaObtenerRegistro) {
		super(fachada, ventanaObtenerRegistro);
		this.ventana.addBuscarListener(e -> obtenerRegistro(ventana.getCedula(), ventana.getNumero()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
	}

	public void obtenerRegistro(String cedula, String numero) {

		if (cedula.isEmpty() || numero.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA, numeroA;
		try {
			cedulaA = Integer.parseInt(cedula);
			numeroA = Integer.parseInt(numero);
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula y el número de registro deben ser números enteros.");
			return;
		}

		ventana.limpiarResultado();
		try {
			VORegistro registro = fachada.obtenerRegistro(cedulaA, numeroA);
			if (registro == null) {
				ventana.showWarning("El alumno no tiene un registro con ese número.");
				return;
			}
			ventana.mostrarRegistro(registro);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
