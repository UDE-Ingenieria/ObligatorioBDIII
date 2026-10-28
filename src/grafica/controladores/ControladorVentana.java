package grafica.controladores;

import java.awt.Point;

import grafica.ventanas.VentanaBase;
import logica.IFachada;

public abstract class ControladorVentana<V extends VentanaBase> {

	protected IFachada fachada;
	protected V ventana;

	protected ControladorVentana(IFachada fachada, V ventana) {
		this.fachada = fachada;
		this.ventana = ventana;
	}

	public void abrir(Point ubicacion) {
		if (ventana.isShowing()) {
			ventana.toFront();
			return;
		}
		prepararApertura();
		ventana.setLocation(ubicacion);
		ventana.setVisible(true);
	}

	// Deja la ventana lista antes de mostrarla (limpiar campos, cargar datos, etc.)
	protected abstract void prepararApertura();

}
