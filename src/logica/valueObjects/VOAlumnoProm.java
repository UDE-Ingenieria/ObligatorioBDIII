package logica.valueObjects;


public class VOAlumnoProm extends VOAlumno {

	private double promedio;

	public VOAlumnoProm(int cedula, String nombre, String apellido, double promedio) {
		super(cedula, nombre, apellido);
		this.promedio = promedio;
	}


	public double getPromedio() {
		return promedio;
	}

}
