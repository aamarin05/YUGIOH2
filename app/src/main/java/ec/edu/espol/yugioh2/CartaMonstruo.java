package ec.edu.espol.yugioh2;

public class CartaMonstruo extends Carta {
    private final TipoMonstruo tipo;
    private final TipoAtributo atributo;
    private int defensa;
    private int ataque;

    public CartaMonstruo(String nombre, String descripcion, Orientacion orientacion, TipoMonstruo tipo, TipoAtributo atributo, int defensa, int ataque, String imagen) {
        super(nombre, descripcion, Posicion.VERTICAL, orientacion,imagen);
        this.tipo = tipo;
        this.atributo = atributo;
        this.defensa = defensa;
        this.ataque = ataque;
    }

    // Getters y Setters
    public TipoMonstruo getTipo() {
        return tipo;
    }
    public TipoAtributo getAtributo() {
        return atributo;
    }
    public int getAtaque() {
        return ataque;
    }
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    public int getDefensa() {
        return defensa;
    }
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    // Funciones jugables
    public void cambiarPosicion(Posicion posicion) {
        if (getOrientacion() == Orientacion.ARRIBA) {
            setPosicion(posicion);
        }
    }

    public void modoAtaque() {
        setOrientacion(Orientacion.ARRIBA);
        setPosicion(Posicion.VERTICAL);
    }

    public void modoDefensa() {
        setOrientacion(Orientacion.ABAJO);
    }

    public boolean eModoAtaque() {
        return getPosicion() == Posicion.VERTICAL;
    }

    public boolean eModoDefensa() {
        return getPosicion() == Posicion.HORIZONTAL || getOrientacion() == Orientacion.ABAJO;
    }

    // Método toString
    @Override
    public String toString() {
        String modo;
        if (eModoAtaque())
            modo = "ATAQUE";
        else
            modo = "DEFENSA";
        if (getOrientacion() == Orientacion.ARRIBA) {
            return super.toString() + " \nATQ: " + ataque + " \nDEF: " + defensa + " \nTIPO: " + tipo + " \nATRIBUTO: " + atributo;
        } else {
            return "Carta boca abajo";
        }
    }

}
