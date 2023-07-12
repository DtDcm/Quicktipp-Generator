package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eurojackpot extends ZahlenLotterie {
    int max = 50;

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        List<Integer> zahlen = new ArrayList<Integer>();
        List<Integer> euroZahlen = new ArrayList<Integer>();

        zahlen = generiereZahlen(unglückszahlen, 5, max);
        euroZahlen = generiereZahlen(unglückszahlen, 2, 10);

        System.out.println(">> Quick-Tipp: " + Arrays.toString(zahlen.toArray()) + " + " + Arrays.toString(euroZahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= max);
    }
}
