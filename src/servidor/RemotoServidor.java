package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import logica.IFachada;

public class RemotoServidor {

	public static void main(String[] args) {
		try {
			Properties p = new Properties();
			String nomArch = "config/config.properties";
			p.load(new FileInputStream(nomArch));
			String ip = p.getProperty("ipServidor");
			String puerto = p.getProperty("puertoServidor");
			int port = Integer.parseInt(puerto);

			IFachada obj = new Fachada();

			LocateRegistry.createRegistry(port);

			String ruta = "//" + ip + ":" + puerto + "/Fachada";
			Naming.rebind(ruta, obj);

			System.out.println("SERVIDOR PUBLICADO CORRECTAMENTE");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
