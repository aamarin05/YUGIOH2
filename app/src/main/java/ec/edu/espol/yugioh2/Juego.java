package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Juego {
    private Maquina maquina;
    private Jugador jugador;
    private Context context;

    public Juego (Context context){
        this.context = context;
    }
    public Juego(Jugador jugador,Context context){
        maquina= new Maquina(context);
        this.jugador= jugador;
        this.context = context;
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
        String msjJ = jugador.tomarCarta();
        String msjM = maquina.tomarCarta();
        String msj = msjJ+ "\n" + msjM;
        Utilitaria.crearDialogs(context,"Cartas tomadas", msj, "Se han tomado las cartas");
        //Buscar la imagen con ese nombre y colocarlo en el LinearLayout de la mano
        //Tiene que agregarse la carta a la mano visualmente
    }

    public void fasePrincipal(LinearLayout mano, LinearLayout monstruosJ, LinearLayout especialesJ){
        // TIENE QUE HABILITAR PARA PONER LAS CARTAS EN EL TABLERO Y SE PUEDAN USAR MAGICAS

        Utilitaria.colocarTablero(context,jugador.getMano(),mano,monstruosJ, especialesJ,"Fase Principal");
       // Utilitaria.colocarTablero(context,maquina.getMano(),mano,monstruosJ,"Fase Principal");


    }

    private void faseBatalla(LinearLayout monstruosA,LinearLayout monstruosO) {
        //BATALLA JUGADOR
        ArrayList<Carta> cartasJ= new ArrayList<>();
        cartasJ.addAll(jugador.getTablero().getCartasMons());
        ArrayList<Carta> cartasM= new ArrayList<>();
        cartasM.addAll(maquina.getTablero().getCartasMons());

        ArrayList<Carta> cartas = Utilitaria.declararBatalla(context,cartasJ,cartasM,monstruosA, monstruosO,"Fase Batalla");
        CartaMonstruo oponente = (CartaMonstruo) cartas.get(1);
        CartaMonstruo atacante = (CartaMonstruo) cartas.get(0);

        Juego.declararBatalla(oponente,atacante,maquina,jugador);

    }

    public void jugar(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ, LinearLayout especialesM) {
        //Se coloquen las cartas de la mano del jugador y de la maquina en el linearLayout
        for (Carta c: jugador.getMano()){
            Utilitaria.crearyAgregar(context,c,manoJ);
        }

        for (Carta c: maquina.getMano()){
            Utilitaria.crearyAgregar(context,c,manoM);
        }
        //Se cambie el text del nombre del jugador
        //Que se cambien los turnos
        //Que se cambien las fases
        while (jugador.getPuntos() > 0 && maquina.getPuntos() > 0){
            faseTomarCarta();
            Carta cartaTomadaJ = jugador.getMano().get(jugador.getMano().size()-1);
            Utilitaria.crearyAgregar(context,cartaTomadaJ,manoJ);
            Carta cartaTomadaM = maquina.getMano().get(maquina.getMano().size()-1);
            Utilitaria.crearyAgregar(context,cartaTomadaM,manoM);

            fasePrincipal(manoJ,monstruosJ,especialesJ);
            maquina.mFasePrincipal();
            for (Carta carta: maquina.getTablero().getCartasMons())
                Utilitaria.crearyAgregar(context,carta,monstruosM);
            for (Carta carta: maquina.getTablero().getEspeciales())
                Utilitaria.crearyAgregar(context,carta,especialesM);

            faseBatalla(monstruosJ,monstruosM);
        }
    }
    public void prueba(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ,LinearLayout especialesM) {
        //Se coloquen las cartas de la mano del jugador y de la maquina en el linearLayout
        for (Carta c: jugador.getMano()){
            Utilitaria.crearyAgregar(context,c,manoJ);
        }

        for (Carta c: maquina.getMano()){
            Utilitaria.crearyAgregar(context,c,manoM);
        }
        //Se cambie el text del nombre del jugador
        //Que se cambien los turnos
        //Que se cambien las fases



        fasePrincipal(manoJ,monstruosJ,especialesJ);
        maquina.mFasePrincipal();

        for (Carta carta: maquina.getTablero().getCartasMons())
            Utilitaria.reemplazar(context,carta,monstruosM);
        for (Carta carta: maquina.getTablero().getEspeciales())
            Utilitaria.reemplazar(context,carta,especialesM);


        //faseBatalla(monstruosJ,monstruosM);




    }


}
