import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Crea la base de datos del sistema de registro de notas (Practico 4, ejercicio 2) y carga los alumnos por
 * defecto. Se puede ejecutar mas de una vez: lo que ya existe no se modifica.
 * Ademas deja lista la carpeta de archivos del segundo medio de persistencia (alumnos.txt con los mismos
 * alumnos), si todavia no existe.
 */
public class Main {

	private static final String[][] ALUMNOS = {
			{ "12345678", "Matilda", "Wormwood" },
			{ "25554449", "Charlie", "Brown" },
			{ "39876542", "Dora", "Márquez" },
			{ "51112223", "Daniel", "Mitchell" },
			{ "66666666", "Merlina", "Addams" } };

	public static void main(String[] args) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream("config/config.properties");
			p.load(fis);
			fis.close();
		} catch (IOException e) {
			System.err.println("No se pudo leer config.properties: " + e.getMessage());
			return;
		}

		crearBaseDeDatos(p);
	}

	private static void crearBaseDeDatos(Properties p) {
		String ip = p.getProperty("ipServidor");
		String puerto = p.getProperty("puertoServidor");
		String bd = p.getProperty("bd");
		String usr = p.getProperty("usr");
		String pwd = p.getProperty("pwd");

		// Se conecta al servidor sin indicar la base de datos, porque todavia puede no existir
		String url = "jdbc:mysql://" + ip + ":" + puerto + "/";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, usr, pwd);
			Statement st = con.createStatement();
			st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + bd);
			st.executeUpdate("CREATE TABLE IF NOT EXISTS " + bd + ".Alumnos ("
					+ "cedula INT PRIMARY KEY, "
					+ "nombre VARCHAR(45), "
					+ "apellido VARCHAR(45))");
			st.executeUpdate("CREATE TABLE IF NOT EXISTS " + bd + ".Registros ("
					+ "numero INT, "
					+ "cedAlu INT, "
					+ "actividad VARCHAR(45), "
					+ "nota INT, "
					+ "PRIMARY KEY (numero, cedAlu), "
					+ "FOREIGN KEY (cedAlu) REFERENCES " + bd + ".Alumnos(cedula))");

			PreparedStatement ps = con.prepareStatement("INSERT IGNORE INTO " + bd + ".Alumnos VALUES (?, ?, ?)");
			for (String[] a : ALUMNOS) {
				ps.setInt(1, Integer.parseInt(a[0]));
				ps.setString(2, a[1]);
				ps.setString(3, a[2]);
				ps.executeUpdate();
			}
			ps.close();
			st.close();
			con.close();
			System.out.println("Base de datos " + bd + " lista");
		} catch (SQLException e1) {
			System.err.println("No se pudo crear la base de datos: " + e1.getMessage());
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				System.err.println("Se nos rompio el acceso a BD");
			}
		}
	}

}
