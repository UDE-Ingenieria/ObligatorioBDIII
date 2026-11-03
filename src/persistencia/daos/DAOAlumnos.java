package persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logica.Alumno;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import persistencia.consultas.Consultas;
import persistencia.poolConexiones.Conexion;
import persistencia.poolConexiones.IConexion;

public class DAOAlumnos implements IDAOAlumnos{
	public DAOAlumnos() {
        
    }
	
    public boolean member(IConexion icon, int cedA) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        boolean existe = false;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().existeAlumno());
            ps.setInt(1, cedA);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al verificar existencia del alumno");
        }
        return existe;
    }
    
    public void insert(IConexion icon, Alumno alu) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().insertarAlumno());
            ps.setInt(1, alu.getCedula());
            ps.setString(2, alu.getNombre());
            ps.setString(3, alu.getApellido());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al insertar el alumno");
        }
    }
    
    public Alumno find(IConexion icon, int cedA) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        Alumno alu = null;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().obtenerAlumno());
            ps.setInt(1, cedA);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                alu = new Alumno(cedA, nom, ape, new DAORegistros(cedA));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al buscar el alumno");
        }
        return alu;
    }
    
    public boolean esVacio(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        boolean vacio = true;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().hayAlumnos());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                vacio = (rs.getInt(1) == 0);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al verificar si existen alumnos");
        }
        return vacio;
    }
    
    public List<VOAlumno> listarAlumnos(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        List<VOAlumno> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().listarAlumnos());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ced = rs.getInt("cedula");
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                lista.add(new VOAlumno(ced, nom, ape));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al listar los alumnos");
        }
        return lista;
    }
    
    public VOAlumnoProm alumnoMayorPromedio(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        VOAlumnoProm vo = null;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().alumnoMayorPromedio());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int ced = rs.getInt("cedula");
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                double prom = rs.getDouble("promedio");
                vo = new VOAlumnoProm(ced, nom, ape, prom);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al obtener alumno con mayor promedio");
        }
        return vo;
    }
    
    public int contarAlumnos(IConexion icon, String act, int nota) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        int cant = 0;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().contarAlumnos());
            ps.setString(1, act);
            ps.setInt(2, nota);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cant = rs.getInt(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al contar los alumnos");
        }
        return cant;
    }
}
