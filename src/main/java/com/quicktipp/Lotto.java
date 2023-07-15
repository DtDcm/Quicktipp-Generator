package com.quicktipp;

import java.util.Arrays;
import java.util.List;

public class Lotto extends ZahlenLotterie{

    Lotto(){
        zahlenraum = 49;
    }

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        try {
            tippZahlen = generiereZahlen(unglückszahlen, 6, zahlenraum);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(">> Quick-Tipp: " + Arrays.toString(tippZahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= zahlenraum);
    }

    @Override
    public String getLotterieName() {
        return "Lotto";
    }

    @Override
    public int getAnzahlTippZahlen() {
        return 6;
    }
}
