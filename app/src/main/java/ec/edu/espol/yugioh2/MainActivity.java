package ec.edu.espol.yugioh2;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView fases_M;
    private TextView fases_J;
  
    LinearLayout manoJugador;

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

    manoJugador=findViewById(R.id.manoJugador);

    }

}
