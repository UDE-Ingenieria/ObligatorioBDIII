package fabricas;

import logica.excepciones.Persistencia;
import persistencia.daos.IDAOAlumnos;
import persistencia.daos.IDAORegistros;

public interface FabricaAbstracta {

    IDAOAlumnos crearDAOAlumnos();

    IDAORegistros crearDAORegistros(int ced) throws Persistencia;
}
