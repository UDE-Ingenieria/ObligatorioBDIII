package persistencia.daos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logica.Alumno;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import persistencia.poolConexiones.IConexion;

public class DAOAlumnosTxt implements IDAOAlumnos {

	private static final String ARCHIVO_ALUMNOS = "alumnos.txt";

	public DAOAlumnosTxt() {
	}

	@Override
	public boolean member(IConexion icon, int cedA) throws Persistencia {
		File archivo = new File(ARCHIVO_ALUMNOS);
		if (!archivo.exists()) {
			return false;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] partes = linea.split("\\|");
					int ced = Integer.parseInt(partes[0].trim());
					if (ced == cedA) {
						return true;
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			throw new Persistencia("Error al verificar existencia del alumno");
		}
		return false;
	}

	@Override
	public void insert(IConexion icon, Alumno alu) throws Persistencia {
		List<VOAlumno> actuales = listarAlumnos(icon);
		List<VOAlumno> nuevos = new ArrayList<>();
		boolean insertado = false;

		for (VOAlumno vo : actuales) {
			if (!insertado && alu.getCedula() < vo.getCedula()) {
				nuevos.add(new VOAlumno(alu.getCedula(), alu.getNombre(), alu.getApellido()));
				insertado = true;
			}
			nuevos.add(vo);
		}
		if (!insertado) {
			nuevos.add(new VOAlumno(alu.getCedula(), alu.getNombre(), alu.getApellido()));
		}

		File archivo = new File(ARCHIVO_ALUMNOS);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
			for (VOAlumno vo : nuevos) {
				bw.write(vo.getCedula() + " | " + vo.getNombre() + " | " + vo.getApellido());
				bw.newLine();
			}
		} catch (IOException e) {
			throw new Persistencia("Error al insertar el alumno");
		}
	}

	@Override
	public Alumno find(IConexion icon, int cedA) throws Persistencia {
		File archivo = new File(ARCHIVO_ALUMNOS);
		Alumno alu = null;
		if (!archivo.exists()) {
			return null;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] partes = linea.split("\\|");
					int ced = Integer.parseInt(partes[0].trim());
					if (ced == cedA) {
						String nom = partes[1].trim();
						String ape = partes[2].trim();
						alu = new Alumno(ced, nom, ape, new DAORegistros(ced));
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			throw new Persistencia("Error al buscar el alumno");
		}
		return null;
	}

	@Override
	public boolean esVacio(IConexion icon) throws Persistencia {
		File archivo = new File(ARCHIVO_ALUMNOS);
		if (!archivo.exists() || archivo.length() == 0) {
			return true;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					return false;
				}
			}
		} catch (IOException e) {
			throw new Persistencia("Error al verificar si existen alumnos");
		}
		return true;
	}

	@Override
	public List<VOAlumno> listarAlumnos(IConexion icon) throws Persistencia {
		List<VOAlumno> lista = new ArrayList<>();
		File archivo = new File(ARCHIVO_ALUMNOS);
		if (!archivo.exists()) {
			return lista;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] partes = linea.split("\\|");
					int ced = Integer.parseInt(partes[0].trim());
					String nom = partes[1].trim();
					String ape = partes[2].trim();
					lista.add(new VOAlumno(ced, nom, ape));
				}
			}
		} catch (IOException | NumberFormatException e) {
			throw new Persistencia("Error al listar los alumnos");
		}
		return lista;
	}

	@Override
	public VOAlumnoProm alumnoMayorPromedio(IConexion icon) throws Persistencia {
		List<VOAlumno> alumnos = listarAlumnos(icon);
		VOAlumnoProm mayor = null;
		double mayorProm = -1.0;

		try {
			for (VOAlumno vo : alumnos) {
				double prom = calcularPromedioAlumno(vo.getCedula());
				if (mayor == null || prom > mayorProm) {
					mayorProm = prom;
					mayor = new VOAlumnoProm(vo.getCedula(), vo.getNombre(), vo.getApellido(), prom);
				}
			}
		} catch (IOException | NumberFormatException e) {
			throw new Persistencia("Error al obtener alumno con mayor promedio");
		}
		return mayor;
	}

	@Override
	public int contarAlumnos(IConexion icon, String act, int nota) throws Persistencia {
		List<VOAlumno> alumnos = listarAlumnos(icon);
		int cant = 0;

		try {
			for (VOAlumno vo : alumnos) {
				if (tieneRegistroConActividadYNota(vo.getCedula(), act, nota)) {
					cant++;
				}
			}
		} catch (IOException | NumberFormatException e) {
			throw new Persistencia("Error al contar los alumnos");
		}
		return cant;
	}

	private double calcularPromedioAlumno(int cedula) throws IOException {
		File archivoReg = new File("registros" + cedula + ".txt");
		if (!archivoReg.exists()) {
			return 0.0;
		}
		int suma = 0;
		int cant = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(archivoReg))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] partes = linea.split("\\|");
					int nota = Integer.parseInt(partes[2].trim());
					suma += nota;
					cant++;
				}
			}
		}
		return (cant == 0) ? 0.0 : (double) suma / cant;
	}

	private boolean tieneRegistroConActividadYNota(int cedula, String act, int notaBuscada) throws IOException {
		File archivoReg = new File("registros" + cedula + ".txt");
		if (!archivoReg.exists()) {
			return false;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(archivoReg))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty()) {
					String[] partes = linea.split("\\|");
					String actividad = partes[1].trim();
					int nota = Integer.parseInt(partes[2].trim());
					if (actividad.equalsIgnoreCase(act.trim()) && nota == notaBuscada) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
