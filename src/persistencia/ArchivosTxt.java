package persistencia.daos;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import logica.excepciones.Persistencia;
import persistencia.poolConexiones.ConexionTxt;
import persistencia.poolConexiones.IConexion;

/**
 * Lectura y escritura de la estructura de archivos de texto plano.
 *
 * Es de uso interno de DAOAlumnosTxt y DAORegistrosTxt (como pide la letra): por eso la clase no es
 * publica y solo se puede usar desde el package persistencia.daos.
 *
 * Cada linea de un archivo es un elemento y sus campos van separados por el caracter |.
 * La carpeta donde estan los archivos se lee de config.properties (clave rutaTxt).
 */
class ArchivosTxt {

	private static final String SEPARADOR = " | ";
	private static final String CARPETA_POR_DEFECTO = "datos";

	private static final Path CARPETA = leerCarpeta();

	private ArchivosTxt() {
		// solo metodos estaticos
	}

	private static Path leerCarpeta() {
		Properties p = new Properties();
		try (FileInputStream fis = new FileInputStream("config/config.properties")) {
			p.load(fis);
		} catch (IOException e) {
			// sin configuracion se usa la carpeta por defecto
		}
		String ruta = p.getProperty("rutaTxt");
		if (ruta == null || ruta.trim().isEmpty())
			ruta = CARPETA_POR_DEFECTO;
		return Paths.get(ruta.trim());
	}

	static Path archivoAlumnos() {
		return CARPETA.resolve("alumnos.txt");
	}

	static Path archivoRegistros(int cedula) {
		return CARPETA.resolve("registros" + cedula + ".txt");
	}

	// ---------------------------------------------------------------- conexion

	// Toda operacion necesita una conexion del pool de archivos (es lo que garantiza el monitor)
	static void exigirConexion(IConexion icon) throws Persistencia {
		if (!(icon instanceof ConexionTxt))
			throw new Persistencia("No hay una conexión válida a los archivos");
	}

	// Las operaciones que modifican archivos solo se permiten con una conexion pedida como escritor
	static void exigirEscritura(IConexion icon) throws Persistencia {
		exigirConexion(icon);
		if (!((ConexionTxt) icon).esEscritor())
			throw new Persistencia("Se intentó modificar los archivos sin acceso de escritura");
	}

	// ---------------------------------------------------------------- lectura

	/**
	 * Devuelve los campos (ya sin espacios alrededor) de cada linea no vacia del archivo.
	 * Si el archivo todavia no existe, se considera vacio.
	 */
	static List<String[]> leer(Path archivo, int cantCampos) throws Persistencia {
		List<String[]> filas = new ArrayList<>();
		if (!Files.exists(archivo))
			return filas;

		List<String> lineas;
		try {
			lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new Persistencia("Error al leer el archivo " + archivo.getFileName());
		}

		for (int i = 0; i < lineas.size(); i++) {
			String linea = lineas.get(i);
			if (i == 0 && linea.startsWith("﻿"))		// BOM que agregan algunos editores al guardar en UTF-8
				linea = linea.substring(1);
			if (linea.trim().isEmpty())
				continue;
			String[] campos = linea.split("\\|", -1);
			if (campos.length != cantCampos)
				throw new Persistencia("Formato inválido en la línea " + (i + 1) + " de " + archivo.getFileName());
			for (int j = 0; j < campos.length; j++)
				campos[j] = campos[j].trim();
			filas.add(campos);
		}
		return filas;
	}

	static int entero(String valor, Path archivo) throws Persistencia {
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			throw new Persistencia("Valor numérico inválido (\"" + valor + "\") en " + archivo.getFileName());
		}
	}

	// ---------------------------------------------------------------- escritura

	static String linea(Object... campos) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < campos.length; i++) {
			if (i > 0)
				sb.append(SEPARADOR);
			sb.append(campos[i]);
		}
		return sb.toString();
	}

	// Un texto con | o con saltos de linea romperia el formato del archivo
	static void validarTexto(String valor, String campo) throws Persistencia {
		if (valor == null || valor.trim().isEmpty())
			throw new Persistencia("El campo " + campo + " no puede ser vacío");
		if (valor.indexOf('|') >= 0 || valor.indexOf('\n') >= 0 || valor.indexOf('\r') >= 0)
			throw new Persistencia("El campo " + campo + " no puede contener el carácter | ni saltos de línea");
	}

	/**
	 * Reescribe el archivo completo con las lineas recibidas. Primero escribe un archivo temporal y despues lo
	 * renombra, asi el archivo original nunca queda escrito a medias si algo falla en el medio.
	 */
	static void escribir(Path archivo, List<String> lineas) throws Persistencia {
		Path temporal = archivo.resolveSibling(archivo.getFileName() + ".tmp");
		try {
			Files.createDirectories(CARPETA);
			Files.write(temporal, lineas, StandardCharsets.UTF_8);
			try {
				Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			try {
				Files.deleteIfExists(temporal);
			} catch (IOException e2) {
				// no se pudo limpiar el temporal: no afecta los datos
			}
			throw new Persistencia("Error al escribir el archivo " + archivo.getFileName());
		}
	}

	static void crearVacioSiNoExiste(Path archivo) throws Persistencia {
		try {
			Files.createDirectories(CARPETA);
			if (!Files.exists(archivo))
				Files.createFile(archivo);
		} catch (IOException e) {
			throw new Persistencia("Error al crear el archivo " + archivo.getFileName());
		}
	}
}
