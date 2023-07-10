package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Lotto extends ZahlenLotterie{
    public void generiereTippreihe(List<Integer> unglückszahlen){
        Random random = new Random();
        List<Integer> zahlen = new ArrayList<>();

        while (zahlen.size() < 6) {
            int zufallszahl = random.nextInt(49) + 1;
            if (!zahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)) {
                zahlen.add(zufallszahl);
            }
        }

        zahlen.sort(null);

        System.out.println("Quick-Tipp: " + Arrays.toString(zahlen.toArray()));
    }
}
