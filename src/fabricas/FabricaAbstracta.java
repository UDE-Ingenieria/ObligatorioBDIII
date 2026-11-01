package fabricas;

import logica.excepciones.Persistencia;
import persistencia.daos.*;

public interface FabricaAbstracta {

    IDAOAlumnos crearDAOAlumnos();

    IDAORegistros crearDAORegistros(int ced) throws Persistencia;
}
