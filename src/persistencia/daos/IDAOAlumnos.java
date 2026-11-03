package persistencia.daos;

import java.util.List;

import logica.Alumno;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import persistencia.poolConexiones.IConexion;

public interface IDAOAlumnos {

	boolean member(IConexion icon, int cedA) throws Persistencia;

	void insert(IConexion icon, Alumno alu) throws Persistencia;

	Alumno find(IConexion icon, int cedA) throws Persistencia;

	boolean esVacio(IConexion icon) throws Persistencia;

	List<VOAlumno> listarAlumnos(IConexion icon) throws Persistencia;

	VOAlumnoProm alumnoMayorPromedio(IConexion icon) throws Persistencia;

	int contarAlumnos(IConexion icon, String act, int nota) throws Persistencia;

}
