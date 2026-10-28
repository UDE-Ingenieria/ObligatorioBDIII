package logica.excepciones;

public class Persistencia extends Exception {
    private static final long serialVersionUID = 1L;

    public Persistencia(String mensaje) {
        super(mensaje);
    }
}
