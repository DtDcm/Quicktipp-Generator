package com.quicktipp;

import java.util.Arrays;
import java.util.List;

public class Lotto extends ZahlenLotterie{

    public void generiereTippreihe(List<Integer> unglückszahlen){
        
        zahlen = generiereZahlen(unglückszahlen, 6, 49);

        System.out.println(">> Quick-Tipp: " + Arrays.toString(zahlen.toArray()));
    }

    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= 49);
    }

    @Override
    public String getLotterieName() {
        return "Lotto";
    }
}
