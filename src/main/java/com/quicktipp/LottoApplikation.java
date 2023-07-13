package com.quicktipp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LottoApplikation 
{
    public ZahlenLotterie lotterie;
    public List<Integer> unglückszahlen = new ArrayList<>();
    private DateiUtil dateiUtil = new DateiUtil();
    
    public static void main(String[] args)
    {
        LottoApplikation app = new LottoApplikation();
        app.start();
        
    }

    private void start(){
        String istNeueTippreihe = "Ja";
        Scanner scanner = new Scanner(System.in);

        while (istNeueTippreihe.equalsIgnoreCase("Ja")) {
            unglückszahlen = dateiUtil.ladeUnglückszahlen();
            
            handleLotterieEingabe(scanner);

            if (!unglückszahlen.isEmpty()) {
                handleLöschEingabe(scanner);               
            }
            
            handleZahlenEingabe(scanner);

            lotterie.generiereTippreihe(unglückszahlen);
            dateiUtil.speichereUnglückszahlen(unglückszahlen);

            istNeueTippreihe = handleAntwortEingabe(scanner);
        }
        scanner.close();
    }

    public void handleLotterieEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        System.out.println(">> Geben Sie an, welche Tippreihe Sie verwenden wollen:\n -- Lotto \n -- Eurojackpot");
        
        while(!isValid){
            eingabe = scanner.nextLine().trim();
            try {
                isValid = überprüfeLotterieEingabe(eingabe);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }

        if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty()){
            eingabe = "Lotto";
            lotterie = new Lotto();
        } else if (eingabe.equalsIgnoreCase("Eurojackpot")){
            eingabe = "Eurojackpot";
            lotterie = new Eurojackpot();
        }
        System.out.println(">> Sie haben " + eingabe + " ausgewählt.");
    }

    public boolean überprüfeLotterieEingabe(String eingabe) throws InputMismatchException{
        boolean isValid = false;
        if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty() || eingabe.equalsIgnoreCase("Eurojackpot")){
            isValid = true;
        }
        else{
            throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Lotto oder Eurojackpot an.");
        }
        return isValid;
    }

    public void handleZahlenEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        System.out.println(">> Geben Sie bis zu 6 Unglückszahlen die ausgeschlossen werden sollen, zum Bespiel 3 13 24 40 31:");

        while(!isValid){
            eingabe = scanner.nextLine().trim();
            if(eingabe.isEmpty()){
                break;
            }
            try {
                isValid = überprüfeZahlenEingabe(eingabe);
            } catch (NumberFormatException e){
            System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            
        }
        if (!eingabe.isEmpty()) {
            for (String s : eingabe.split("\\s+")) {
                int zahl = Integer.parseInt(s);
                
                if(!unglückszahlen.contains(zahl)){
                    unglückszahlen.add(zahl);
                }
            }
        }
    }

    public boolean überprüfeZahlenEingabe(String eingabe) throws NumberFormatException, InputMismatchException{
        boolean isValid = false;

        String[] zahlEingaben = eingabe.split("\\s+");
        for (String s : zahlEingaben) {
            int zahl = Integer.parseInt(s);
            if(!lotterie.istGültigeZahl(zahl)){
                throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
            }
        }

        if(zahlEingaben.length > 6){
            throw new InputMismatchException(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.");
        }
        
        isValid = true;
        
        return isValid;
    }

    public void handleLöschEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        List<Integer> zuLöschen = new ArrayList<>();
        System.out.println(">> Diese wurden schon gespeichert:\n>> " 
        + unglückszahlen.toString() 
        + "\n>> Wollen Sie Zahlen löschen:"
        + "\n -- Geben Sie die Zahlen ein, die aus der Datei entfernt werden sollen"
        + "\n -- Geben Sie Alle, wenn Sie alle gespeicherten Zahlen löschen wollen"
        + "\n -- Drücke Enter, wenn Sie keine Zahl löschen wollen");

        while(!isValid){
            eingabe = scanner.nextLine().trim();
            zuLöschen.clear();
                if (eingabe.equalsIgnoreCase("Alle")) {
                    zuLöschen = null;
                    break;
                }

                if(eingabe.isEmpty()){
                    break;
                }
                isValid = überprüfeLöschEingabe(eingabe);
        }
        if(!eingabe.isEmpty() && !eingabe.equalsIgnoreCase("Alle")){
            System.out.println(eingabe);
            for (String s : eingabe.split("\\s+")) {
                int zahl = Integer.parseInt(s);
                
                if(!zuLöschen.contains(zahl)){
                    zuLöschen.add(zahl);
                }
            }
        }

        if (zuLöschen == null) {
            dateiUtil.löscheAlleUnglückszahlen();
        } else if(!zuLöschen.isEmpty()){
            dateiUtil.löscheUnglückszahlen(zuLöschen);
        }
        unglückszahlen = dateiUtil.ladeUnglückszahlen();
    }

    public boolean überprüfeLöschEingabe(String eingabe){

        String[] zahlEingaben = eingabe.split("\\s+");
        boolean isValid = false;
        try{
            for (String s : zahlEingaben) {
                int zahl = Integer.parseInt(s);
                if(zahl > 0 && zahl <= 50){
                    isValid = true;
                }
                else{
                    isValid = false;
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
                }
            }
        }
        catch (NumberFormatException e){
            System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isValid;
    }
    
    public String handleAntwortEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        System.out.println(">> Wollen Sie eine weitere Tippreihe generieren? Dann geben Ja oder Nein ein.");

        while(!isValid){
             eingabe = scanner.nextLine().trim();
             isValid = überprüfeAntwortEingabe(eingabe);
        }
        return eingabe;
    }

    public boolean überprüfeAntwortEingabe(String eingabe){
        boolean isValid = false;
            try {
                if(eingabe.equalsIgnoreCase("Ja")|| eingabe.equalsIgnoreCase("Nein")){
                    isValid = true;
                } 
                else{
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        return isValid;
    }
}
