package logica;

public class Registro {

	private int numero, nota;
	private String actividad;

	public Registro(int numero, int nota, String actividad) {
		this.numero = numero;
		this.nota = nota;
		this.actividad = actividad;
	}

	public int getNumero() {
		return numero;
	}

	public int getNota() {
		return nota;
	}

	public String getActividad() {
		return actividad;
	}

}
