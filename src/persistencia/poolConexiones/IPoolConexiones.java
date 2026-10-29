package persistencia.poolConexiones;

import logica.excepciones.Persistencia;

public interface IPoolConexiones {
    IConexion obtenerConexion(boolean modifica) throws Persistencia;
    void liberarConexion(IConexion con, boolean ok) throws Persistencia;
}
