package grafica.controladores;

import java.rmi.RemoteException;
import java.util.List;

import grafica.ventanas.VentanaRegistros;
import logica.IFachada;
import logica.excepciones.Persistencia;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;

public class ControladorVentanaRegistros {

	private IFachada fachada;
	private VentanaRegistros ventana;

	public ControladorVentanaRegistros(IFachada fachada, VentanaRegistros ventana) {
		this.fachada = fachada;
		this.ventana = ventana;
		this.ventana.addRegistrarListener(e -> registrarNota(ventana.getNotaCedula(), ventana.getNotaActividad(), ventana.getNotaNota()));
		this.ventana.addBuscarListener(e -> obtenerRegistro(ventana.getBuscarCedula(), ventana.getBuscarNumero()));
		this.ventana.addListarListener(e -> listarRegistros(ventana.getListarCedula()));
	}

	public void registrarNota(String cedula, String actividad, String nota) {

		if (cedula.isEmpty() || actividad.isEmpty() || nota.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA, notaA;
		try {
			cedulaA = Integer.parseInt(cedula);
			if (cedulaA <= 0) {
				ventana.showWarning("La cédula debe de ser mayor a 0");
				return;
			}
			notaA = Integer.parseInt(nota);
			if (notaA < 0) {
				ventana.showWarning("La nota debe de ser mayor o igual a 0");
				return;
			}
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula y la nota deben ser números enteros.");
			return;
		}

		VORegistro vo = new VORegistro(actividad, notaA);

		try {
			fachada.registrarNota(cedulaA, vo);
			ventana.showSuccess("Nota registrada exitosamente.");
			ventana.limpiarRegistrarNota();
		} catch (RemoteException ex) {
			ventana.showError("Error de conexión con el servidor.");
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}

	public void obtenerRegistro(String cedula, String numero) {

		if (cedula.isEmpty() || numero.isEmpty()) {
			ventana.showWarning("Todos los campos obligatorios deben estar completos.");
			return;
		}

		int cedulaA, numeroA;
		try {
			cedulaA = Integer.parseInt(cedula);
			if (cedulaA <= 0) {
				ventana.showWarning("La cédula debe de ser mayor a 0");
				return;
			}
			numeroA = Integer.parseInt(numero);
			if (numeroA < 0) {
				ventana.showWarning("El numero debe de ser mayor a 0");
				return;
			}
		} catch(NumberFormatException e) {
			ventana.showWarning("La cédula y el número de registro deben ser números enteros.");
			return;
		}

		ventana.limpiarResultadoRegistro();
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
		} catch (Persistencia ex) {
			ventana.showError(ex.getMessage());
		}
	}
}
