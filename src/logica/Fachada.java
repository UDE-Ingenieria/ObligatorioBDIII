package logica;

//Cuando queden los DAOS, descomentar

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Properties;

import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;

import persistencia.daos.IDAOAlumnos;
import fabricas.FabricaAbstracta;

import persistencia.poolConexiones.IConexion;
import persistencia.poolConexiones.IPoolConexiones;

public class Fachada extends UnicastRemoteObject implements IFachada {

	private static final long serialVersionUID = 1L;

	private IPoolConexiones ipool;
	private IDAOAlumnos daoAlumnos;
	private FabricaAbstracta fabrica;

	public Fachada() throws RemoteException {
		try {
			Properties p = new Properties();
			FileInputStream fis = new FileInputStream("config/config.properties");
			p.load(fis);
			fis.close();
			// Los nombres del pool y de la fabrica concretos salen del archivo de configuracion:
			// el medio de persistencia usado no queda visible en el codigo
			String medio = p.getProperty("medioPersistencia");
			String poolConcreto = (medio == null) ? null : p.getProperty("pool." + medio.trim());
			if (poolConcreto == null)
				throw new IllegalStateException("Medio de persistencia no configurado: " + medio);
			ipool = (IPoolConexiones) Class.forName(poolConcreto.trim()).getDeclaredConstructor().newInstance();

			// La fabrica concreta se carga igual que el pool: por reflexion, a partir del medio configurado
			String fabricaConcreta = p.getProperty("fabrica." + medio.trim());
			if (fabricaConcreta == null)
				throw new IllegalStateException("Fabrica no configurada para el medio: " + medio);
			fabrica = (FabricaAbstracta) Class.forName(fabricaConcreta.trim()).getDeclaredConstructor().newInstance();
			daoAlumnos = fabrica.crearDAOAlumnos();
		} catch (IOException | ReflectiveOperationException | RuntimeException e) {
			throw new RemoteException("No se pudo inicializar el medio de persistencia", e);
		}
	}

	public void nuevoAlumno(VOAlumno voA) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(true);
		boolean ok = false;
		try {
			if (daoAlumnos.member(icon, voA.getCedula()))
				throw new Persistencia("El alumno ya ha sido registrado anteriormente");
			daoAlumnos.insert(icon, new Alumno(voA.getCedula(), voA.getNombre(), voA.getApellido(),
					fabrica.crearDAORegistros(voA.getCedula())));
			ok = true;
		} finally {
			ipool.liberarConexion(icon, ok);
		}
	}

	public void registrarNota(int cedA, VORegistro voR) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(true);
		boolean ok = false;
		try {
			Alumno alu = buscarAlumno(icon, cedA);
			// El numero lo asigna el propio alumno al agregar el registro
			alu.agregarRegistro(icon, new Registro(0, voR.getNota(), voR.getActividad()));
			ok = true;
		} finally {
			ipool.liberarConexion(icon, ok);
		}
	}

	public List<VOAlumno> listarAlumnos() throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			return daoAlumnos.listarAlumnos(icon);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public VORegistro obtenerRegistro(int cedA, int numR) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			Alumno alu = buscarAlumno(icon, cedA);
			VORegistro voR = alu.obtenerRegistro(icon, numR);
			if (voR == null)
				throw new Persistencia("El alumno no tiene un registro con ese numero");
			return voR;
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public List<VORegistroListado> listarRegistros(int cedA) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			Alumno alu = buscarAlumno(icon, cedA);
			return alu.listarRegistros(icon);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public double promedioNotas(int cedA) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			Alumno alu = buscarAlumno(icon, cedA);
			return alu.promedioNotas(icon);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public VOAlumnoProm alumnoMayorPromedio() throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			if (daoAlumnos.esVacio(icon))
				throw new Persistencia("No hay alumnos registrados");
			return daoAlumnos.alumnoMayorPromedio(icon);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public int contarAlumnos(String act, int nota) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			return daoAlumnos.contarAlumnos(icon, act, nota);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	public VOActividad actividadMasCursada(int cedA) throws RemoteException, Persistencia {
		IConexion icon = ipool.obtenerConexion(false);
		try {
			Alumno alu = buscarAlumno(icon, cedA);
			if (alu.cantidadRegistros(icon) == 0)
				throw new Persistencia("El alumno no tiene actividades registradas");
			return alu.actividadMasCursada(icon);
		} finally {
			ipool.liberarConexion(icon, true);
		}
	}

	private Alumno buscarAlumno(IConexion icon, int cedA) throws Persistencia {
		Alumno alu = daoAlumnos.find(icon, cedA);
		if (alu == null)
			throw new Persistencia("El alumno no esta registrado");
		return alu;
	}

}