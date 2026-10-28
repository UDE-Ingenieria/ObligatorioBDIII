package logica.valueObjects;

public class VORegistroListado extends VORegistro {

	private int numero;

	public VORegistroListado(int numero, String actividad, int nota) {
		super(actividad, nota);
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}
}
