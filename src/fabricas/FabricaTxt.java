package fabricas;

import logica.excepciones.Persistencia;
import persistencia.daos.*;

public class FabricaTxt implements FabricaAbstracta {

    @Override
    public IDAOAlumnos crearDAOAlumnos() {
        return new DAOAlumnosTxt(this);
    }

   @Override
   public IDAORegistros crearDAORegistros(int ced) throws Persistencia{
       return new DAORegistrosTxt(ced);
    }
    
}
