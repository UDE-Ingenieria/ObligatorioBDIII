package persistencia.daos;
import java.util.List;

import logica.Registro;
import logica.excepciones.Persistencia;
import persistencia.poolConexiones.IConexion;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VORegistroListado;

public interface IDAORegistros {

    int largo(IConexion icon) throws Persistencia;

    void insback(IConexion icon, Registro reg) throws Persistencia;

    Registro kesimo(IConexion icon, int numR) throws Persistencia;

    List<VORegistroListado> listarRegistros(IConexion icon) throws Persistencia;

    double promedioNotas(IConexion icon) throws Persistencia;

    VOActividad actividadMasCursada(IConexion icon) throws Persistencia;
}

