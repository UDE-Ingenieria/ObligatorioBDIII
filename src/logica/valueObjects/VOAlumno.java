package logicaPersistencia.valueObjects;

public class VOAlumno {

	private int cedula;
	private String nombre;
	private String apellido;

	public VOAlumno(int cedula, String nombre, String apellido) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
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

}
