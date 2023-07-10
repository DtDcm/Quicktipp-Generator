package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Eurojackpot extends ZahlenLotterie {

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        Random random = new Random();

        List<Integer> zahlen = new ArrayList<Integer>();
        List<Integer> euroZahlen = new ArrayList<Integer>();

        while(zahlen.size() < 5){
            int zufallszahl = random.nextInt(50) + 1;
            if(!zahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)){
                zahlen.add(zufallszahl);
            }
        }

        while(euroZahlen.size() < 2){
            int zufallszahl = random.nextInt(12) + 1;
            if(!euroZahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)){
                euroZahlen.add(zufallszahl);
            }
        }
        zahlen.sort(null);
        euroZahlen.sort(null);

        System.out.println("Quick-Tipp: " + Arrays.toString(zahlen.toArray()) + " + " + Arrays.toString(euroZahlen.toArray()));
    }
}
