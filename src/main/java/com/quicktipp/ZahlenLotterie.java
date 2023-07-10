package com.quicktipp;

import java.util.List;

public abstract class ZahlenLotterie implements TippreihenGenerator {

    @Override
    public abstract void generiereTippreihe(List<Integer> zahlen);
}
