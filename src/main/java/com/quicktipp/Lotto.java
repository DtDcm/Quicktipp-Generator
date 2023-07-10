package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lotto extends ZahlenLotterie{
    public void generiereTippreihe(List<Integer> unglückszahlen){
        List<Integer> zahlen = new ArrayList<>();
        zahlen = generiereZahlen(unglückszahlen, 6, 49);

        System.out.println("Quick-Tipp: " + Arrays.toString(zahlen.toArray()));
    }
}
