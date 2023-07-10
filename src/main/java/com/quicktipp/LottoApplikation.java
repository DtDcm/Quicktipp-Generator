package com.quicktipp;

import java.util.ArrayList;
import java.util.List;


public class LottoApplikation 
{
    public static void main(String[] args)
    {
        List<Integer> list = new ArrayList<>();
        ZahlenLotterie euro = new Eurojackpot();
        ZahlenLotterie lotto = new Lotto();
        euro.generiereTippreihe(list);
        lotto.generiereTippreihe(list);
    }
}
