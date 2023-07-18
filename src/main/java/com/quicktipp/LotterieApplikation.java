package com.quicktipp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Die Klasse LottoApplikation implementiert eine interaktive Anwendung zur Generierung von Quick-Tippreihen.
 * Sie ermöglicht es dem Benutzer, zwischen den Lotterien "Lotto" und "Eurojackpot" zu wählen und Unglückszahlen
 * festzulegen, die bei der Generierung von Tippreihen ausgeschlossen werden sollen.
 */
public class LotterieApplikation 
{
    private String dateiPfad = "Unglückszahlen.txt";
    public ZahlenLotterie lotterie;
    public List<Integer> unglückszahlen = new ArrayList<Integer>();
    public DateiUtil dateiUtil = new DateiUtil(dateiPfad);
    public final int anzahlUnglückzahlenEingabe = 6;
    
    public static void main(String[] args)
    {
        LotterieApplikation app = new LotterieApplikation();
        app.start();
    }

    /**
    * Die Methode dient als Start für die Ausführung der LottoApplikation.
    * Sie steuert den Ablauf der Anwendung, indem sie die entsprechenden Methoden aufruft, um Benutzereingaben zu
    * verarbeiten für die Generierung der Quick-Tippreihe.
    * Die Methode läuft in einer Schleife, bis der Benutzer "Nein" eingibt und damit keine neue Tippreihe generieren will.
    */
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
                try {
                    lotterie.generiereTippreihe(unglückszahlen);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    dateiUtil.logNachricht(e.getMessage());
                }
                dateiUtil.speichereUnglückszahlen(unglückszahlen);

