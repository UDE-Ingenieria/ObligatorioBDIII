package persistencia.poolConexiones;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import logica.excepciones.Persistencia;

public class PoolConexiones implements IPoolConexiones {

	private static final int TOPE_POR_DEFECTO = 5;

	private String driver, url, user, password;
	private int nivelTransaccionalidad;
	private Conexion[] conexiones;
	private int tamanio, creadas, tope;

	public PoolConexiones() {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream("config/config.properties");
			p.load(fis);
			fis.close();
		} catch (IOException e) {
			System.err.println("No se pudo leer config.properties: " + e.getMessage());
		}

		driver = p.getProperty("driver");
		url = p.getProperty("url");
		user = p.getProperty("usr");
		password = p.getProperty("pwd");
		nivelTransaccionalidad = leerNivel(p.getProperty("nivelTransaccionalidad"));
		tope = leerTope(p.getProperty("tope"));

		conexiones = new Conexion[tope];
		tamanio = 0;
		creadas = 0;

		if (driver != null) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				System.err.println("No se pudo cargar el driver " + driver);
			}
		}
	}

	private int leerTope(String valor) {
		try {
			int t = Integer.parseInt(valor.trim());
			if (t >= 1)
				return t;
		} catch (RuntimeException e) {
			// null o texto invalido: se usa el valor por defecto
		}
		return TOPE_POR_DEFECTO;
	}

	private int leerNivel(String valor) {
		try {
			int n = Integer.parseInt(valor.trim());
			if (n == Connection.TRANSACTION_READ_UNCOMMITTED || n == Connection.TRANSACTION_READ_COMMITTED
					|| n == Connection.TRANSACTION_REPEATABLE_READ || n == Connection.TRANSACTION_SERIALIZABLE)
				return n;
		} catch (RuntimeException e) {
			// null o texto invalido: se usa el valor por defecto
		}
		return Connection.TRANSACTION_REPEATABLE_READ;
	}

	public synchronized IConexion obtenerConexion(boolean modifica) throws Persistencia {
		if (url == null || user == null || password == null)
			throw new Persistencia("No se pudo leer la configuración de la base de datos");

		// Si estan todas en uso y no se pueden crear mas, espera a que alguien libere una
		while (tamanio == 0 && creadas == tope) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new Persistencia("Se interrumpió la espera por una conexión");
			}
		}

		// Busca entre las libres una que siga viva. Las que se cayeron mientras estaban guardadas se descartan.
		Conexion con = null;
		while (con == null && tamanio > 0) {
			tamanio--;
			con = conexiones[tamanio];
			conexiones[tamanio] = null;
			if (!estaViva(con)) {
				cerrar(con);
				creadas--;
				con = null;
			}
		}

		// Solo se crea una nueva si no habia ninguna libre (a demanda)
		if (con == null) {
			try {
				Connection c = DriverManager.getConnection(url, user, password);
				c.setTransactionIsolation(nivelTransaccionalidad);
				con = new Conexion(c);
				creadas++;
			} catch (SQLException e) {
				// puede haber quedado un lugar libre para otro que este esperando
				notify();
				throw new Persistencia("No se pudo conectar con la base de datos");
			}
		}

		try {
			con.getConnection().setAutoCommit(!modifica);
		} catch (SQLException e) {
			cerrar(con);
			creadas--;
			notify();
			throw new Persistencia("No se pudo conectar con la base de datos");
		}
		return con;
	}

	public synchronized void liberarConexion(IConexion icon, boolean ok) throws Persistencia{
		if (icon == null)
			return;
		Conexion con = (Conexion) icon;
		boolean fallo = false;
		try {
			Connection c = con.getConnection();
			if (!c.getAutoCommit()) {
				if (ok)
					c.commit();
				else
					c.rollback();
				c.setAutoCommit(true);
			}
		} catch (SQLException e) {
			fallo = true;
		}

		if (fallo) {			// la conexion quedo en un estado dudoso: no se reutiliza
			cerrar(con);
			creadas--;
		} else {
			conexiones[tamanio] = con;
			tamanio++;
		}
		notify();

		if (fallo && ok)
			throw new Persistencia("No se pudo confirmar la operación en la base de datos");
	}

	private boolean estaViva(Conexion con) {
		try {
			return con.getConnection().isValid(2);
		} catch (SQLException e) {
			return false;
		}
	}

	private void cerrar(Conexion con) {
		try {
			con.getConnection().close();
		} catch (SQLException e) {
			// ya estaba caida
		}
	}

}
