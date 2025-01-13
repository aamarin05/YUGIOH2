package ec.edu.espol.yugioh2;
import ec.edu.espol.yugioh2.*;

public class CartaTrampa extends Carta{
    private final TipoAtributo atributo;

    public CartaTrampa(String nombre, String descripcion, Posicion posicion, Orientacion orientacion, TipoAtributo atributo) {
        super(nombre, descripcion, posicion, orientacion);
        this.atributo = atributo;
        setOrientacion(Orientacion.ABAJO);
    }

    // Getter
    public TipoAtributo getAtributo() {
        return atributo;
    }

    // Función jugable
    public boolean usar(CartaMonstruo cartaAtacante) {
        return atributo == cartaAtacante.getAtributo();
    }

    // Método toString
    @Override
    public String toString() {
        return "Carta Trampa: " + getNombre() + ", detiene el ataque de un monstruo con atributo " + atributo;
    }
}
