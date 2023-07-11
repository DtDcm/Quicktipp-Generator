package com.quicktipp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LottoApplikation 
{
    private static ZahlenLotterie lotterie;
    private static List<Integer> unglückszahlen = new ArrayList<>();
    public static void main(String[] args)
    {
        String antwort = "Ja";
        Scanner scanner = new Scanner(System.in);

        while (antwort.equalsIgnoreCase("Ja")) {
            System.out.println(">> Geben Sie an, welche Tippreihe Sie verwenden wollen Lotto oder Eurojackpot:");
            

            überprüfeLotterieEingabe(scanner);
            
            System.out.println(">> Geben Sie bis zu 6 Unglückszahlen die ausgeschlossen werden sollen:");

            überprüfeZahlenEingabe(scanner);
            lotterie.generiereTippreihe(unglückszahlen);
            System.out.println(">> Wollen Sie eine weitere Tippreihe generieren? Dann geben Ja oder Nein ein.");
            antwort = überprüfeAntwortEingabe(scanner);
        }
        

        scanner.close();
    }

    private static void überprüfeLotterieEingabe(Scanner scanner){
        boolean istGültig = false;
        String eingabe = "";
        while(!istGültig){
             eingabe = scanner.nextLine();
            try {
                if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty()){
                    istGültig = true;
                    eingabe = "Lotto";
                    lotterie = new Lotto();
                } else if (eingabe.equalsIgnoreCase("Eurojackpot")){
                    istGültig = true;
                    eingabe = "Eurojackpot";
                    lotterie = new Eurojackpot();
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
        boolean istGültig = false;
        String eingabe = "";
        while(!istGültig){
            eingabe = scanner.nextLine();
            try {
                String[] zahlEingaben = eingabe.split(" ");
                if(eingabe.isEmpty()){
                    break;
                }

                if(zahlEingaben.length > 6){
                    throw new InputMismatchException(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.");
                }

                for (String s : zahlEingaben) {
                    int zahl = Integer.parseInt(s);
                    if(lotterie instanceof Lotto){
                        istGültig = (zahl > 0 && zahl <= 49);
                    } else if (lotterie instanceof Eurojackpot) {
                        istGültig = (zahl > 0 && zahl <= 50);
                    }
                    
                    if(!unglückszahlen.contains(zahl) && istGültig){
                        unglückszahlen.add(zahl);
                    }
                    else if(!istGültig){
                        throw new InputMismatchException(">> Inkorrekte Eingabe. Die Zahl liegt nicht im korrekten Zahlenraum.");
                    }
                }
            } catch (NumberFormatException e){
                System.out.println(eingabe + " ist keine Zahl. Bitte geben ganze Zahlen an.");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        System.out.println(">> Die Zahlen wurden gespeichert.");
    }

    private static String überprüfeAntwortEingabe(Scanner scanner){
        boolean istGültig = false;
        String eingabe = "";
        while(!istGültig){
             eingabe = scanner.nextLine();
            try {
                if(eingabe.equalsIgnoreCase("Ja")|| eingabe.equalsIgnoreCase("Nein")){
                    istGültig = true;
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
}
