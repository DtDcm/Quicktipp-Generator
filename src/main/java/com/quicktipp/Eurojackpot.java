package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eurojackpot extends ZahlenLotterie {

    public void generiereTippreihe(List<Integer> unglückszahlen) {
        List<Integer> zahlen = new ArrayList<Integer>();
        List<Integer> euroZahlen = new ArrayList<Integer>();

        zahlen = generiereZahlen(unglückszahlen, 5, 50);
        euroZahlen = generiereZahlen(unglückszahlen, 2, 12);

        System.out.println("Quick-Tipp: " + Arrays.toString(zahlen.toArray()) + " + " + Arrays.toString(euroZahlen.toArray()));
    }
}
