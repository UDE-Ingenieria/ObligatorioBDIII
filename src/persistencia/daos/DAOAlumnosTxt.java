package persistencia.daos;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fabricas.FabricaAbstracta;
import logica.Alumno;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import logica.valueObjects.VORegistroListado;
import persistencia.poolConexiones.IConexion;

/**
 * DAO de alumnos sobre la estructura de archivos de texto.
 *
 * Los alumnos se guardan en alumnos.txt, uno por linea (cedula | nombre | apellido), ordenados por cedula.
 * Tiene los mismos cabezales que DAOAlumnos; solo cambia el medio donde persiste.
 *
 * La conexion no se usa para leer ni escribir (los archivos se abren en cada operacion), pero se exige igual:
 * es la prueba de que la fachada paso por el monitor de lectura y escritura de PoolConexionesTxt.
 */
public class DAOAlumnosTxt implements IDAOAlumnos {

	private FabricaAbstracta fabrica;

	// Igual que DAOAlumnos: recibe la fabrica para crear los alumnos (cada alumno le pide su DAO de registros)
	public DAOAlumnosTxt(FabricaAbstracta fabrica) {
		this.fabrica = fabrica;
	}

	public boolean member(IConexion icon, int cedA) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		return buscar(leerAlumnos(), cedA) != null;
	}

	public void insert(IConexion icon, Alumno alu) throws Persistencia {
		ArchivosTxt.exigirEscritura(icon);
		ArchivosTxt.validarTexto(alu.getNombre(), "nombre");
		ArchivosTxt.validarTexto(alu.getApellido(), "apellido");

		List<VOAlumno> alumnos = leerAlumnos();
		if (buscar(alumnos, alu.getCedula()) != null)
			throw new Persistencia("Error al insertar el alumno: la cédula ya existe");

		alumnos.add(new VOAlumno(alu.getCedula(), alu.getNombre().trim(), alu.getApellido().trim()));
		alumnos.sort(Comparator.comparingInt(VOAlumno::getCedula));
		escribirAlumnos(alumnos);

		// Cada alumno tiene su archivo de registros, aunque todavia no tenga notas
		ArchivosTxt.crearVacioSiNoExiste(ArchivosTxt.archivoRegistros(alu.getCedula()));
	}

	public Alumno find(IConexion icon, int cedA) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		VOAlumno vo = buscar(leerAlumnos(), cedA);
		if (vo == null)
			return null;
		return new Alumno(vo.getCedula(), vo.getNombre(), vo.getApellido(), fabrica);
	}

	public boolean esVacio(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		return leerAlumnos().isEmpty();
	}

	public List<VOAlumno> listarAlumnos(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		return leerAlumnos();
	}

	/**
	 * Mismo criterio que la consulta SQL (LEFT JOIN + ORDER BY promedio DESC): gana el mayor promedio entre los
	 * alumnos que tienen notas; los que no tienen notas quedan al final. Si nadie tiene notas, devuelve el primer
	 * alumno con promedio 0. Devuelve null si no hay alumnos (la fachada lo controla antes con esVacio).
	 *
	 * Las notas se obtienen a traves del DAO de registros que da la fabrica: este DAO no conoce el formato de
	 * los archivos de registros.
	 */
	public VOAlumnoProm alumnoMayorPromedio(IConexion icon) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		VOAlumnoProm mejor = null;
		boolean mejorTieneNotas = false;

		for (VOAlumno a : leerAlumnos()) {
			IDAORegistros registros = fabrica.crearDAORegistros(a.getCedula());
			boolean tieneNotas = registros.largo(icon) > 0;
			double prom = registros.promedioNotas(icon);

			if (mejor == null || (tieneNotas && (!mejorTieneNotas || prom > mejor.getPromedio()))) {
				mejor = new VOAlumnoProm(a.getCedula(), a.getNombre(), a.getApellido(), prom);
				mejorTieneNotas = tieneNotas;
			}
		}
		return mejor;
	}

	/**
	 * Cantidad de alumnos que tienen al menos un registro con esa actividad y esa nota (como el COUNT DISTINCT
	 * de la consulta SQL: un alumno que repitio la actividad con la misma nota cuenta una sola vez).
	 * La actividad se compara sin distinguir mayusculas, igual que MySQL con su collation por defecto.
	 */
	public int contarAlumnos(IConexion icon, String act, int nota) throws Persistencia {
		ArchivosTxt.exigirConexion(icon);
		String buscada = (act == null) ? "" : act.trim();
		int cant = 0;

		for (VOAlumno a : leerAlumnos()) {
			IDAORegistros registros = fabrica.crearDAORegistros(a.getCedula());
			boolean encontrado = false;
			for (VORegistroListado r : registros.listarRegistros(icon)) {
				if (r.getNota() == nota && r.getActividad().equalsIgnoreCase(buscada)) {
					encontrado = true;
					break;
				}
			}
			if (encontrado)
				cant++;
		}
		return cant;
	}

	// ---------------------------------------------------------------- auxiliares internos

	// Lee alumnos.txt y devuelve los alumnos ordenados por cedula
	private List<VOAlumno> leerAlumnos() throws Persistencia {
		Path archivo = ArchivosTxt.archivoAlumnos();
		List<VOAlumno> alumnos = new ArrayList<>();
		for (String[] campos : ArchivosTxt.leer(archivo, 3)) {
			int ced = ArchivosTxt.entero(campos[0], archivo);
			alumnos.add(new VOAlumno(ced, campos[1], campos[2]));
		}
		alumnos.sort(Comparator.comparingInt(VOAlumno::getCedula));
		return alumnos;
	}

	private void escribirAlumnos(List<VOAlumno> alumnos) throws Persistencia {
		List<String> lineas = new ArrayList<>();
		for (VOAlumno a : alumnos)
			lineas.add(ArchivosTxt.linea(a.getCedula(), a.getNombre(), a.getApellido()));
		ArchivosTxt.escribir(ArchivosTxt.archivoAlumnos(), lineas);
	}

	private VOAlumno buscar(List<VOAlumno> alumnos, int cedA) {
		for (VOAlumno a : alumnos)
			if (a.getCedula() == cedA)
				return a;
		return null;
	}
}
