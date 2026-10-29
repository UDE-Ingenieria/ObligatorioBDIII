package persistencia.poolConexiones;

import logica.excepciones.Persistencia;

/**
 * Pool para la estructura de archivos de texto: monitor de lectura y escritura.
 * Los requerimientos que modifican archivos (modifica = true) son escritores y entran de a uno, sin lectores.
 * Los que no modifican son lectores y pueden entrar varios a la vez. Si hay un escritor esperando, los lectores
 * nuevos esperan detras de el, para que los escritores no queden postergados indefinidamente.
 */
public class PoolConexionesTxt implements IPoolConexiones {

	private int lectores;				// lectores leyendo actualmente
	private boolean escribiendo;		// hay un escritor trabajando
	private int escritoresEsperando;

	public PoolConexionesTxt() {
		lectores = 0;
		escribiendo = false;
		escritoresEsperando = 0;
	}

	public synchronized IConexion obtenerConexion(boolean modifica) throws Persistencia {
		if (modifica) {
			escritoresEsperando++;
			try {
				while (escribiendo || lectores > 0)
					wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new Persistencia("Se interrumpió la espera por el acceso a los archivos");
			} finally {
				escritoresEsperando--;
				// Si el escritor se fue sin entrar, los lectores que esperaban detras de el pueden seguir
				notifyAll();
			}
			escribiendo = true;
		} else {
			try {
				while (escribiendo || escritoresEsperando > 0)
					wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new Persistencia("Se interrumpió la espera por el acceso a los archivos");
			}
			lectores++;
		}
		return new ConexionTxt(modifica);
	}

	// Los archivos no tienen transacciones: ok no cambia nada, solo se libera el lugar de lector o de escritor
	public synchronized void liberarConexion(IConexion icon, boolean ok) throws Persistencia {
		if (icon == null)
			return;
		if (((ConexionTxt) icon).esEscritor())
			escribiendo = false;
		else
			lectores--;
		notifyAll();
	}

}
