package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Juego {
    private Maquina maquina;
    private Jugador jugador;
    private Context context;

    public Juego (Context context){
        this.context = context;
    }
    public Juego(Maquina maquina, Jugador jugador){
        this.maquina= maquina;
        this.jugador= jugador;
    }
    public static void batallaDirecta(CartaMonstruo monstruoAtacante, Jugador oponente){
        int puntos = oponente.getPuntos() - monstruoAtacante.getAtaque();
        oponente.setPuntos(puntos);
    }

    public static String declararBatalla(CartaMonstruo cartaOponente, CartaMonstruo cartaAtacante, Jugador oponente, Jugador atacante) {
        // Ambas cartas en modo ataque
        if (cartaOponente.eModoAtaque() && cartaAtacante.eModoAtaque()) {
            if (cartaOponente.getAtaque() < cartaAtacante.getAtaque()) {
                int diferencia = cartaOponente.getAtaque() - cartaAtacante.getAtaque();
                int puntos = oponente.getPuntos() - Math.abs(diferencia);
                oponente.setPuntos(puntos);
                cartaOponente.destruida();
                oponente.getTablero().removerCarta(cartaOponente);
                return "Carta: \n"+ cartaOponente + "\ndestruida, puntos de oponente "+oponente.getPuntos()+" .\n";
            } else if (cartaAtacante.getAtaque() == cartaOponente.getAtaque()) {
                oponente.getTablero().removerCarta(cartaOponente);
                atacante.getTablero().removerCarta(cartaAtacante);
                cartaOponente.destruida();
                cartaAtacante.destruida();
                return "Sus cartas fueron iguales.\n";
            } else 
                return "Carta: \n" + cartaAtacante + "\n no pudo atacar\n" + cartaOponente + "\n";
        }
        else if (cartaAtacante.eModoAtaque() && cartaOponente.eModoDefensa()) {
            if (cartaAtacante.getAtaque() > cartaOponente.getDefensa()) {
                oponente.getTablero().removerCarta(cartaOponente);
                cartaOponente.destruida();
                return "Carta oponente destruida.\n";
            } else if (cartaAtacante.getAtaque() < cartaOponente.getDefensa()) {
                int diferencia = cartaAtacante.getAtaque() - cartaOponente.getDefensa();
                int puntos = atacante.getPuntos() - Math.abs(diferencia);
                atacante.setPuntos(puntos);
                cartaOponente.modoAtaque();
                cartaOponente.setPosicion(Posicion.HORIZONTAL);
                return "Ataque fallido, puntos del atacante actualizados.\n";
            } else {
                return "Carta " + cartaAtacante + " no pudo atacar " + cartaOponente + "\n";
            }
        }
    
        // Tablero actualizado
        return "Tablero de " + atacante.getNombre() + ": " + atacante.getTablero().toString() + "\n" +
               "Tablero de " + oponente.getNombre() + ": " + oponente.getTablero().toString() + "\n";
    }

    public static String usarTrampas(Jugador j, CartaMonstruo monsM,ArrayList<CartaTrampa> trampas){
        StringBuilder resultado = new StringBuilder();
        for (Carta especial: j.getTablero().getEspeciales()){
            if ((especial instanceof CartaTrampa) && (((CartaTrampa) especial).usar(monsM))) {
                trampas.add((CartaTrampa) especial);
                j.getTablero().getEspeciales().remove(especial);
                resultado.append("Se usó una carta trampa: ").append(especial.toString()).append("\n");
            }
            else
                resultado.append("");
        }
        return resultado.toString();
    }

    public void faseTomarCarta(){
        jugador.tomarCarta();
        maquina.tomarCarta();
    }

    public void FasePrincipal(){
        // TIENE QUE HABILITAR PARA PONER LAS CARTAS EN EL TABLERO Y SE PUEDAN USAR MAGICAS

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");

        for (Carta carta: jugador.getTablero().getCartasMons()){
            CartaMonstruo c = (CartaMonstruo) carta;
            builder.setMessage(c.toString());
        }
        for (Carta carta: jugador.getTablero().getEspeciales()){
            CartaMagica c = (CartaMagica) carta;
            builder.setMessage(c.toString());
        }


        // Botones del cuadro de diálogo
        builder.setPositiveButton("Ataque", (dialog, which) -> {
            Toast.makeText(context.getApplicationContext(), "Carta colocada en Ataque", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
        });

        builder.setNegativeButton("Defensa", (dialog, which) -> {
            Toast.makeText(context.getApplicationContext(), "Carta colocada en Defensa", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en defensa (guardar el estado, etc.)
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();

    }

    public void FaseBatalla(){

    }


    
}
