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

}
