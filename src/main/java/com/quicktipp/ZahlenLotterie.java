package com.quicktipp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ZahlenLotterie implements TippreihenGenerator {

    List<Integer> zahlen;

    public abstract void generiereTippreihe(List<Integer> unglückszahlen);
    
    public abstract boolean istGültigeZahl(int zahl);
    
    public abstract String getLotterieName();

    public List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max, int zahlenraum){
        Random random = new Random();
        List<Integer> zahlen = new ArrayList<>();

        while (zahlen.size() < max) {
            int zufallszahl = random.nextInt(zahlenraum) + 1;
            if (!zahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)) {
                zahlen.add(zufallszahl);
            }
        }

        zahlen.sort(null);

        return zahlen;
    }
}
