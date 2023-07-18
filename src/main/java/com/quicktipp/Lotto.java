package com.quicktipp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Klasse Lotto implementiert die Generierung von Tippreihen für die Lotterie "Lotto".
 */
public class Lotto extends ZahlenLotterie{
    private final int ANZAHL_TIPP_ZAHLEN = 6;
    private final int ZAHLENRAUM = 49;
    private List<Integer> tippzahlen = new ArrayList<>();

    /**
    * Die Methode generiert eine Tippreihe für die Lotterie "Lotto" unter Berücksichtigung von Unglückszahlen.
    * Nachdem die Tippzahlen erfolgreich generiert wurden, werden sie in der Konsole ausgegeben.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen, die ausgeschlossen werden sollen.
    * @throws IllegalStateException Wenn bereits zu viele Unglückszahlen vorhanden sind.
    */
    public void generiereTippreihe(List<Integer> unglückszahlen) throws IllegalStateException {
        tippzahlen = generiereZahlen(unglückszahlen, ANZAHL_TIPP_ZAHLEN, ZAHLENRAUM);

        System.out.println(">> Quick-Tipp: " + Arrays.toString(tippzahlen.toArray()));
    }

    /**
    * Die Methode überprüft, ob eine gegebene Zahl gültig ist für die Lotterie "Lotto".
    * 
    * @param zahl Die zu überprüfende Zahl.
    * @return true, wenn die Zahl gültig ist, ansonsten false.
    */
    @Override
    public boolean istGültigeZahl(int zahl) {
        return (zahl > 0 && zahl <= ZAHLENRAUM);
    }

    /**
    * Die Methode überprüft, ob zu viele Unglückszahlen ausgeschlossen sind und dadurch keine vollständige Generierung möglich ist.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen.
    * @return false, wenn zu viele Unglückszahlen ausgeschlossen sind, ansonsten true.
    */
    @Override
    public boolean istGenerierungMöglich(List<Integer> unglückszahlen){
        if(unglückszahlen.size() > ZAHLENRAUM - ANZAHL_TIPP_ZAHLEN){
            return false;
        }
        return true;
    }

    /**
    * Die Methode gibt den Lotterienamen "Lotto" zurück.
    * 
    * @return "Lotto"
    */
    @Override
    public String getLotterieName() {
        return "Lotto";
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
}
