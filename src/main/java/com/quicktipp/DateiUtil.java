package com.quicktipp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class DateiUtil {
    private final String DATEI_PFAD = "Unglückszahlen.txt";

    private final String LOGFILENAME = "logfile.log";
    private final Logger LOGGER = Logger.getLogger("com.quicktipp");
    private FileHandler fileHandler;

    public void speichereUnglückszahlen(List<Integer> unglückszahlen){
        File datei = new File(DATEI_PFAD);
        try {
            if (!datei.exists()) {
                datei.createNewFile();
            }
            FileWriter writer = new FileWriter(DATEI_PFAD);
            unglückszahlen.sort(null);
            for (Integer integer : unglückszahlen) {               
                writer.write(integer.toString() + "\n");             
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Ein Fehler");
        }
    }

    public List<Integer> ladeUnglückszahlen(){
        File datei = new File("Unglückszahlen.txt");
        List<Integer> unglückszahlen = new ArrayList<>();

        if(datei.exists()){
            try {
                Scanner scanner = new Scanner(datei);
                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    unglückszahlen.add(Integer.parseInt(data));
                }
                scanner.close();
            } catch (IOException e) {
                System.out.println("Ein Fahler");
            }
        }
        return unglückszahlen;
    }

    public void löscheUnglückszahlen(List<Integer> zahlenZuLöschen){
        File file = new File(DATEI_PFAD);
        if(file.exists()){
            try {
                File datei = new File(DATEI_PFAD);

                List<Integer> zahlenInDatei = new ArrayList<>();
                List<Integer> unglückszahlen = ladeUnglückszahlen();

                for (Integer gespeicherteZahl : unglückszahlen) {
                    if (!zahlenZuLöschen.contains(gespeicherteZahl)) {
                        zahlenInDatei.add(gespeicherteZahl);
                    }
                }

                FileWriter writer = new FileWriter(datei);

                for (int zahl : zahlenInDatei) {
                    writer.write(String.valueOf(zahl) + "\n");
                }

                writer.close();
            } catch (IOException e) {

            } catch (NumberFormatException e) {

            }
        }
    }

    public void löscheAlleUnglückszahlen(){
        File file = new File(DATEI_PFAD);

        if(file.exists()){
            try {
                File datei = new File(DATEI_PFAD);
                FileWriter writer = new FileWriter(datei);

                writer.write("");
                writer.close();
            } catch (IOException e) {

            } catch (NumberFormatException e) {

            }
        }
    }

    public void initialisiereLogger() {
        try {
            fileHandler = new FileHandler(LOGFILENAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void logNachricht(String message) {
        LOGGER.log(Level.SEVERE, message + "\n");
    }
}
