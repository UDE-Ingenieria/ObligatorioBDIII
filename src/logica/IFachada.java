package logica;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import logica.excepciones.LogicaPersistenciaException;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;

public interface IFachada extends Remote {

	public void nuevoAlumno(VOAlumno voA) throws RemoteException, LogicaPersistenciaException;

	public void registrarNota(int cedA, VORegistro voR) throws RemoteException, LogicaPersistenciaException;

	public List<VOAlumno> listarAlumnos() throws RemoteException, LogicaPersistenciaException;

	public VORegistro obtenerRegistro(int cedA, int numR) throws RemoteException, LogicaPersistenciaException;

	public List<VORegistroListado> listarRegistros(int cedA) throws RemoteException, LogicaPersistenciaException;

	public double promedioNotas(int cedA) throws RemoteException, LogicaPersistenciaException;

	public VOAlumnoProm alumnoMayorPromedio() throws RemoteException, LogicaPersistenciaException;

	public int contarAlumnos(String act, int nota) throws RemoteException, LogicaPersistenciaException;

	public VOActividad actividadMasCursada(int cedA) throws RemoteException, LogicaPersistenciaException;

}
