package persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logica.Registro;
import logica.excepciones.Persistencia;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VORegistroListado;
import persistencia.consultas.Consultas;
import persistencia.poolConexiones.Conexion;
import persistencia.poolConexiones.IConexion;

public class DAORegistros implements IDAORegistros{
    private int cedAlumno;
    
    public DAORegistros(int ced) throws Persistencia {
        this.cedAlumno = ced;
    }
    
    public int largo(IConexion icon) throws Persistencia {
    	Connection con = ((Conexion) icon).getConnection(); //obtengo la con
        int cant = 0;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().largoRegistros());
            ps.setInt(1, cedAlumno);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                cant = rs.getInt("largo");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al contar los registros");
        }
        return cant;
    }
    
    public void insback(IConexion icon, Registro reg) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().insertarRegistro());
            // mismo orden que en la consulta: (numero, cedAlu, actividad, nota)
            ps.setInt(1, reg.getNumero());
            ps.setInt(2, cedAlumno);
            ps.setString(3, reg.getActividad());
            ps.setInt(4, reg.getNota());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al agregar el registro");
        }
    }
    
    
    public Registro kesimo(IConexion icon, int numR) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        Registro reg = null;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().obtenerRegistro());
            ps.setInt(1, cedAlumno);
            ps.setInt(2, numR);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int num = rs.getInt("numero");
                int nota = rs.getInt("nota");
                String act = rs.getString("actividad");
                reg = new Registro(num, nota, act);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al obtener el k-ésimo registro");
        }
        return reg;
    }
    
    
    public List<VORegistroListado> listarRegistros(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        List<VORegistroListado> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().listarRegistros());
            ps.setInt(1, cedAlumno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int num = rs.getInt("numero");
                int nota = rs.getInt("nota");
                String act = rs.getString("actividad");
                lista.add(new VORegistroListado(num, act, nota));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al listar los registros");
        }
        return lista;
    }

    
    public double promedioNotas(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        double prom = 0.0;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().promedioNotas());
            ps.setInt(1, cedAlumno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prom = rs.getDouble(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al calcular el promedio de notas");
        }
        return prom;
    }

   
    public VOActividad actividadMasCursada(IConexion icon) throws Persistencia {
        Connection con = ((Conexion) icon).getConnection();
        VOActividad vo = null;
        try {
            PreparedStatement ps = con.prepareStatement(new Consultas().actividadMasCursada());
            ps.setInt(1, cedAlumno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String act = rs.getString("actividad");
                int cant = rs.getInt("veces");
                vo = new VOActividad(act, cant);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Persistencia("Error al obtener la actividad más cursada");
        }
        return vo;
    }
  
    
}
