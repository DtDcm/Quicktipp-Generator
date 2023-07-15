package com.quicktipp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ZahlenLotterie implements TippreihenGenerator {
    List<Integer> tippZahlen;
    int zahlenraum;

    public List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max, int zahlenraum) throws IllegalStateException{
        Random random = new Random();
        List<Integer> generierteTippZahlen = new ArrayList<>();

        if(unglückszahlen.size() > getZahlenraum() - getAnzahlTippZahlen()){
            throw new IllegalStateException("Ein interner Fehler ist aufgetreten: Es sind schon zu viele Unglückzahlen.");
        }

        while (generierteTippZahlen.size() < max) {
            int zufallszahl = random.nextInt(zahlenraum) + 1;
            if (!generierteTippZahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)) {
                generierteTippZahlen.add(zufallszahl);
            }
        }

        generierteTippZahlen.sort(null);

        return generierteTippZahlen;
    }

    @Override
    public int getZahlenraum() {
        return zahlenraum;
    }
}