                istNeueTippreihe = handleAntwortEingabe(scanner);
            }
        scanner.close();
    }

    /**
    * Die Methode ermöglicht es dem Benutzer, die gewünschte Lotterie für die Tippreihe auszuwählen.
    * Sie zeigt dem Benutzer die verfügbaren Lotterien an und liest die Benutzereingabe ein.
    * Die Methode läuft in einer Schleife, bis eine gültige Lotterieauswahl getroffen wurde.
    *
    * @param scanner Der Scanner zum Lesen der Benutzereingabe.
    */
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

    /**
    * Die Methode validiert die Benutzereingabe für die Lotterieauswahl.
    * Sie überprüft, ob die Eingabe den erlaubten Optionen entspricht: "Lotto" oder "Eurojackpot".
    * Wenn die Eingabe gültig ist, wird eine entsprechende Lotterieinstanz erstellt.
    * Falls die Eingabe ungültig ist, wird eine Fehlermeldung ausgelöst und protokolliert.
    *
    * @param eingabe Die Benutzereingabe für die Lotterieauswahl.
    * @throws InputMismatchException Wenn die Eingabe nicht den erlaubten Optionen entspricht.
    */
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
    /**
    * Die Methode ermöglicht es dem Benutzer, bis zu 6 Unglückszahlen einzugeben, die von den Tippreihen ausgeschlossen werden sollen.
    * Sie fordert den Benutzer auf, die Zahlen einzugeben, und überprüft die Eingabe.
    * Die Methode läuft in einer Schleife, bis eine gültige Eingabe gemacht wurde.
    * 
    * @param scanner Der Scanner zum Lesen der Benutzereingabe.
    */
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
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
    * Die Methode überprüft die Benutzereingabe für die Unglückszahlen.
    * Sie überprüft, ob die Eingabe eine ganze Zahl ist und ob jede einzelne Zahl, im zulässigen Zahlenraum der Lotterie liegt.
    * Dann überprüft auch, ob die Anzahl der eingegebenen Zahlen die erlaubte Obergrenze nicht überschreitet.
    * Falls diese Überprüfungen fehlschlagen,
    * werden entsprechende Fehlermeldungen ausgelöst und protokolliert.
    * 
    * @param eingabe Die Benutzereingabe für die Unglückszahlen.
    * @return true, wenn die Eingabe gültig ist.
    * @throws NumberFormatException Wenn eine Zahl in der Eingabe keine ganze Zahl ist.
    * @throws InputMismatchException Wenn die Eingabe nicht korrekten Zahlenraum liegt oder die Anzahl nicht korrekt ist.
    * @throws IllegalStateException Wenn bereits zu viele Unglückszahlen gespeichert sind.
    */
    public boolean überprüfeZahlenEingabe(String eingabe) throws NumberFormatException, InputMismatchException, IllegalStateException{
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
        
        return true;
    }

    /**
    * Die Methode ermöglicht es dem Benutzer, gespeicherte Unglückszahlen zu löschen.
    * Sie zeigt dem Benutzer die bereits gespeicherten Unglückszahlen an.
    * Der Benutzer kann entweder einzelne Zahlen eingeben, die aus der Datei entfernt werden sollen,
    * oder "Alle" eingeben, um alle gespeicherten Zahlen zu löschen. Wenn der Benutzer keine Zahlen löschen möchte,
    * kann er einfach die Eingabetaste drücken, um den Vorgang zu beenden.
    * Die Methode läuft in einer Schleife, bis eine gültige Eingabe gemacht wurde.
    * 
    * @param scanner Der Scanner zum Lesen der Benutzereingabe.
    */
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

    /**
    * Die Methode überprüft die Benutzereingabe für das Löschen von Unglückszahlen.
    * Sie überprüft, ob die Eingabe ganze Zahlen sind und ob diese im zulässigen Zahlenraum liegen.
    * Sie überprüft jede einzelne Zahl und stellt sicher, dass sie im zulässigen Bereich von 1 bis 50 liegt.
    * Wenn eine Überprüfung fehlschlägt, wird eine Fehlermeldung ausgelöst und protokolliert.
    * 
    * @param eingabe Die Benutzereingabe für das Löschen von Unglückszahlen.
    * @return true, wenn die Eingabe gültig ist.
    * @throws NumberFormatException Wenn eine Zahl in der Eingabe keine ganze Zahl ist.
    * @throws InputMismatchException Wenn die Eingabe nicht im zulässigen Zahlenraum liegt.
    */
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
    
    /**
    * Die Methode ermöglicht es dem Benutzer, eine weitere Tippreihe zu generieren.
    * Sie fordert den Benutzer auf, entweder "Ja" oder "Nein" einzugeben.
    * Die Methode überprüft die Eingabe, um sicherzustellen, dass sie einer der erlaubten Antworten entspricht.
    * Die Methode läuft in einer Schleife, bis eine gültige Eingabe gemacht wurde.
    * 
    * @param scanner Der Scanner zum Lesen der Benutzereingabe.
    * @return Die Antwort des Benutzers in Kleinbuchstaben: "ja" oder "nein".
    */
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

    /**
    * Die Methode überprüft die Benutzereingabe für die Antwort auf die Frage nach der Generierung einer weiteren Tippreihe.
    * Sie überprüft, ob die Eingabe entweder "Ja" oder "Nein" ist.
    * Wenn die Überprüfung fehlschlägt, wird eine Fehlermeldung ausgelöst und protokolliert.
    * 
    * @param eingabe Die Benutzereingabe für die Antwort auf die Frage.
    * @return true, wenn die Eingabe gültig ist.
    * @throws InputMismatchException Wenn die Eingabe nicht den erlaubten Antworten entspricht: "Ja" oder "Nein".
    */
    public boolean überprüfeAntwortEingabe(String eingabe) throws InputMismatchException{
        if(!eingabe.equalsIgnoreCase("Ja") && !eingabe.equalsIgnoreCase("Nein")){
            dateiUtil.logNachricht("Ein Fehler ist aufgetreten: Der Benutzer hat eine inkorrekte Eingabe getätigt: " + eingabe + ". Erlaubte Eingaben sind Ja oder Nein.");
            throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.");
        }
        return true;
    }
}
