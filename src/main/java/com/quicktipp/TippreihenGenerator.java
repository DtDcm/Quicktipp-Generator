package com.quicktipp;

import java.util.List;

public interface TippreihenGenerator {
    List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max, int zahlenraum);
    void generiereTippreihe(List<Integer> unglückszahlen);
    boolean istGültigeZahl(int zahl);
    String getLotterieName();
}
