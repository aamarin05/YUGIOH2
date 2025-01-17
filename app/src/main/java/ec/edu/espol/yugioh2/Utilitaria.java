package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


    public static void fasesDialog(Context context, Carta carta, String fase,ImageView imageView,ImageView[] currentSelectedCard){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");
        String boton = "";

        if (fase.equals("Fase Principal")) {
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo c = (CartaMonstruo) carta;
                builder.setMessage(c.toString());
                boton = "Ataque";

                builder.setNegativeButton("Defensa", (dialog, which) -> {
                    currentSelectedCard[0] = imageView;
                    Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                });
            }
            if (carta instanceof CartaMagica) {
                CartaMagica c = (CartaMagica) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
            }
            if (carta instanceof CartaTrampa) {
                CartaTrampa c = (CartaTrampa) carta;
                builder.setMessage(c.toString());
                boton = "Colorcar";
            }
            // Botones del cuadro de diálogo
            builder.setPositiveButton(boton, (dialog, which) -> {
                currentSelectedCard[0] = imageView;
                Toast.makeText(context.getApplicationContext(), "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
            });

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

    public static void cartaView(Context context, Carta carta, LinearLayout contenedor) {
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

    public static void selecMano(Context context, ArrayList<Carta> cartas, LinearLayout mano, ImageView[] currentSelectedCard,String fase){
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
                Utilitaria.fasesDialog(context, carta, fase, imageView, currentSelectedCard);
            });
        }
    }

    public static void selecTablero(Context context, LinearLayout cartasViews, ImageView[] currentSelectedCard){
        for (int i = 0; i< cartasViews.getChildCount(); i++){
            // Referencias a las cartas en el tablero
            ImageView carta = (ImageView) cartasViews.getChildAt(i); // Ajusta según los IDs reales

            carta.setOnClickListener(v -> {
                if (currentSelectedCard[0] != null) {
                    carta.setImageDrawable(currentSelectedCard[0].getDrawable()); // Reemplazar carta
                    currentSelectedCard[0] = null; // Resetear selección
                    Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                    // Hacer que la carta en el tablero sea clickeable para mostrar un nuevo diálogo
                    carta.setOnClickListener(view -> {
                        // Mostrar un nuevo cuadro de diálogo cuando se haga clic en la carta en el tablero
                        new AlertDialog.Builder(context)
                                .setTitle("Detalles de la Carta en el Tablero")
                                .setMessage("Esta es la carta colocada en el tablero. Puedes realizar más acciones aquí.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .setNegativeButton("Acción Adicional", (dialog, which) -> {
                                    // Lógica para una acción adicional con la carta
                                    Toast.makeText(context, "Acción adicional realizada", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .show();
                    });
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

    public static void colocarTablero(Context context,ArrayList<Carta> cartas, LinearLayout mano, LinearLayout cartasViews, String fase){
        final ImageView[] currentSelectedCard = {null};
        selecMano(context,cartas,mano,currentSelectedCard,fase);
        selecTablero(context,cartasViews,currentSelectedCard);
        for (int i = 0; i < cartasViews.getChildCount(); i++) {
            ImageView cartaTablero = (ImageView) cartasViews.getChildAt(i);

            cartaTablero.setOnClickListener(v -> {
                if (currentSelectedCard[0] != null) {
                    // Encuentra y elimina la carta de la mano
                    for (int j = 0; j < mano.getChildCount(); j++) {
                        if (mano.getChildAt(j) == currentSelectedCard[0]) {
                            mano.removeViewAt(j); // Elimina la carta de la mano
                            break;
                        }
                    }
                }
            });
        }

    }

}
