package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lotto extends ZahlenLotterie{
    int max = 49;

    public void generiereTippreihe(List<Integer> unglückszahlen){
        List<Integer> zahlen = new ArrayList<>();
        zahlen = generiereZahlen(unglückszahlen, 6, 49);

        System.out.println(">> Quick-Tipp: " + Arrays.toString(zahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= max);
    }
}
