package com.quicktipp;

import java.util.List;

/**
* Das Interface TippreihenGenerator stellt die Methoden zur Verfügung, um Tippreihen für eine Lotterie zu generieren.
* 
* Die Methoden ermöglichen das Generieren von Zahlen unter Berücksichtigung von Unglückszahlen,
* die Überprüfung, ob eine Zahl gültig ist, das Abrufen des Namens der Lotterie und der kleinsten Anzahl der Tippzahlen.
*/
public interface TippreihenGenerator {
    List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max, int zahlenraum);
    void generiereTippreihe(List<Integer> unglückszahlen);
    boolean istGültigeZahl(int zahl);
    boolean istGenerierungMöglich(List<Integer> unglückszahlen);
    String getLotterieName();
    int getAnzahlTippzahlen();
    int getZahlenraum();
}
