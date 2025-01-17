package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView fases_M;
    private TextView fases_J;
    private LinearLayout manoJugador;
    private LinearLayout monstruosJ;

    private Deck deck;

    private ImageView selectedCard;
    private ImageView currentSelectedCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fases_M = (TextView) findViewById(R.id.fases_M);
        fases_J = (TextView) findViewById(R.id.fases_J);
        manoJugador= findViewById(R.id.manoJugador);
        monstruosJ = findViewById(R.id.monstruosJ);


        //fases();
        inicializar();
        colocarTablero();

        //Se define un boton con el ID y se crea una variable
        Button btnCambiarFase = findViewById(R.id.boton_cambiar_fase); //se agrega el boton con el ID
        // Agrega un OnClickListener al botón
        btnCambiarFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí va la lógica para cambiar de fase
                cambiarFase(fases_J);
                cambiarFase(fases_M);
            }

        });

    }
    public void fases(){
        if (fases_J.getText().toString().equals("Fase Tomar Carta"))
            inicializar();
        if (fases_J.getText().toString().equals("Fase Principal"))
            colocarTablero();
    }

    // Metodo para cambiar de fase

    private void cambiarFase(TextView j) {
        // Lógica para cambiar de fase
        String faseActual = j.getText().toString();
        String nuevaFase = "";
        if (faseActual.equals("Fase Tomar Carta")) {
            nuevaFase = "Fase Principal";
        }
        else if (faseActual.equals("Fase Principal")){
            nuevaFase = "Fase Batalla";
        }
        else if (faseActual.equals("Fase Batalla")){
            nuevaFase = "Fase Tomar Carta";
        }
        Toast.makeText(this, "Fase cambiada", Toast.LENGTH_SHORT).show();//muestra un pequeño mensaje que la fase se ha cambiado y luego se elimina
        j.setText(nuevaFase);


    }
    public void mostrarDetallesCarta(Carta carta) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles de la Carta");

        if (carta instanceof CartaMonstruo) {
            CartaMonstruo c = (CartaMonstruo) carta;
            builder.setMessage(c.toString());
        } else if (carta instanceof CartaMagica) {
            CartaMagica c = (CartaMagica) carta;
            builder.setMessage(c.toString());
        } else if (carta instanceof CartaTrampa) {
            CartaTrampa c = (CartaTrampa) carta;
            builder.setMessage(c.toString());
        }

        // Botones del cuadro de diálogo
        builder.setPositiveButton("Ataque", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Carta colocada en Ataque", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
        });

        builder.setNegativeButton("Defensa", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Carta colocada en Defensa", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en defensa (guardar el estado, etc.)
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();

    }
    public void inicializar()
    {
        AssetManager am = this.getAssets();
        try {
            //Jugador j = new Jugador("Alexa");
            Deck deck= Deck.crearDeck(am);
            ArrayList<Carta> cartas= deck.getCartas();
            //ArrayList<Carta> cartas= j.getMano();

            for(Carta c : cartas)
            {
                ImageView imv = new ImageView(this);
                Resources resources = getResources();
                int rid = resources.getIdentifier(c.getImagen(),"drawable",getPackageName());
                imv.setImageResource(rid);
                manoJugador.addView(imv);
                //imv.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                imv.getLayoutParams().width = 250;
                imv.setPadding(10,0,10,0);
                imv.setScaleType(ImageView.ScaleType.FIT_XY);
                imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarDetallesCarta(c);
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void colocarTablero(){
        final ImageView[] currentSelectedCard = {null};

        for (int i = 0; i< manoJugador.getChildCount(); i++) {

            ImageView imageView = (ImageView) manoJugador.getChildAt(i); // Ajusta según el ID real de la carta

            imageView.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("¿Colocar carta en el tablero?")
                        .setMessage("¿Quieres colocar esta carta en el tablero?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            currentSelectedCard[0] = imageView; // Almacenar la carta seleccionada
                            Toast.makeText(this, "Ahora selecciona una carta del tablero para reemplazarla.", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
        }
        for (int i = 0; i< monstruosJ.getChildCount(); i++){
            // Referencias a las cartas en el tablero
            ImageView monstruo = (ImageView) monstruosJ.getChildAt(i); // Ajusta según los IDs reales

            monstruo.setOnClickListener(v -> {
                if (currentSelectedCard[0] != null) {
                    monstruo.setImageDrawable(currentSelectedCard[0].getDrawable()); // Reemplazar carta
                    currentSelectedCard[0] = null; // Resetear selección
                    Toast.makeText(this, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
