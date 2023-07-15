package com.quicktipp;

import java.util.Arrays;
import java.util.List;

public class Eurojackpot extends ZahlenLotterie {
    List<Integer> euroZahlen;

    Eurojackpot(){
        zahlenraum = 50;
    }

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        try {
            tippZahlen = generiereZahlen(unglückszahlen, 5, zahlenraum);
            euroZahlen = generiereZahlen(unglückszahlen, 2, 10);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(">> Quick-Tipp: " + Arrays.toString(tippZahlen.toArray()) + " + " + Arrays.toString(euroZahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= zahlenraum);
    }

    @Override
    public String getLotterieName() {
        return "Eurojackpot";
    }

    @Override
    public int getAnzahlTippZahlen() {
        return 5;
    }
}
