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
        dateiUtil.initialisiereLogger();

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
            } catch (InputMismatchException e) {
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
        System.out.println(">> Sie haben " + lotterie.getLotterieName() + " ausgewählt.");
    }

    public boolean überprüfeLotterieEingabe(String eingabe) throws InputMismatchException{
        if(!eingabe.equalsIgnoreCase("Lotto") && !eingabe.isEmpty() && !eingabe.equalsIgnoreCase("Eurojackpot")){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind Lotto oder Eurojackpot.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Lotto oder Eurojackpot an.");
        }
        return true;
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
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind nur ganze Zahlen.");
                System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
            }
            catch (InputMismatchException e) {
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
        String[] zahlEingaben = eingabe.split("\\s+");
        for (String s : zahlEingaben) {
            int zahl = Integer.parseInt(s);
            if(!lotterie.istGültigeZahl(zahl)){
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + zahl +". Die Eingaben liegen nicht im korrekten Zahlenraum.");
                throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
            }
        }

        if(zahlEingaben.length > 6){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte sind nur bis zu 6 Zahlen.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.");
        }
        
        return true;
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
                try{
                    isValid = überprüfeLöschEingabe(eingabe);
                }
                catch (NumberFormatException e){
                    System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
                }
                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
        }
        
        if(!eingabe.isEmpty() && !eingabe.equalsIgnoreCase("Alle")){
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

    public boolean überprüfeLöschEingabe(String eingabe) throws NumberFormatException, InputMismatchException{
        String[] zahlEingaben = eingabe.split("\\s+");
        for (String s : zahlEingaben) {
            int zahl = Integer.parseInt(s);
            if(zahl <= 0 || zahl > 50){
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Die Eingaben liegen nicht im korrekten Zahlenraum.");
                throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
            }
        }
        return true;
    }
    
    public String handleAntwortEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        System.out.println(">> Wollen Sie eine weitere Tippreihe generieren? Dann geben Ja oder Nein ein.");

        while(!isValid){
             eingabe = scanner.nextLine().trim();
             try{
                isValid = überprüfeAntwortEingabe(eingabe);
             } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
        return eingabe.toLowerCase();
    }

    public boolean überprüfeAntwortEingabe(String eingabe) throws InputMismatchException{
        if(!eingabe.equalsIgnoreCase("Ja") && !eingabe.equalsIgnoreCase("Nein")){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind Ja oder Nein.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.");
        }
        return true;
    }
}
