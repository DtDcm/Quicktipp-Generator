package com.quicktipp;

import java.util.Arrays;
import java.util.List;

public class Eurojackpot extends ZahlenLotterie {
    List<Integer> euroZahlen;

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        zahlen = generiereZahlen(unglückszahlen, 5, 50);
        euroZahlen = generiereZahlen(unglückszahlen, 2, 10);

        System.out.println(">> Quick-Tipp: " + Arrays.toString(zahlen.toArray()) + " + " + Arrays.toString(euroZahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= 50);
    }

    @Override
    public String getLotterieName() {
        return "Eurojackpot";
    }
}
