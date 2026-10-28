package grafica.ventanas;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaPromedioNotas extends VentanaBase {

	private static final long serialVersionUID = 1L;

	private JTextField txtCedula = new JTextField(15);
	private JTextField txtPromedio = crearCampoResultado();
	private JButton btnCalcular = new JButton("Calcular");

	public VentanaPromedioNotas() {
		super("Promedio de notas", 380, 220);

		JPanel entrada = crearFormulario();
		agregarCampo(entrada, 0, "Cédula del alumno:", txtCedula);

		JPanel resultado = crearFormularioResultado();
		agregarCampo(resultado, 0, "Promedio:", txtPromedio);

		add(crearPanelConsulta(entrada, resultado), BorderLayout.CENTER);
		add(crearBotonera(btnCalcular, crearBotonCerrar()), BorderLayout.SOUTH);
	}

	public String getCedula() {
		return txtCedula.getText().trim();
	}

	public void mostrarPromedio(double promedio) {
		txtPromedio.setText(String.format("%.2f", promedio));
	}

	public void limpiarResultado() {
		txtPromedio.setText("");
	}

	public void limpiar() {
		txtCedula.setText("");
		limpiarResultado();
		txtCedula.requestFocus();
	}

	public void addCalcularListener(ActionListener l) {
		btnCalcular.addActionListener(l);
	}

}
