package grafica.controladores;

import java.rmi.RemoteException;

import grafica.ventanas.VentanaAlumnoMayorPromedio;
import logica.IFachada;
import logica.excepciones.LogicaPersistenciaException;
import logica.valueObjects.VOAlumnoProm;

public class ControladorVentanaAlumnoMayorPromedio extends ControladorVentana<VentanaAlumnoMayorPromedio> {

	public ControladorVentanaAlumnoMayorPromedio(IFachada fachada, VentanaAlumnoMayorPromedio ventanaAlumnoMayorPromedio) {
		super(fachada, ventanaAlumnoMayorPromedio);
		this.ventana.addConsultarListener(e -> alumnoMayorPromedio());
	}

	@Override
	protected void prepararApertura() {
		ventana.limpiarResultado();
	}

	public void alumnoMayorPromedio() {
		ventana.limpiarResultado();
		try {
			VOAlumnoProm alumno = fachada.alumnoMayorPromedio();
			if (alumno == null) {
				ventana.showWarning("Todavía no hay notas registradas.");
				return;
			}
			ventana.mostrarAlumno(alumno);
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (LogicaPersistenciaException ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
