package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaRegistrarNota;
import logica.IFachada;
import logica.excepciones.LogicaPersistenciaException;
import logica.valueObjects.VORegistro;

public class ControladorVentanaRegistrarNota extends ControladorVentana<VentanaRegistrarNota> {

	public ControladorVentanaRegistrarNota(IFachada fachada, VentanaRegistrarNota ventanaRegistrarNota) {
		super(fachada, ventanaRegistrarNota);
		this.ventana.addRegistrarListener(e -> registrarNota(ventana.getCedula(), ventana.getActividad(), ventana.getNota()));
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiar();
	}

	public void registrarNota(String cedula, String actividad, String nota) {

		if (cedula.isEmpty() || actividad.isEmpty() || nota.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA, notaA;
		try {
			cedulaA = Integer.parseInt(cedula);
			notaA = Integer.parseInt(nota);
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula y la nota deben ser números enteros.");
			return;
		}

		VORegistro vo = new VORegistro(actividad, notaA);

		try {
			fachada.registrarNota(cedulaA, vo);
			ventana.showSuccess("Nota registrada exitosamente.");
			ventana.limpiar();
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (LogicaPersistenciaException ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
