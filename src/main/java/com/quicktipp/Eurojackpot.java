package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Klasse Eurojackpot implementiert die Generierung von Tippreihen für die Lotterie "Eurojackpot".
 */
public class Eurojackpot extends ZahlenLotterie {
    private static final int ANZAHL_TIPP_ZAHLEN = 5;
    private static final int ZAHLENRAUM = 50;
    private List<Integer> tippzahlen = new ArrayList<>();
    private List<Integer> eurozahlen = new ArrayList<>();

    /**
    * Die Methode generiert eine Tippreihe für die Lotterie "Eurojackpot" unter Berücksichtigung von Unglückszahlen.
    * Nachdem die Tippzahlen erfolgreich generiert wurden, werden sie in der Konsole ausgegeben.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen, die ausgeschlossen werden sollen.
    */
    public void generiereTippreihe(List<Integer> unglückszahlen) {
        try {
            tippzahlen = generiereZahlen(unglückszahlen, getAnzahlTippzahlen());
            eurozahlen = generiereZahlen(unglückszahlen, 2);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(">> Quick-Tipp: " + Arrays.toString(tippzahlen.toArray()) + " + " + Arrays.toString(eurozahlen.toArray()));
    }

    /**
    * Die Methode überprüft, ob eine gegebene Zahl gültig ist für die Lotterie "Eurojackpot".
    * 
    * @param zahl Die zu überprüfende Zahl.
    * @return true, wenn die Zahl gültig ist, ansonsten false.
    */
    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= getZahlenraum());
    }

    /**
    * Die Methode gibt den Lotterienamen "Eurojackpot" zurück.
    * 
    * @return "Eurojackpot"
    */
    @Override
    public String getLotterieName() {
        return "Eurojackpot";
    }

    /**
    * Die Methode gibt die Anzahl der Tippzahlen zurück.
    * 
    * @return ANZAHL_TIPP_ZAHLEN
    */
    @Override
    public int getAnzahlTippzahlen() {
        return ANZAHL_TIPP_ZAHLEN;
    }

    /**
    * Die Methode gibt den Zahlenraum zurück.
    * 
    * @return ZAHLENRAUM
    */
    @Override
    public int getZahlenraum() {
        return ZAHLENRAUM;
    }

    /**
    * Die Methode gibt die Tippzahlen zurück.
    * 
    * @return tippzahlen
    */
    public List<Integer> getTippzahlen() {
        return tippzahlen;
    }

    /**
    * Die Methode gibt die Eurozahlen zurück.
    * 
    * @return eurozahlen
    */
    public List<Integer> getEurozahlen() {
        return eurozahlen;
    }

}
