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
    private LinearLayout magicasJ;

    private LinearLayout monstruoJ;
    private LinearLayout magicasM;
    private LinearLayout monstruoM;

    private Deck deck;

    private ImageView selectedCard;
    private ImageView currentSelectedCard = null;
    private Juego juego;
    private TextView J_vida;
    private TextView turno;

    private Jugador j= new Jugador("Juan",this);





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
        magicasJ = findViewById(R.id.magicasJ); // cambie monstruoJ por magicaJ incluso en la funcion del tablero
        monstruoJ= findViewById(R.id.monstruoJ);
        magicasM= findViewById(R.id.magicasM);
        monstruoM= findViewById(R.id.monstruosM);
        J_vida= findViewById(R.id.J_vida);
        turno= (TextView) findViewById(R.id.turno);
        //Utilitaria.cambiarDatosJugaddr(this,j,J_vida);



        //fases();
        //inicializar();
        //Juego juego = new Juego(new Jugador("Alexa",this),this);
        //juego.prueba(manoJugador,monstruosJ);
        //Utilitaria.colocarTablero(this,manoJugador,monstruosJ);


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


    /*
    public void fases(){
        if (fases_J.getText().toString().equals("Fase Tomar Carta"))
            inicializar();

    }
*/

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

    //Esta funcion permite ver todos los detalles de las cartas siendo magicas,trampa o monstruo cuando se coloca en la mano
    //Esta funcion puede ser llamada para cambiar el setOnClickListener de las imagenes cuando esta en la mano
    public void detallesCartaMano(Carta carta) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles de la Carta");

        if (carta instanceof CartaMonstruo) {
            CartaMonstruo c = (CartaMonstruo) carta;
            builder.setMessage(c.toString());
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
        } else if (carta instanceof CartaMagica) {
            CartaMagica c = (CartaMagica) carta;
            builder.setMessage(c.toString());
            // Botones del cuadro de diálogo
            builder.setPositiveButton("Colocar", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada", Toast.LENGTH_SHORT).show();
                // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
            });
            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else if (carta instanceof CartaTrampa) {
            CartaTrampa c = (CartaTrampa) carta;
            builder.setMessage(c.toString());
            // Botones del cuadro de diálogo
            builder.setPositiveButton("Colocar", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada", Toast.LENGTH_SHORT).show();
                // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
            });
            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }



    }




    public void inicializar()
    {

        AssetManager am = this.getAssets();
        try {
            //Jugador j = new Jugador("Alexa",this);
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
                        detallesCartaMano(c);
                    }
                });

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
