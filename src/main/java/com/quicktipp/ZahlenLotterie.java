package com.quicktipp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Die abstrakte Klasse ZahlenLotterie ist für die zufällige Generierung von Zahlen für eine Tippreihe verantwortlich.
 * Dabei werden bei der Generierung die gegeben Unglückszahlen ausgeschlossen.
 */
public abstract class ZahlenLotterie implements TippreihenGenerator {
    /**
     * Die Methode generiert eine Liste von zufälligen Zahlen für eine Tippreihe unter Berücksichtigung von Unglückszahlen.
     * Die Methode verwendet einen Zufallszahlengenerator, um Zahlen im angegebenen Zahlenraum zu generieren. Die generierten Zahlen 
     * werden aufsteigende Reihenfolge sortiert.
     * 
     * Die Methode überprüft, ob die Anzahl der übergebenen Unglückszahlen bereits eine maximale Anzahl überschreitet, sonst kann keine vollständige Tippreihe generiert werden.
     * Wenn dies der Fall ist, wird eine Fehlermeldung ausgelöst.
     * 
     * @param unglückszahlen Die Liste der Unglückszahlen, die ausgeschlossen werden sollen.
     * @param max Die maximale Anzahl von Zahlen, die generiert werden sollen.
     * @return Eine sortierte Liste von generierten Zahlen für eine Tippreihe unter Berücksichtigung der Unglückszahlen.
     * @throws IllegalStateException Wenn bereits zu viele Unglückszahlen vorhanden sind.
     */
    public List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max) throws IllegalStateException{
        Random random = new Random();
        List<Integer> generierteTippzahlen = new ArrayList<>();

        if(unglückszahlen.size() > getZahlenraum() - getAnzahlTippzahlen()){
            throw new IllegalStateException("Ein interner Fehler ist aufgetreten: Es sind schon zu viele Unglückzahlen.");
        }

        while (generierteTippzahlen.size() < max) {
            int zufallszahl = random.nextInt(getZahlenraum()) + 1;
            if (!generierteTippzahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)) {
                generierteTippzahlen.add(zufallszahl);
            }
        }

        generierteTippzahlen.sort(null);

        return generierteTippzahlen;
    }
}
