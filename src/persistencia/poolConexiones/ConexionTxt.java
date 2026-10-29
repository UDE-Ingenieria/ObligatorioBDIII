package persistencia.poolConexiones;

/**
 * Conexion para la estructura de archivos de texto. No envuelve ningun recurso: solo recuerda si
 * quien la tiene es escritor o lector, para que el pool sepa que lugar liberar.
 */
public class ConexionTxt implements IConexion {

	private boolean escritor;

	public ConexionTxt(boolean escritor) {
		this.escritor = escritor;
	}

	public boolean esEscritor() {
		return escritor;
	}
}
