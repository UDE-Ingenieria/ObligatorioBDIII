package persistencia.consultas;

public class Consultas {

	public String existeAlumno() {
		return "SELECT 1 FROM Alumnos WHERE cedula = ?";
	}

	public String insertarAlumno() {
		return "INSERT INTO Alumnos VALUES (?, ?, ?)";
	}

	public String obtenerAlumno() {
		return "SELECT * FROM Alumnos WHERE cedula = ?";
	}

	public String hayAlumnos() {
		return "SELECT 1 FROM Alumnos LIMIT 1";
	}

	public String listarAlumnos() {
		return "SELECT * FROM Alumnos ORDER BY cedula";
	}

	public String largoRegistros() {
		return "SELECT COUNT(*) AS largo FROM Registros WHERE cedAlu = ?";
	}

	public String insertarRegistro() {
		return "INSERT INTO Registros (numero, cedAlu, actividad, nota) VALUES (?, ?, ?, ?)";
	}

	public String obtenerRegistro() {
		return "SELECT * FROM Registros WHERE cedAlu = ? AND numero = ?";
	}

	public String listarRegistros() {
		return "SELECT * FROM Registros WHERE cedAlu = ? ORDER BY numero";
	}

	public String promedioNotas() {
		return "SELECT AVG(nota) AS promedio FROM Registros WHERE cedAlu = ?";
	}

	public String actividadMasCursada() {
		return "SELECT actividad, COUNT(*) AS veces "
				+ "FROM Registros "
				+ "WHERE cedAlu = ? "
				+ "GROUP BY actividad "
				+ "ORDER BY veces DESC "
				+ "LIMIT 1";
	}
	
	//limit 1 me queda el mayor solo arriba
	public String alumnoMayorPromedio() {
	    return "SELECT a.cedula, a.nombre, a.apellido, AVG(r.nota) AS promedio "
	         + "FROM Alumnos a LEFT JOIN Registros r ON a.cedula = r.cedAlu "
	         + "GROUP BY a.cedula, a.nombre, a.apellido "
	         + "ORDER BY promedio DESC LIMIT 1";
	}
	
	//distinct para que no cuente el mismo alumno dos veces si curso esa actividad mas de una vez
	public String contarAlumnos () {
		return "SELECT COUNT(DISTINCT cedAlu) AS cantidad FROM Registros WHERE actividad = ? AND nota = ?";
	}
	
}
