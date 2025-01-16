package ec.edu.espol.yugioh2;

public class Carta {
    private final String nombre;
    private final String descripcion;
    private Posicion posicion;
    private Orientacion orientacion;

    private final String imagen;

    public Carta(String nombre, String descripcion, Posicion posicion, Orientacion orientacion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicion = posicion;
        this.orientacion = orientacion;
        this.imagen= imagen;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Orientacion getOrientacion() {
        return orientacion;
    }

    public String getImagen() { return imagen;}

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    // Método destrucción de la carta
    public String destruida() {
        return nombre + " fue destruida";
    }

    // TO STRING
    @Override
    public String toString() {
        if (orientacion == Orientacion.ARRIBA) {
            return nombre + "\n" + descripcion;
        } else {
            return "Carta boca abajo";
        }
    }
}

