package ec.edu.espol.yugioh2;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Carta> cartas;

    public Deck(ArrayList<Carta> cartas){
        this.cartas = cartas;
    }
    public ArrayList<Carta> getCartas(){
        return cartas;
    }

    public static Deck crearDeck(AssetManager am)throws IOException{
        ArrayList<CartaMonstruo> monstruos = new ArrayList<>();
        ArrayList<CartaMagica> magicas = new ArrayList<>();
        ArrayList<CartaTrampa> trampas = new ArrayList<>();
        ArrayList<Carta> cartas = new ArrayList<>();

        BufferedReader bf = new BufferedReader(new InputStreamReader(am.open("CartasCreadas.txt")));
            String line;
            while ((line = bf.readLine()) != null){
                String[] arr = line.split(",");
                if ("CartaMonstruo".equals(arr[0])){
                    CartaMonstruo c = new CartaMonstruo(arr[1], arr[2], Orientacion.valueOf(arr[4].split("\\.")[arr[4].split("\\.").length-1]), TipoMonstruo.valueOf(arr[5].split("\\.")[arr[5].split("\\.").length-1]), TipoAtributo.valueOf(arr[6].split("\\.")[arr[6].split("\\.").length-1]), Integer.parseInt(arr[7]), Integer.parseInt(arr[8]),arr[9]);
                    monstruos.add(c);
                }
                if ("CartaMagica".equals(arr[0])){
                    CartaMagica c = new CartaMagica(arr[1], arr[2],Posicion.valueOf(arr[3].split("\\.")[arr[3].split("\\.").length-1]), Orientacion.valueOf(arr[4].split("\\.")[arr[4].split("\\.").length-1]), Integer.parseInt(arr[5]),Integer.parseInt(arr[6]),TipoMonstruo.valueOf(arr[7].split("\\.")[arr[7].split("\\.").length-1]),arr[8]);
                    magicas.add(c);
                }
                if ("CartaTrampa".equals(arr[0])){
                    CartaTrampa c = new CartaTrampa(arr[1], arr[2],Posicion.valueOf(arr[3].split("\\.")[arr[3].split("\\.").length-1]), Orientacion.valueOf(arr[4].split("\\.")[arr[4].split("\\.").length-1]), TipoAtributo.valueOf(arr[5].split("\\.")[arr[5].split("\\.").length-1]),arr[6]);
                    trampas.add(c);
                }
            }

        if (monstruos.size() > 10 && magicas.size() > 6 && trampas.size() > 6){
            for (int i = 0; i<10; i ++){
                cartas.add(monstruos.get(i));
            }
            for (int i = 0; i<5; i ++){
                cartas.add(magicas.get(i));
                cartas.add(trampas.get(i));
            }
        }
        Collections.shuffle(cartas);
        return new Deck(cartas);
    }

}
