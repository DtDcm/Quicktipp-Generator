package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Klasse Eurojackpot implementiert die Generierung von Tippreihen für die Lotterie "Eurojackpot".
 */
public class Eurojackpot extends ZahlenLotterie {
    private final int ANZAHL_TIPP_ZAHLEN = 5;
    private final int ANZAHL_EURO_ZAHLEN = 2;
    private final int ZAHLENRAUM = 50;
    private final int EURO_ZAHLENRAUM = 10;
    private List<Integer> tippzahlen = new ArrayList<>();
    private List<Integer> eurozahlen = new ArrayList<>();

    /**
    * Die Methode generiert eine Tippreihe für die Lotterie "Eurojackpot" unter Berücksichtigung von Unglückszahlen.
    * Nachdem die Tippzahlen erfolgreich generiert wurden, werden sie in der Konsole ausgegeben.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen, die ausgeschlossen werden sollen.
    * @throws IllegalStateException Wenn bereits zu viele Unglückszahlen vorhanden sind.
    */
    public void generiereTippreihe(List<Integer> unglückszahlen) throws IllegalStateException {
        tippzahlen = generiereZahlen(unglückszahlen, ANZAHL_TIPP_ZAHLEN, ZAHLENRAUM);
        eurozahlen = generiereZahlen(unglückszahlen, ANZAHL_EURO_ZAHLEN, EURO_ZAHLENRAUM);

        System.out.println("\n>> Quick-Tipp: " + Arrays.toString(tippzahlen.toArray()) + " + " + Arrays.toString(eurozahlen.toArray()));
    }

    /**
    * Die Methode überprüft, ob eine gegebene Zahl eine gültige Eurozahl ist für die Lotterie "Eurojackpot".
    * 
    * @param zahl Die zu überprüfende Zahl.
    * @return true, wenn die Zahl gültig ist, ansonsten false.
    */
    public boolean istGültigeEurozahl(int zahl) {
        return (zahl > 0 && zahl <= EURO_ZAHLENRAUM);
    }

    /**
    * Die Methode überprüft, ob zu viele Unglückszahlen ausgeschlossen sind und dadurch keine vollständige Generierung möglich ist.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen.
    * @return false, wenn zu viele Unglückszahlen ausgeschlossen sind, ansonsten true.
    */
    @Override
    public boolean istGenerierungMöglich(List<Integer> unglückszahlen){
        int anzahlVerbleibenderZahlen = 0;
        int anzahlVerbleibenderEurozahlen = 0;
        for (Integer zahl : unglückszahlen) {
            if(istGültigeZahl(zahl)){
                anzahlVerbleibenderZahlen++;
            }
            if(istGültigeEurozahl(zahl)){
                anzahlVerbleibenderEurozahlen++;
            }
        }
        if(anzahlVerbleibenderZahlen > ZAHLENRAUM - ANZAHL_TIPP_ZAHLEN || anzahlVerbleibenderEurozahlen > EURO_ZAHLENRAUM - ANZAHL_EURO_ZAHLEN){
            return false;
        }
        return true;
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
