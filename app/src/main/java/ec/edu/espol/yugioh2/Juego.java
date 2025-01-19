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
    private int turno=0;

    private String fase;

    public Juego (Context context){
        this.context = context;
    }
    public Juego(Jugador jugador,Context context){
        maquina= new Maquina(context);
        this.jugador= jugador;
        this.context = context;
    }

    public void setFase(String nuevaFase, LinearLayout manoJ, LinearLayout manoM,
                        LinearLayout monstruosJ, LinearLayout monstruosM,
                        LinearLayout especialesJ, LinearLayout especialesM) {
        this.fase = nuevaFase;
        // Llama automáticamente a la función prueba cuando se actualiza la fase
        prueba(manoJ, manoM, monstruosJ, monstruosM, especialesJ, especialesM);
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
                return "Carta: \n"+ cartaOponente + "destruida. \nPuntos de oponente actualizados. ";
            } else if (cartaAtacante.getAtaque() == cartaOponente.getAtaque()) {
                oponente.getTablero().removerCarta(cartaOponente);
                atacante.getTablero().removerCarta(cartaAtacante);
                cartaOponente.destruida();
                cartaAtacante.destruida();
                return "Sus cartas fueron iguales y se destruyeron.\n";
            } else 
                return "Carta: \n" + cartaAtacante + "\n no pudo atacar a\n" + cartaOponente + "\n porque es de menor ataque";
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
                return "Carta " + cartaAtacante + " no pudo atacar " + cartaOponente + "\n Mismo ataque y defensa";
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

        Utilitaria.colocarTablero(context,jugador.getMano(),mano,monstruosJ, especialesJ,"Fase Principal",jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales());
       // Utilitaria.colocarTablero(context,maquina.getMano(),mano,monstruosJ,"Fase Principal");


    }

    private void faseBatalla(LinearLayout monstruosA,LinearLayout monstruosO, ArrayList<CartaMonstruo> cartasA, ArrayList<CartaMonstruo> cartasO,Jugador jugador,Jugador maquina) {
        //BATALLA JUGADOR

        //Utilitaria.declararBatalla(context,cartasJ,cartasM,monstruosA, monstruosO,"Fase Batalla");
        //Utilitaria.gestionarBatalla(context,cartasA,monstruosA,cartasO,monstruosO,jugador,maquina,"Fase Batalla");

        //ArrayList<Carta> cartas = Utilitaria.declararBatalla(context,cartasJ,cartasM,monstruosA, monstruosO,"Fase Batalla");
        //CartaMonstruo oponente = (CartaMonstruo) cartas.get(1);
        //CartaMonstruo atacante = (CartaMonstruo) cartas.get(0);

        //Juego.declararBatalla(oponente,atacante,maquina,jugador);

    }

    public void jugar(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ,LinearLayout especialesM) {
        if (turno==0){
            for (Carta c : jugador.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoJ);
            }
            for (Carta c : maquina.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoM);
            }
            turno+=1;

        }
        if (fase.equals("Fase Tomar Carta")) {
            //Se coloquen las cartas de la mano del jugador y de la maquina en el linearLayout
            Carta ct= jugador.getDeck().getCartas().get(0);
            jugador.getMano().add(ct);
            jugador.getDeck().getCartas().remove(0);
            Toast.makeText(context, "Jugador tomo la carta "+ct.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ct,manoJ);

            Carta ctm= maquina.getDeck().getCartas().get(0);
            maquina.getMano().add(ctm);
            maquina.getDeck().getCartas().remove(0);
            Toast.makeText(context, "La maquina tomo la carta "+ctm.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ctm,manoM);



            //Se cambie el text del nombre del jugador
            //Que se cambien los turnos
            //Que se cambien las fases

            turno++;
        }
        if (fase.equals("Fase Principal")) {



            fasePrincipal(manoJ, monstruosJ, especialesJ);
            maquina.mFasePrincipal();

            for (Carta carta : maquina.getTablero().getCartasMons()) {
                Utilitaria.reemplazar(context, carta, monstruosM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            for (Carta carta : maquina.getTablero().getEspeciales()) {
                Utilitaria.reemplazar(context, carta, especialesM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            //Utilitaria.quitarCartas(context,manoJ,jugador.getMano());

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < jugador.getMano().size(); i++) {
                builder.append(i + 1).append(". ") // Agrega el número de la lista
                        .append(jugador.getMano().get(i).getNombre())       // Agrega el elemento actual
                        .append("\n");              // Salto de línea después de cada elemento
            }

            Utilitaria.crearDialogs(context,"Context",builder.toString(),"ok");



        }


        //if (fase.equals("Fase Batalla"))
        //faseBatalla(monstruosJ,monstruosM);

    }
    public void prueba(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ,LinearLayout especialesM) {
        if (turno==0){
            for (Carta c : jugador.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoJ);
            }
            for (Carta c : maquina.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoM);
            }
            turno+=1;

        }
        if (fase.equals("Fase Tomar Carta")) {
            //Se coloquen las cartas de la mano del jugador y de la maquina en el linearLayout
            Carta ct= jugador.getDeck().getCartas().get(0);
            jugador.getMano().add(ct);
            jugador.getDeck().getCartas().remove(0);
            Toast.makeText(context, "Jugador tomo la carta "+ct.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ct,manoJ);

            Carta ctm= maquina.getDeck().getCartas().get(0);
            maquina.getMano().add(ctm);
            maquina.getDeck().getCartas().remove(0);
            Toast.makeText(context, "La maquina tomo la carta "+ctm.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ctm,manoM);
            Utilitaria.eliminarClickListenersTablero(monstruosJ, monstruosM, especialesJ, especialesM);

            //Se cambie el text del nombre del jugador
            //Que se cambien los turnos
            //Que se cambien las fases
            turno++;
        }
        if (fase.equals("Fase Principal")) {

            fasePrincipal(manoJ, monstruosJ, especialesJ);
            maquina.mFasePrincipal();
          
            for (Carta carta : maquina.getTablero().getCartasMons()) {
                Utilitaria.reemplazar(context, carta, monstruosM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            for (Carta carta : maquina.getTablero().getEspeciales()) {
                Utilitaria.reemplazar(context, carta, especialesM);
                Utilitaria.removerImageView(context, manoM, carta);
            }

            /*

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < jugador.getTablero().getEspeciales().size(); i++) {
                builder.append(i + 1).append(". ") // Agrega el número de la lista
                        .append(jugador.getTablero().getEspeciales().get(i).getNombre())       // Agrega el elemento actual
                        .append("\n");              // Salto de línea después de cada elemento
            }

            Utilitaria.crearDialogs(context,"Context", String.valueOf(jugador.getTablero().getEspeciales().size()),"ok");

             */
        }

        if (fase.equals("Fase Batalla")) {
            Utilitaria.quitarClickListeners(manoJ);
            //faseBatalla(monstruosJ,monstruosM,jugador.getTablero().getCartasMons(),maquina.getTablero().getCartasMons(),jugador,maquina);

            Utilitaria.mostrarDetallesbatalla(context, monstruosJ,monstruosM,especialesJ,especialesM,jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales(),maquina.getTablero().getCartasMons(),maquina.getTablero().getEspeciales(),jugador,maquina);
        }
        
    }

}
