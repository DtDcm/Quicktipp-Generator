package com.quicktipp;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LottoApplikation 
{
    public static void main(String[] args)
    {
        List<Integer> list = new ArrayList<>();
        ZahlenLotterie lotterie;

        //TODO Terminal and User input!
        System.out.println(">> Geben Sie an, welche Tippreihe Sie verwenden wollen Lotto oder Eurojackpot:");
        Scanner scanner = new Scanner(System.in);
        String eingabe = scanner.nextLine();

        try {
            if(eingabe.equalsIgnoreCase("Lotto") || eingabe.isEmpty()){
                eingabe = "Lotto";
                lotterie = new Lotto();
            } else if (eingabe.equalsIgnoreCase("Eurojackpot")){
                eingabe = "Eurojackpot";
                lotterie = new Eurojackpot();
            }
            else{
                throw new InputMismatchException(">> Inkorrekte Eingabe. Bitte geben Sie entweder Lotto oder Eurojackpot an.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println(">> Sie haben " + eingabe + " ausgewählt.");

        scanner.close();
        
    }
}
