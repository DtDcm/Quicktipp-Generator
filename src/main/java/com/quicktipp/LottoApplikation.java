package com.quicktipp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class LottoApplikation 
{
    public ZahlenLotterie lotterie;
    public List<Integer> unglückszahlen = new ArrayList<Integer>();
    private DateiUtil dateiUtil = new DateiUtil();
    public final int anzahlUnglückzahlenEingabe = 6;
    
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
        System.out.println(">> Geben Sie an, welche Tippreihe Sie verwenden wollen:\n -- Lotto \n -- Eurojackpot");
        
        while(true){
            String eingabe = scanner.nextLine().trim();
            try {
                überprüfeLotterieEingabe(eingabe);
                System.out.println(">> Sie haben " + lotterie.getLotterieName() + " ausgewählt.");
                break;
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void überprüfeLotterieEingabe(String eingabe) throws InputMismatchException{
        if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty()){
            lotterie = new Lotto();
        } else if (eingabe.equalsIgnoreCase("Eurojackpot")){
            lotterie = new Eurojackpot();
        }
        else{
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind Lotto oder Eurojackpot.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder 'Lotto' oder 'Eurojackpot' an.");
        }
    }

    public void handleZahlenEingabe(Scanner scanner){
        boolean isValid = false;
        System.out.println(">> Geben Sie bis zu 6 Unglückszahlen die ausgeschlossen werden sollen, zum Bespiel '3 13 24 40 31':");

        while(!isValid){
            String eingabe = scanner.nextLine().trim();
            if(eingabe.isEmpty()){
                break;
            }
            try {
                isValid = überprüfeZahlenEingabe(eingabe);
                for (String s : eingabe.split("\\s+")) {
                    int zahl = Integer.parseInt(s);
                    
                    if(!unglückszahlen.contains(zahl)){
                        unglückszahlen.add(zahl);
                    }
                }
                break;
            } catch (NumberFormatException e){
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind nur ganze Zahlen.");
                System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
            }
            catch (InputMismatchException | IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean überprüfeZahlenEingabe(String eingabe) throws NumberFormatException, InputMismatchException{
        String[] zahlEingaben = eingabe.split("\\s+");
        for (String s : zahlEingaben) {
            int zahl = Integer.parseInt(s);
            if(!lotterie.istGültigeZahl(zahl)){
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + zahl +". Die Eingaben liegen nicht im korrekten Zahlenraum.");
                throw new InputMismatchException(">> Inkorrekte Eingabe. Die Zahlen bei " + lotterie.getLotterieName() +" dürfen nur zwischen 1 und " + lotterie.getZahlenraum() + ".");
            }
        }

        if(zahlEingaben.length > anzahlUnglückzahlenEingabe){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte sind nur bis zu 6 Zahlen.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.");
        }

        if(unglückszahlen.size() + zahlEingaben.length > lotterie.getZahlenraum() - lotterie.getAnzahlTippZahlen()){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Es sind schon zu viele Unglückzahlen gespeichert");
            throw new IllegalStateException("Es sind schon zu viele Unglückzahlen gespeichert. Bitte Löschen die Unglückzahlen, die Sie nicht mehr verwenden wollen.");
        }
        
        return true;
    }

    public void handleLöschEingabe(Scanner scanner){
        boolean isValid = false;
        List<Integer> zuLöschen = new ArrayList<>();
        System.out.println(">> Diese wurden schon gespeichert:\n>> " 
        + unglückszahlen.toString() 
        + "\n>> Wollen Sie Zahlen löschen:"
        + "\n -- Geben Sie die Zahlen ein, die aus der Datei entfernt werden sollen"
        + "\n -- Geben Sie Alle, wenn Sie alle gespeicherten Zahlen löschen wollen"
        + "\n -- Drücke Enter, wenn Sie keine Zahl löschen wollen");

        while(!isValid){
            String eingabe = scanner.nextLine().trim();
                if (eingabe.equalsIgnoreCase("Alle")) {
                    dateiUtil.löscheAlleUnglückszahlen();
                    break;
                }

                if(eingabe.isEmpty()){
                    break;
                }
                
                try{
                    isValid = überprüfeLöschEingabe(eingabe);
                    for (String s : eingabe.split("\\s+")) {
                        int zahl = Integer.parseInt(s);
                        
                        if(!zuLöschen.contains(zahl)){
                            zuLöschen.add(zahl);
                        }
                    }
                    dateiUtil.löscheUnglückszahlen(zuLöschen);
                }
                catch (NumberFormatException e){
                    dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe +". Erlaubte Eingaben sind nur ganze Zahlen.");
                    System.out.println(">> Inkorrekte Eingabe. Die Eingabe darf nur ganze Zahlen enhalten.");
                }
                catch (InputMismatchException e) {
                    System.out.println(e.getMessage());
                }
        }
        unglückszahlen = dateiUtil.ladeUnglückszahlen();
    }

    public boolean überprüfeLöschEingabe(String eingabe) throws NumberFormatException, InputMismatchException{
        String[] zahlEingaben = eingabe.split("\\s+");
        for (String s : zahlEingaben) {
            int zahl = Integer.parseInt(s);
            if(zahl <= 0 || zahl > 50){
                dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + zahl +". Die Eingaben liegen nicht im korrekten Zahlenraum.");
                throw new InputMismatchException(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.");
            }
        }
        return true;
    }
    
    public String handleAntwortEingabe(Scanner scanner){
        String eingabe = "";
        boolean isValid = false;
        System.out.println(">> Wollen Sie eine weitere Tippreihe generieren? Dann geben 'Ja' oder 'Nein' ein.");

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
