package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Utilitaria {

    public static void crearDialogs(Context context, String titulo, String descripcion, String msj){
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(descripcion)
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }
    //public static void fasesDialog(Context context, Carta carta, String fase,ImageView imageView,ImageView[] currentSelectedCard){
    public static void fasesDialog(Context context, Carta carta, String fase,LinearLayout monstruosJ,LinearLayout especialesJ){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");
        String boton = "";

        if (fase.equals("Fase Principal")) {
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo c = (CartaMonstruo) carta;
                builder.setMessage(c.toString());
                boton = "Ataque";

                builder.setPositiveButton(boton, (dialog, which) -> {
                    reemplazar(context,carta,monstruosJ);
                    Toast.makeText(context.getApplicationContext(), c.toString(), Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                });

                builder.setNegativeButton("Defensa", (dialog, which) -> {
                    reemplazar(context,carta,monstruosJ);
                    //Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                });
            }
            if (carta instanceof CartaMagica || carta instanceof CartaTrampa) {
                //CartaMagica c = (CartaMagica) carta;
                builder.setMessage(carta.toString());
                boton = "Colocar";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    reemplazar(context,carta,especialesJ);
                    Toast.makeText(context.getApplicationContext(), carta.toString(), Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                });
            }
            /*
            if (carta instanceof CartaTrampa) {
                CartaTrampa c = (CartaTrampa) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
            }

             */
            // Botones del cuadro de diálogo

            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        }
        if (fase.equals("Fase Batalla")) {
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo c = (CartaMonstruo) carta;
                builder.setMessage(c.toString());
                boton = "Declarar Batalla";
            }
            if (carta instanceof CartaMagica) {
                CartaMagica c = (CartaMagica) carta;
                builder.setMessage(c.toString());
                boton = "Usar Magica";
            }
            // Botones del cuadro de diálogo
            builder.setPositiveButton(boton, (dialog, which) -> {
                Toast.makeText(context.getApplicationContext(), "Carta usada", Toast.LENGTH_SHORT).show();
                // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        }

        builder.show();

    }

    public static void crearyAgregar(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(),"drawable",context.getPackageName()
        );
        ImageView cartaView = new ImageView(context);
        cartaView.setImageResource(imagenId);
        cartaView.setTag(carta.getImagen());
        contenedor.addView(cartaView);
        cartaView.getLayoutParams().width = 250;
        cartaView.setPadding(10,0,10,0);
        cartaView.setScaleType(ImageView.ScaleType.FIT_XY);

/*
        if (imagenId != 0) {
            cartaView.setImageResource(imagenId);
        } else {
            cartaView.setImageResource(R.drawable.no_hay_carta);
        }

        cartaView.setOnClickListener(view -> fasesDialog(context, carta,fase));
    */


    }

    public static void reemplazar(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(),"drawable",context.getPackageName()
        );
        int noHayCartaId = context.getResources().getIdentifier(
                "no_hay_carta", "drawable", context.getPackageName()
        );

        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);
        ImageView cartaSeleccionada = null;
        Drawable cartaView = context.getResources().getDrawable(imagenId);


        for (int i = 0; i < contenedor.getChildCount(); i++) {
            ImageView imageView = (ImageView) contenedor.getChildAt(i); // Ajusta según el ID real de la carta
            Drawable currentDrawable = imageView.getDrawable();

            if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                cartaSeleccionada = imageView;
            }
        }
        if (cartaSeleccionada != null) {
            //cartaSeleccionada.setImageDrawable(cartaView); // Reemplazar carta
            cartaSeleccionada.setImageResource(imagenId);
            cartaSeleccionada.setTag(carta.getImagen());
        }
            /*
            //Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
        else {
            //Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
        }

             */
    }
