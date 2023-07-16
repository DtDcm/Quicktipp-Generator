package com.quicktipp;

import java.util.List;

/**
* Das Interface TippreihenGenerator stellt die Methoden zur Verfügung, um Tippreihen für eine Lotterie zu generieren.
* 
* Die Methoden ermöglichen das Generieren von Zahlen unter Berücksichtigung von Unglückszahlen,
* die Überprüfung, ob eine Zahl gültig ist, das Abrufen des Namens der Lotterie und der Anzahl der Tippzahlen.
*/
public interface TippreihenGenerator {
    List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max);
    void generiereTippreihe(List<Integer> unglückszahlen);
    boolean istGültigeZahl(int zahl);
    String getLotterieName();
    int getAnzahlTippzahlen();
    int getZahlenraum();
}
