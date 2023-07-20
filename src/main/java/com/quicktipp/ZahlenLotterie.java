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
    public List<Integer> generiereZahlen(List<Integer> unglückszahlen, int max, int zahlenraum) throws IllegalStateException{
        Random random = new Random();
        List<Integer> generierteTippzahlen = new ArrayList<>();

        if(!istGenerierungMöglich(unglückszahlen)){
            throw new IllegalStateException("\n>> Es konnte keine vollstädige Tippreihe für " + getLotterieName() + " generiert werden, aufgrund der ausgeschlossenen Zahlen."
            + "\n>> Bitte löschen Sie Unglückzahlen, die Sie nicht mehr verwenden wollen.");
        }

        while (generierteTippzahlen.size() < max) {
            int zufallszahl = random.nextInt(zahlenraum) + 1;
            if (!generierteTippzahlen.contains(zufallszahl) && !unglückszahlen.contains(zufallszahl)) {
                generierteTippzahlen.add(zufallszahl);
            }
        }

        generierteTippzahlen.sort(null);

        return generierteTippzahlen;
    }

    /**
    * Die Methode überprüft, ob eine gegebene Zahl gültig für den angegebenen Zahlenraum.
    * 
    * @param zahl Die zu überprüfende Zahl.
    * @return true, wenn die Zahl gültig ist, ansonsten false.
    */
    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= getZahlenraum());
    }
}