/*
    public static void cartaViewM(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(), "drawable", context.getPackageName()
        );

        int noHayCartaId = context.getResources().getIdentifier(
                "no_hay_carta", "drawable", context.getPackageName()
        );

        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);
        Drawable cartaCambiar = context.getResources().getDrawable(imagenId);

        // Buscar si existe un ImageView con la imagen "no_hay_carta"
        ImageView cartaSeleccionada = null;
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            View child = contenedor.getChildAt(i);
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;

                // Verificar si el Drawable actual del ImageView es "no_hay_carta"
                Drawable currentDrawable = imageView.getDrawable();
                if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                    cartaSeleccionada = imageView;
                }
            }
        }

        if (cartaSeleccionada != null) {
            //cartaSeleccionada.setImageDrawable(cartaCambiar);
            //cartaSeleccionada.setImageDrawable(noHayCartaDrawable);
            cartaSeleccionada.setImageResource(imagenId);
            //cartaSeleccionada.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            // Crear un nuevo ImageView si no existe "no_hay_carta"
            ImageView cartaView = new ImageView(context);
            cartaView.setImageResource(imagenId);
            cartaView.setTag(carta.getImagen());
            contenedor.addView(cartaView);
            cartaView.getLayoutParams().width = 250;
            cartaView.setPadding(10, 0, 10, 0);
            cartaView.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

 */

    public static void selecCarta1(Context context, ArrayList<Carta> cartas, LinearLayout mano, ImageView[] currentSelectedCard,String fase){
        for (int i = 0; i < mano.getChildCount(); i++) {

            ImageView imageView = (ImageView) mano.getChildAt(i); // Ajusta según el ID real de la carta

            Carta carta = Utilitaria.buscarCarta(cartas, (String) imageView.getTag());
            imageView.setOnClickListener(v -> {
                    /*
                    new AlertDialog.Builder(context)
                            .setTitle("¿Colocar carta en el tablero?")
                            .setMessage("¿Quieres colocar esta carta en el tablero?")
                            .setPositiveButton("OK", (dialog, which) -> {
                                currentSelectedCard[0] = imageView; // Almacenar la carta seleccionada
                                Toast.makeText(context, "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                     */
                currentSelectedCard[0] = imageView;
                //Utilitaria.fasesDialog(context, carta, fase, imageView, currentSelectedCard);
            });
        }
    }

    private static Carta buscarCarta(ArrayList<Carta> cartas, String nombre) {
        for (Carta carta : cartas) {
            if (carta.getImagen().equals(nombre)) {
                return carta;
            }
        }
        return null; // En caso de no encontrar la carta
    }

    public static Carta imageViewCarta(ImageView imageView, ArrayList<Carta> listaCartas, Context context) {
        // Obtener el Drawable del ImageView
        Drawable drawableImageView = imageView.getDrawable();

        if (drawableImageView == null) {
            return null; // Si el ImageView no tiene imagen, no hay carta asociada
        }
        // Iterar sobre el ArrayList de cartas para encontrar la que corresponde al Drawable
        for (Carta carta : listaCartas) {
            // Obtener el recurso Drawable de la carta basado en su atributo "imagen"
            int imagenId = context.getResources().getIdentifier(
                    carta.getImagen(), "drawable", context.getPackageName()
            );

            if (imagenId != 0) { // Asegurarse de que el recurso exista
                Drawable drawableCarta = context.getResources().getDrawable(imagenId);

                // Comparar si los Drawables son iguales
                if (drawableImageView.getConstantState().equals(drawableCarta.getConstantState())) {
                    return carta; // Devolver la carta asociada
                }
            }
        }
        return null; // No se encontró ninguna carta asociada
    }

    public static void colocarTablero(Context context,ArrayList<Carta> cartas, LinearLayout mano, LinearLayout monstruosJ,LinearLayout especialesJ, String fase){
        final ImageView[] currentSelectedCard = {null};
        for (int i = 0; i < mano.getChildCount(); i++) {

            ImageView imageView = (ImageView) mano.getChildAt(i); // Ajusta según el ID real de la carta

            Carta carta = Utilitaria.buscarCarta(cartas, (String) imageView.getTag());
            imageView.setOnClickListener(v -> {
                    /*
                    new AlertDialog.Builder(context)
                            .setTitle("¿Colocar carta en el tablero?")
                            .setMessage("¿Quieres colocar esta carta en el tablero?")
                            .setPositiveButton("OK", (dialog, which) -> {
                                currentSelectedCard[0] = imageView; // Almacenar la carta seleccionada
                                Toast.makeText(context, "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                     */
                currentSelectedCard[0] = imageView;
               // Utilitaria.fasesDialog(context, carta, fase, imageView, currentSelectedCard);
            });
        }

        Carta carta = Utilitaria.imageViewCarta(currentSelectedCard[0], cartas, context);
        if (carta instanceof CartaMonstruo) {
            reemplazar(context, carta, monstruosJ);
            Toast.makeText(context, "MONSRTEUO", Toast.LENGTH_SHORT).show();
        }
        else if (carta instanceof CartaMagica || carta instanceof CartaTrampa)
          //selecTablero(context, mano, especialesJ, currentSelectedCard);
        {
            reemplazar(context, carta, especialesJ);
            Toast.makeText(context, "ESPECIAL", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "NO HAY CARTA", Toast.LENGTH_SHORT).show();


        //else
       //   Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
    }
/*
    public static ArrayList<Carta> declararBatalla(Context context,ArrayList<Carta> monstruosJ,ArrayList<Carta> monstruosM, LinearLayout monstruosA, LinearLayout monstruosO, String fase){
        final ImageView[] currentSelectedCard = {null};
        selecCarta1(context,monstruosJ,monstruosA,currentSelectedCard,fase);
        Carta atacante = Utilitaria.imageViewCarta(currentSelectedCard[0],monstruosJ,context);
        selecCarta1(context,monstruosM,monstruosO,currentSelectedCard,fase);
        Carta oponente = Utilitaria.imageViewCarta(currentSelectedCard[0],monstruosJ,context);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(atacante);
        cartas.add(oponente);
        return cartas;
    }

 */
    public static void declararBatalla(Context context,ArrayList<Carta> monstruosJ,ArrayList<Carta> monstruosM, LinearLayout monstruosA, LinearLayout monstruosO, String fase){
        final ImageView[] currentSelectedCard = {null};
        selecCarta1(context,monstruosJ,monstruosA,currentSelectedCard,fase);
        //Utilitaria.imageViewCarta(currentSelectedCard[0],monstruosJ,context);
        //Carta atacante = Utilitaria.imageViewCarta(currentSelectedCard[0],monstruosJ,context);
        /*
        selecCarta1(context,monstruosM,monstruosO,currentSelectedCard,fase);
        Carta oponente = Utilitaria.imageViewCarta(currentSelectedCard[0],monstruosJ,context);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(atacante);
        cartas.add(oponente);

         */
    }

    //funcion que cambia el texto del view de la vida por el nombre y los puntos de vida
    public static void vidaJugadorView(Jugador j,TextView textvida)
    {
        textvida.setText("LP "+j.getNombre()+": "+j.getPuntos());
    }
    //funcion que cambia el texto del view de turnos por el asignado
    public static void cambiarturnoView(int turno, TextView textturno)
    {
        String texto= ""+turno;
        textturno.setText("Turno: "+ texto);
    }

    public static void cartaTablero(Context context, LinearLayout mano, LinearLayout monstruosJ, LinearLayout especialesJ, ArrayList<Carta> cartas,String fase) {
        for (int i = 0; i < mano.getChildCount(); i++) {
            ImageView cardView = (ImageView) mano.getChildAt(i);

            String nombreCarta = (String) cardView.getTag();

            // Buscar la carta en el ArrayList
            Carta carta = Utilitaria.buscarCarta(cartas, nombreCarta);

            if (carta == null) {
                Toast.makeText(context, "Carta no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }
                cardView.setOnClickListener(v -> {
                    fasesDialog(context,carta,fase,monstruosJ,especialesJ);

                    /*

                    // Mostrar diálogo para colocar la carta
                    new AlertDialog.Builder(context)
                            .setTitle("Colocar carta")
                            .setMessage("¿Quieres colocar esta carta en el campo?")
                            .setPositiveButton("Sí", (dialog, which) -> {

                                if (carta instanceof CartaMonstruo) {
                                    reemplazar(context, carta, monstruosJ);
                                    Toast.makeText(context, carta.toString(), Toast.LENGTH_SHORT).show();

                                    //enablePlacement(context, cardView, monstruosJ, "monstruos");
                                }

                                if (carta instanceof CartaTrampa || carta instanceof CartaMagica) {
                                    reemplazar(context,carta,especialesJ);
                                    Toast.makeText(context, carta.toString(), Toast.LENGTH_SHORT).show();
                                    //enablePlacement(context, cardView, especialesJ, "especiales");
                               }

                            })
                            .setNegativeButton("No", null)
                            .show();

                     */
                });
            }

        }
}
