package logica;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;

public interface IFachada extends Remote {

	public void nuevoAlumno(VOAlumno voA) throws RemoteException, Persistencia;

	public void registrarNota(int cedA, VORegistro voR) throws RemoteException, Persistencia;

	public List<VOAlumno> listarAlumnos() throws RemoteException, Persistencia;

	public VORegistro obtenerRegistro(int cedA, int numR) throws RemoteException, Persistencia;

	public List<VORegistroListado> listarRegistros(int cedA) throws RemoteException, Persistencia;

	public double promedioNotas(int cedA) throws RemoteException, Persistencia;

	public VOAlumnoProm alumnoMayorPromedio() throws RemoteException, Persistencia;

	public int contarAlumnos(String act, int nota) throws RemoteException, Persistencia;

	public VOActividad actividadMasCursada(int cedA) throws RemoteException, Persistencia;

}
