package persistencia.daos;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import logica.Registro;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VORegistroListado;
import persistencia.poolConexiones.IConexion;

/**
 * DAO de registros (secuencia de notas de un alumno) sobre la estructura de archivos de texto.
 *
 * Los registros de cada alumno se guardan en registrosCCCCCCCC.txt (CCCCCCCC = cedula), uno por linea
 * (numero | actividad | nota), ordenados por numero. Si el archivo no existe, el alumno no tiene registros.
 * Tiene los mismos cabezales que DAORegistros; solo cambia el medio donde persiste.
 */
public class DAORegistrosTxt implements IDAORegistros {

	private int cedAlumno;

	public DAORegistrosTxt(int ced) {
		this.cedAlumno = ced;
	}

	public int largo(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		return leerRegistros().size();
	}

	public void insback(IConexion icon, Registro reg) throws Persistencia {
		ArchivosTxt.exigirEscritura(icon);
		ArchivosTxt.validarTexto(reg.getActividad(), "actividad");

		List<Registro> registros = leerRegistros();
		if (buscar(registros, reg.getNumero()) != null)
			throw new Persistencia("Error al agregar el registro: el número ya existe");

		registros.add(new Registro(reg.getNumero(), reg.getNota(), reg.getActividad().trim()));
		registros.sort(Comparator.comparingInt(Registro::getNumero));
		escribirRegistros(registros);
	}

	// Devuelve null si no hay un registro con ese numero (igual que DAORegistros)
	public Registro kesimo(IConexion icon, int numR) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		return buscar(leerRegistros(), numR);
	}

	public List<VORegistroListado> listarRegistros(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		List<VORegistroListado> lista = new ArrayList<>();
		for (Registro r : leerRegistros())
			lista.add(new VORegistroListado(r.getNumero(), r.getActividad(), r.getNota()));
		return lista;
	}

	// Sin registros devuelve 0, igual que AVG(nota) leido con getDouble en DAORegistros
	public double promedioNotas(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		List<Registro> registros = leerRegistros();
		if (registros.isEmpty())
			return 0.0;
		int suma = 0;
		for (Registro r : registros)
			suma += r.getNota();
		return (double) suma / registros.size();
	}

	/**
	 * Actividad que aparece mas veces entre los registros del alumno. Agrupa sin distinguir mayusculas, como el
	 * GROUP BY de MySQL; en caso de empate gana la que aparecio primero. Devuelve null si no hay registros
	 * (la fachada lo controla antes con cantidadRegistros).
	 */
	public VOActividad actividadMasCursada(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		Map<String, Integer> veces = new LinkedHashMap<>();		// clave en minusculas -> cantidad
		Map<String, String> nombres = new LinkedHashMap<>();	// clave en minusculas -> nombre tal como se ingreso

		for (Registro r : leerRegistros()) {
			String clave = r.getActividad().toLowerCase(Locale.ROOT);
			Integer cant = veces.get(clave);
			veces.put(clave, (cant == null) ? 1 : cant + 1);
			if (!nombres.containsKey(clave))
				nombres.put(clave, r.getActividad());
		}

		VOActividad masCursada = null;
		for (Map.Entry<String, Integer> e : veces.entrySet())
			if (masCursada == null || e.getValue() > masCursada.getVeces())
				masCursada = new VOActividad(nombres.get(e.getKey()), e.getValue());
		return masCursada;
	}

	// ---------------------------------------------------------------- auxiliares internos

	// Lee el archivo de registros del alumno y los devuelve ordenados por numero
	private List<Registro> leerRegistros() throws Persistencia {
		Path archivo = ArchivosTxt.archivoRegistros(cedAlumno);
		List<Registro> registros = new ArrayList<>();
		for (String[] campos : ArchivosTxt.leer(archivo, 3)) {
			int numero = ArchivosTxt.entero(campos[0], archivo);
			int nota = ArchivosTxt.entero(campos[2], archivo);
			registros.add(new Registro(numero, nota, campos[1]));
		}
		registros.sort(Comparator.comparingInt(Registro::getNumero));
		return registros;
	}

	private void escribirRegistros(List<Registro> registros) throws Persistencia {
		List<String> lineas = new ArrayList<>();
		for (Registro r : registros)
			lineas.add(ArchivosTxt.linea(r.getNumero(), r.getActividad(), r.getNota()));
		ArchivosTxt.escribir(ArchivosTxt.archivoRegistros(cedAlumno), lineas);
	}

	private Registro buscar(List<Registro> registros, int numero) {
		for (Registro r : registros)
			if (r.getNumero() == numero)
				return r;
		return null;
	}
}
