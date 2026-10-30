package logica;

import java.util.List;

import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;
/*import persistencia.daos.IDAORegistros;
import persistencia.fabricas.CargadorFabrica;
import persistencia.fabricas.FabricaAbstracta;
*/
import persistencia.poolConexiones.IConexion;

public class Alumno {

	private int cedula;
	private String nombre, apellido;
	// private IDAORegistros secuencia;

	public Alumno(int cedula, String nombre, String apellido) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
	//	FabricaAbstracta fabrica = CargadorFabrica.cargar();
	//	this.secuencia = fabrica.crearIDAORegistros(cedula);
	}

	public int getCedula() {
		return cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	/*
	public int cantidadRegistros(IConexion icon) throws Persistencia {
		return secuencia.largo(icon);
	}

	// El numero del nuevo registro lo asigna el alumno: el siguiente al ultimo ingresado (1 si es el primero)
	public void agregarRegistro(IConexion icon, Registro reg) throws Persistencia {
		int numero = secuencia.largo(icon) + 1;
		secuencia.insback(icon, new Registro(numero, reg.getNota(), reg.getActividad()));
	}

	// Devuelve null si el alumno no tiene un registro con ese numero
	public VORegistro obtenerRegistro(IConexion icon, int numR) throws Persistencia{
		Registro reg = secuencia.kesimo(icon, numR);
		if (reg == null)
			return null;
		return new VORegistro(reg.getActividad(), reg.getNota());
	}

	public List<VORegistroListado> listarRegistros(IConexion icon) throws Persistencia {
		return secuencia.listarRegistros(icon);
	}

	public double promedioNotas(IConexion icon) throws Persistencia {
		return secuencia.promedioNotas(icon);
	}

	public VOActividad actividadMasCursada(IConexion icon) throws Persistencia {
		return secuencia.actividadMasCursada(icon);
	}
*/
}
