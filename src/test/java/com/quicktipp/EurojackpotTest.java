package com.quicktipp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class EurojackpotTest {
    
    Eurojackpot eurojackpot;

    @BeforeEach
    public void setUp() {
         eurojackpot = new Eurojackpot();
    }

    @Test
    public void generiereTippreiheTest() {
        
        List<Integer> unglückszahlen = Arrays.asList(13, 24, 37);
        
        eurojackpot.generiereTippreihe(unglückszahlen);
        List<Integer> generierteZahlen = eurojackpot.zahlen;
        List<Integer> generierteEuroZahlen = eurojackpot.euroZahlen;

        assertEquals(5, generierteZahlen.size());
        assertEquals(2, generierteEuroZahlen.size());
        
        for (int zahl : generierteZahlen) {
            assertTrue(eurojackpot.istGültigeZahl(zahl));
        }
        for (int zahl : generierteEuroZahlen) {
            assertTrue(eurojackpot.istGültigeZahl(zahl));
        }

        Set<Integer> eindeutigeZahlen = new HashSet<>(generierteZahlen);
        assertEquals(5, eindeutigeZahlen.size());
        
        Set<Integer> eindeutigeEuroZahlen = new HashSet<>(generierteEuroZahlen);
        assertEquals(2, eindeutigeEuroZahlen.size());
    }

    @Nested
    class istGültigeZahlTest {
        @ParameterizedTest
        @ValueSource(ints = {1, 49, 10, 23})
        public void validZahl(int zahl) {
            assertTrue(eurojackpot.istGültigeZahl(zahl));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 51})
        public void invalidZahl(int zahl) {
            assertFalse(eurojackpot.istGültigeZahl(zahl));
        }
    }
}