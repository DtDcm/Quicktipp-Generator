package com.quicktipp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LottoApplikation 
{
    private static ZahlenLotterie lotterie;
    private static List<Integer> unglückszahlen = new ArrayList<>();
    private static final String DATEI_PFAD = "Unglückszahlen.txt";
    public static void main(String[] args)
    {
        String istNeueTippreihe = "Ja";
        Scanner scanner = new Scanner(System.in);

        while (istNeueTippreihe.equalsIgnoreCase("Ja")) {
            ladeUnglückszahlen();
            System.out.println(">> Geben Sie an, welche Tippreihe Sie verwenden wollen:\n -- Lotto \n -- Eurojackpot");
            überprüfeLotterieEingabe(scanner);

            if (!unglückszahlen.isEmpty()) {
                List<Integer> zuLöschen = überprüfeLöschEingabe(scanner);
                if (zuLöschen == null) {
                    löschealleUnglückszahlen();
                    System.out.println("test");
                } else if(!zuLöschen.isEmpty()){
                    löscheUnglückszahlen(zuLöschen);
                }
            }
            
            
            System.out.println(">> Geben Sie bis zu 6 Unglückszahlen die ausgeschlossen werden sollen, zum Bespiel 3 13 24 40 31:");
            überprüfeZahlenEingabe(scanner);

            lotterie.generiereTippreihe(unglückszahlen);
            speichereUnglückszahlen();

            System.out.println(">> Wollen Sie eine weitere Tippreihe generieren? Dann geben Ja oder Nein ein.");
            istNeueTippreihe = überprüfeAntwortEingabe(scanner);
        }
        scanner.close();
    }

    private static void überprüfeLotterieEingabe(Scanner scanner){
        String eingabe = "";
        while(true){
             eingabe = scanner.nextLine().trim();
            try {
                if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty()){
                    eingabe = "Lotto";
                    lotterie = new Lotto();
                    break;
                } else if (eingabe.equalsIgnoreCase("Eurojackpot")){
                    eingabe = "Eurojackpot";
                    lotterie = new Eurojackpot();
                    break;
                }
                else{
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Lotto oder Eurojackpot an.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(">> Sie haben " + eingabe + " ausgewählt.");
    }

    private static void überprüfeZahlenEingabe(Scanner scanner){
        String eingabe = "";
        List<Integer> tempList = new ArrayList<>();

        while(true){
            eingabe = scanner.nextLine().trim();
            tempList.clear();
            try {
                String[] zahlEingaben = eingabe.split("\\s+");

                if(eingabe.isEmpty()){
                    break;
                }

                int count = 0;
                for (String s : zahlEingaben) {
                    int zahl = Integer.parseInt(s);
                    if(!lotterie.istGültigeZahl(zahl)){
                        throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
                    }

                    if(!tempList.contains(zahl) && !unglückszahlen.contains(zahl)){
                        tempList.add(zahl);
                    }
                    count++;
                }

                if(count > 6){
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.");
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        unglückszahlen.addAll(tempList);
    }

    private static String überprüfeAntwortEingabe(Scanner scanner){
        String eingabe = "";
        while(true){
             eingabe = scanner.nextLine().trim();
            try {
                if(eingabe.equalsIgnoreCase("Ja")|| eingabe.equalsIgnoreCase("Nein")){
                    break;
                } 
                else{
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return eingabe;
    }

    private static void speichereUnglückszahlen(){
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
            System.out.println("Ein Fahler");
        }
    }

    private static void ladeUnglückszahlen(){
        File datei = new File("Unglückszahlen.txt");
        unglückszahlen = new ArrayList<>();

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
    }

    private static List<Integer> überprüfeLöschEingabe(Scanner scanner){
        String eingabe = "";
        List<Integer> tempList = new ArrayList<>();
        System.out.println(">> Diese wurden schon gespeichert:\n>> " 
        + unglückszahlen.toString() 
        + "\n>> Wollen SIe Zahlen löschen:"
        + "\n -- Geben Sie die Zahlen ein, die aus der Datei entfernt werden sollen"
        + "\n -- Geben Sie Alle, wenn Sie alle gespeicherten Zahlen löschen wollen"
        + "\n -- Drücke Enter, wenn Sie keine Zahl löschen wollen");

        while(true){
            eingabe = scanner.nextLine().trim();
            tempList.clear();
            try {
                if (eingabe.equalsIgnoreCase("Alle")) {
                    return null;
                }
                String[] zahlEingaben = eingabe.split("\\s+");

                if(eingabe.isEmpty()){
                    break;
                }

                for (String s : zahlEingaben) {
                    int zahl = Integer.parseInt(s);
                    if(zahl < 0 && zahl > 50){
                        throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
                    }
                    if(!tempList.contains(zahl)){
                        tempList.add(zahl);
                    }
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return tempList;
    }

    private static void löscheUnglückszahlen(List<Integer> zahlenZuLöschen){
        File file = new File(DATEI_PFAD);
        if(file.exists()){
            try {
                File datei = new File(DATEI_PFAD);

                ArrayList<Integer> zahlenInDatei = new ArrayList<>();

                for (Integer gespeicherteZahl : unglückszahlen) {
                    if (!zahlenZuLöschen.contains(gespeicherteZahl)) {
                        zahlenInDatei.add(gespeicherteZahl);
                    }
                }
                unglückszahlen.removeAll(zahlenZuLöschen);

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

    private static void löschealleUnglückszahlen(){
        File file = new File(DATEI_PFAD);
        if(file.exists()){
            try {
                File datei = new File(DATEI_PFAD);
                FileWriter writer = new FileWriter(datei);

                writer.write("");
                writer.close();
                unglückszahlen.clear();
            } catch (IOException e) {

            } catch (NumberFormatException e) {

            }

        }
    }
}
