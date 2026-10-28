package persistencia.poolConexiones;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import logica.excepciones.Persistencia;

public class PoolConexiones implements IPoolConexiones {

    private String driver, url, user, password;
    private int nivelTran;
    //el arreglo con las conexiones
    private Conexion[] conexiones;
    private int tamanio, creadas, tope;

    public PoolConexiones() throws Persistencia {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("config.txt")); 
            driver = p.getProperty("driver");
            url = p.getProperty("url");
            user = p.getProperty("user");
            password = p.getProperty("password");
            Class.forName(driver);
        } catch (IOException e) {
            throw new Persistencia("Error al leer el archivo de configuracion");
        } catch (ClassNotFoundException e) {
            throw new Persistencia("No se encontro el driver de la base de datos");
        }
        tamanio = 3;
        conexiones = new Conexion[tamanio];
        creadas = 0; //conexiones creadas en total
        tope = 0; //conexiones disponibles
        nivelTran = Connection.TRANSACTION_SERIALIZABLE;
    }

    public synchronized IConexion obtenerConexion() throws Persistencia {
    	Conexion c = null;
    	try {
        	boolean tengoCon = false;
            //Tope = 0 entonces no hay conexiones disponibles 
            //creadas = tamanio enotnces todas las conexiones estan prestadas 
            while (!tengoCon) {
            	if (tope > 0) { //si hay conexiones disponibles 
                    tope--; //bajo porque sque una conexion
                    c = conexiones[tope]; //agarro la conexion
                    tengoCon = true;
                } else if (creadas < tamanio) { //puedo crear una nueva, la creo y la retorno
                	Connection con = DriverManager.getConnection(url, user, password);
                	c = new Conexion(con);
                    c.getConnection().setTransactionIsolation(nivelTran);
                    c.getConnection().setAutoCommit(false);
                    creadas++; //sumo al total de creadas
                    tengoCon = true;
                } else //sino tengo que esperar
	            	wait(); 
            }
        } catch (InterruptedException e) {
            throw new Persistencia("Se interrumpio");
        } catch (SQLException e) {
        	throw new Persistencia("Error al obtener una conexion con la base de datos");
        }
    	return c;
    }
    //devuelvo conexion al pool
    public synchronized void liberarConexion(IConexion ic, boolean ok) throws Persistencia {
        Persistencia error = null;
        Conexion c = (Conexion) ic; //casteo
        try {
            if (ok) //salio todo bien entonces commiteo
                c.getConnection().commit();
            else //no salio todo bien entonces tiro para atras
                c.getConnection().rollback();
        } catch (SQLException e) {
            error = new Persistencia("Error al finalizar la transaccion");
        }
        conexiones[tope] = c; //meto en el arreglo y queda disponible
        tope++; //subo tope
        notify(); ///despierto a alguien que esté esperando 
        if (error != null)
            throw error;
    }
}