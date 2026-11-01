package fabricas;

import logica.excepciones.Persistencia;
import persistencia.daos.*;

public class FabricaMySQL implements FabricaAbstracta {

    @Override
    public IDAOAlumnos crearDAOAlumnos() {
        return new DAOAlumnos();
    }

   @Override
   public IDAORegistros crearDAORegistros(int ced) throws Persistencia{
       return new DAORegistros(ced);
    }
}