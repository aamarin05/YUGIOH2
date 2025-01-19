package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Utilitaria {

    public static void crearDialogs(Context context, String titulo, String descripcion, String msj) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(descripcion)
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    public static void fasesDialog(Context context, Carta carta, String fase, ImageView imageView, ImageView[] currentSelectedCard, ArrayList<Carta> mano,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");
        String boton = "";

        if (fase.equals("Fase Principal")) {
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo c = (CartaMonstruo) carta;
                builder.setMessage(c.toString());
                boton = "Ataque";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    mano.remove(c);
                    tableroM.add(c);
                    currentSelectedCard[0] = imageView;
                    Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);

                });
                builder.setNegativeButton("Defensa", (dialog, which) -> {
                    mano.remove(c);
                    tableroM.add(c);
                    currentSelectedCard[0] = imageView;
                    Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    c.setOrientacion(Orientacion.ABAJO);
                });
            }
            if (carta instanceof CartaMagica) {
                CartaMagica c = (CartaMagica) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    tableroE.add(c);
                    mano.remove(c);
                    currentSelectedCard[0] = imageView;
                    Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);

                });
            }
            if (carta instanceof CartaTrampa) {
                CartaTrampa c = (CartaTrampa) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    tableroE.add(c);
                    mano.remove(c);
                    currentSelectedCard[0] = imageView;
                    Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);

                });
            }
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
                carta.getImagen(), "drawable", context.getPackageName()
        );

        // Obtener la imagen "no hay carta" para identificar los espacios vacíos
        int noHayCartaId = context.getResources().getIdentifier(
                "no_hay_carta", "drawable", context.getPackageName()
        );
        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);

        ImageView cartaSeleccionada = null;

        // Recorre todos los elementos del contenedor
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            ImageView imageView = (ImageView) contenedor.getChildAt(i);
            Drawable currentDrawable = imageView.getDrawable();

            // Si el ImageView tiene la imagen "no hay carta", es un espacio vacío
            if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                cartaSeleccionada = imageView;
            }
        }

        if (cartaSeleccionada != null) {
            // Obtener el ID del contenedor (monstruos o magicas)
            String contenedorId = contenedor.getResources().getResourceEntryName(contenedor.getId());

            // Verificar el tipo de la carta
            String tipoCarta = carta.getClass().getSimpleName(); // Obtiene el tipo de carta (e.g., "CartaMonstruo", "CartaMagica", "CartaTrampa")

            // Verificar si el contenedor es para monstruos y la carta no es monstruo
            if (contenedorId.contains("monstruosJ") && !tipoCarta.equals("CartaMonstruo")) {
                Utilitaria.crearDialogs(context, "Error", "No se puede colocar una carta que no sea monstruo en el área de monstruos.", "OK");
                return;  // Salir de la función para evitar que la carta se coloque
            }

            // Verificar si el contenedor es para cartas mágicas/trampa y la carta no es mágica/trampa
            if (contenedorId.contains("magicasJ") && (tipoCarta.equals("CartaMonstruo"))) {
                Utilitaria.crearDialogs(context, "Error", "No se puede colocar una carta de monstruo en el área de cartas mágicas/trampa.", "OK");
                return;  // Salir de la función para evitar que la carta se coloque
            }

            // Reemplazar la carta en el contenedor adecuado
            cartaSeleccionada.setImageResource(imagenId);
            cartaSeleccionada.setTag(carta.getImagen());  // Actualiza el tag con el nombre de la carta
        }
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

    public static void selecCarta1(Context context, ArrayList<Carta> cartas, LinearLayout mano, ImageView[] currentSelectedCard,String fase,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE){
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
                Utilitaria.fasesDialog(context, carta, fase, imageView, currentSelectedCard,cartas,tableroM,tableroE);
            });
        }
    }

    public static void selecTablero(Context context, LinearLayout mano, LinearLayout monstruosJ, ImageView[] currentSelectedCard,ArrayList<Carta> cartas){
        for (int i = 0; i< monstruosJ.getChildCount(); i++){
            // Referencias a las cartas en el tablero
            ImageView carta = (ImageView) monstruosJ.getChildAt(i); // Ajusta según los IDs reales
            String cartaTag = (String) carta.getTag();
            Carta c = buscarCarta(cartas,cartaTag);


            carta.setOnClickListener(v -> {
                if (currentSelectedCard[0] != null) {
                    mano.removeView(currentSelectedCard[0]);
                    carta.setImageDrawable(currentSelectedCard[0].getDrawable()); // Reemplazar carta
                    currentSelectedCard[0] = null; // Resetear selección
                    cartas.remove(c);
                    Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
                }
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

    public static void selecCarta2(Context context,ArrayList<Carta> listaCartas, LinearLayout monstruosM, ImageView[] currentSelectedCard){
        for (int i = 0; i< monstruosM.getChildCount(); i++){
            // Referencias a las cartas en el tablero
            ImageView carta = (ImageView) monstruosM.getChildAt(i); // Ajusta según los IDs reales

            carta.setOnClickListener(v -> {
                if (currentSelectedCard[0] != null) {
                    Utilitaria.imageViewCarta(currentSelectedCard[0],listaCartas,context); // Reemplazar carta
                    currentSelectedCard[0] = null; // Resetear selección
                    Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void colocarTablero(Context context,ArrayList<Carta> cartas, LinearLayout mano, LinearLayout monstruosJ,LinearLayout especialesJ, String fase,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE){
        final ImageView[] currentSelectedCard = {null};
        selecCarta1(context,cartas,mano,currentSelectedCard,fase,tableroM,tableroE);
        //if (currentSelectedCard[0] != null)
        //{
            //Carta carta = Utilitaria.imageViewCarta(currentSelectedCard[0], cartas, context);

            //if (carta instanceof CartaMonstruo)
                selecTablero(context, mano, monstruosJ, currentSelectedCard, cartas);
            //else
              selecTablero(context, mano, especialesJ, currentSelectedCard, cartas);

        //}
        //else
          //  Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
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
       // selecCarta1(context,monstruosJ,monstruosA,currentSelectedCard,fase,table);
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

    public static ArrayList<Carta> leerImagenesLayout(Context context,LinearLayout contenedor, ArrayList<Carta> cartas) {
        ArrayList<Carta> cartasContenedor = new ArrayList<>();
        for (int i = 0; i < contenedor.getChildCount(); i++) {

            ImageView imageView = (ImageView) contenedor.getChildAt(i); // Ajusta según el ID real de la carta

            Carta carta = Utilitaria.buscarCarta(cartas, (String) imageView.getTag());
            cartasContenedor.add(carta);
        }
        return cartasContenedor;

    }

    public static void quitarCartas(Context context,LinearLayout contenedor, ArrayList<Carta> cartasAtributoM,ArrayList<Carta> cartasAtributoE){
        //MANO Y MANO
        // EN EL CONTENEDOR ESTÁN ESTÁ LA CARTA1 Y CARTA2
        //EN EL ATRIBUTO ESTÁN LAS CARTAS 1, 2, 3, 4
        ArrayList<Carta> cartasContenedor = leerImagenesLayout(context,contenedor,cartasAtributoE);


        for (Carta carta: cartasContenedor){
            if (carta instanceof CartaMonstruo)
                cartasAtributoM.add(carta);
            if (carta instanceof CartaTrampa || carta instanceof  CartaMagica)
                cartasAtributoE.add(carta);
        }

    }

    public static void removerImageView(Context context,LinearLayout mano, Carta carta){
        int imagenId = context.getResources().getIdentifier(carta.getImagen(), "drawable", context.getPackageName());

        if (imagenId == 0) {
            // Si la carta no tiene un drawable válido, no hacer nad
            return;
        }

        // Obtener el drawable de la carta específica
        Drawable drawableCarta = context.getResources().getDrawable(imagenId);
        ImageView cartaSeleccionada = null;

        // Recorre todos los elementos del LinearLayout
        for (int i = 0; i < mano.getChildCount(); i++) {
            ImageView imageView = (ImageView) mano.getChildAt(i);

                // Comparar el drawable del ImageView con el de la carta
            Drawable currentDrawable = imageView.getDrawable();
            if (currentDrawable != null && currentDrawable.getConstantState().equals(drawableCarta.getConstantState())) {
                cartaSeleccionada = imageView;
                // Continúa recorriendo para garantizar que solo la última coincidencia (si hay varias) será seleccionada
            }
        }
        // Si encontró el último ImageView correspondiente, eliminarlo
        if (cartaSeleccionada != null) {
            mano.removeView(cartaSeleccionada);
        }
    }
}
