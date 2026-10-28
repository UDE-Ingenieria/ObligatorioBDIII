package grafica;

import javax.swing.SwingUtilities;

import grafica.controladores.ControladorPrincipal;
import grafica.ventanas.VentanaPrincipal;

public class Aplicacion {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			VentanaPrincipal ventana = new VentanaPrincipal();
			new ControladorPrincipal(ventana);
			ventana.setVisible(true);
		});
	}

}
