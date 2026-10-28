package logicaPersistencia.valueObjects;

public class VOActividad {

	private String actividad;
	private int veces;
	
	public VOActividad(String actividad, int veces) {
		this.actividad = actividad;
		this.veces = veces;
	}

	public String getActividad() {
		return actividad;
	}

	public int getVeces() {
		return veces;
	}
	
}
