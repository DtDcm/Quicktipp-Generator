package com.quicktipp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Die Klasse DateiUtil bietet Funktionen zum Speichern, Laden und Löschen von Unglückszahlen in einer Datei.
 * Zusätzlich bietet sie Funktionen zur Initialisierung eines Loggers und zum Loggen von Nachrichten.
 */
public class DateiUtil {
    private String dateiPfad = "Unglückszahlen.txt";

    private final String LOGFILENAME = "logfile.log";
    private final Logger LOGGER = Logger.getLogger("com.quicktipp");
    private FileHandler fileHandler;

    /**
    * Die Methode speichert die übergebene Liste von Unglückszahlen in einer Datei.
    * Die Methode erstellt eine neue Datei, falls sie noch nicht existiert, und schreibt die Unglückszahlen in die Datei.
    * Die Unglückszahlen werden zuvor sortiert.
    * Jede Zahl wird als Zeile in der Datei gespeichert.
    * 
    * @param unglückszahlen Die Liste der Unglückszahlen, die gespeichert werden sollen.
    */
    public void speichereUnglückszahlen(List<Integer> unglückszahlen){
        File datei = new File(dateiPfad);
        try {
            if (!datei.exists()) {
                datei.createNewFile();
            }
            FileWriter writer = new FileWriter(dateiPfad);
            unglückszahlen.sort(null);
            for (Integer integer : unglückszahlen) {               
                writer.write(integer.toString() + "\n");             
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Ein interner Fehler ist aufgetreten: Es ist ein Problem beim Schreiben in die Datei aufgetreten.");
        }
    }
    /**
    * Die Methode lädt die Unglückszahlen aus der Datei und gibt sie als Liste zurück.
    * Die Methode überprüft zunächst, ob die Datei existiert. Wenn dies der Fall ist, wird sie geöffnet und Zeile für Zeile eingelesen.
    * Jede Zeile wird der Liste der Unglückszahlen hinzugefügt.
    * 
    * @return geladeneUnglückszahlen Die Liste der geladenen Unglückszahlen oder eine leere Liste.
    */

    public List<Integer> ladeUnglückszahlen(){
        File datei = new File(dateiPfad);
        List<Integer> geladeneUnglückszahlen = new ArrayList<>();

        if(datei.exists()){
            try {
                Scanner scanner = new Scanner(datei);
                while (scanner.hasNextLine()) {
                    String unglückszahl = scanner.nextLine();
                    geladeneUnglückszahlen.add(Integer.parseInt(unglückszahl));
                }
                scanner.close();
            } catch (IOException e) {
                System.out.println("Ein interner Fehler ist aufgetreten: Es ist ein Problem beim Lesen der Datei aufgetreten.");
            }
        }
        return geladeneUnglückszahlen;
    }

    /**
    * Die Methode löscht bestimmte Zahlen aus der Datei mit den Unglückszahlen.
    * Die Methode überprüft zunächst, ob die Datei existiert. Wenn dies der Fall ist, wird sie geöffnet und die aktuell gespeicherten Unglückszahlen geladen.
    * Es wird überprüft, welche der gespeicherten Unglückszahlen sich in der übergebenen Liste der zu löschenden Zahlen befindet und diese werden ausgeschlossen.
    * Die Zahlen die nicht in der der zu löschenden Zahlenliste sind werden in die Datei wieder reingeschrieben.
    * 
    * @param zahlenZuLöschen Die Liste der Zahlen, die aus der Datei gelöscht werden sollen.
    */
    public void löscheUnglückszahlen(List<Integer> zahlenZuLöschen){
        File file = new File(dateiPfad);
        if(file.exists()){
            try {
                File datei = new File(dateiPfad);

                List<Integer> beibehalteneUnglückszahlen = new ArrayList<>();
                List<Integer> gespeicherteUnglückszahlen = ladeUnglückszahlen();

                for (Integer gespeicherteZahl : gespeicherteUnglückszahlen) {
                    if (!zahlenZuLöschen.contains(gespeicherteZahl)) {
                        beibehalteneUnglückszahlen.add(gespeicherteZahl);
                    }
                }

                FileWriter writer = new FileWriter(datei);

                for (int zahl : beibehalteneUnglückszahlen) {
                    writer.write(String.valueOf(zahl) + "\n");
                }

                writer.close();
            } catch (IOException e) {
                System.out.println("Ein interner Fehler ist aufgetreten: Es ist ein Problem beim Schreiben in die Datei aufgetreten.");
            }
        }
    }

    /**
    * Die Methode löscht alle gespeicherten Unglückszahlen aus der Datei.
    */
    public void löscheAlleUnglückszahlen(){
        File file = new File(dateiPfad);

        if(file.exists()){
            try {
                File datei = new File(dateiPfad);
                FileWriter writer = new FileWriter(datei);

                writer.write("");
                writer.close();
            } catch (IOException e) {
                System.out.println("Ein interner Fehler ist aufgetreten: Es ist ein Problem beim Schreiben in die Datei aufgetreten.");
            }
        }
    }

    /**
    * Die Methode initialisiert den Logger für die Anwendung.
    * Die Log-Nachrichten werden im Append-Modus in die Log-Datei geschrieben.
    */    
    public void initialisiereLogger() {
        try {
            fileHandler = new FileHandler(LOGFILENAME, true); 
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false); //Verhindert das Lognachricht in der Konsole ausgegeben wird
        } catch (SecurityException | IOException e) {
            System.out.println("Ein interner Fehler ist aufgetreten:: Es ist ein Problem beim Initialisieren des Loggers aufgetreten.");
        }
    }

    /** 
    * Die Methode protokolliert eine Nachricht.
    * Die übergebene Nachricht wird in der Logdatei gespeichert.
    *
    * @param message Die Nachricht die protokoliert wird
    */
    public void logNachricht(String message) {
        LOGGER.log(Level.SEVERE, message + "\n");
    }

    /**
    * Die Methode setzt den Dateipfad für die Speicherung der Unglückszahlen.
    */
    public void setDateiPfad(String dateiPfad){
        this.dateiPfad = dateiPfad;
    }
}
