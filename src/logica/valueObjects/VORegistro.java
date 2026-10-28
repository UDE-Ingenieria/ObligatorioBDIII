package logicaPersistencia.valueObjects;

public class VORegistro {

	private String actividad;
	private int nota;

	public VORegistro(String actividad, int nota) {
		this.actividad = actividad;
		this.nota = nota;
	}

	public String getActividad() {
		return actividad;
	}

	public int getNota() {
		return nota;
	}

}
