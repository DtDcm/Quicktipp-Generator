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

    @Nested
    class istGültigeZahlTest {
        @ParameterizedTest
        @ValueSource(ints = {1, 50, 10, 23})
        public void validZahl(int zahl) {
            assertTrue(eurojackpot.istGültigeZahl(zahl));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 51})
        public void invalidZahl(int zahl) {
            assertFalse(eurojackpot.istGültigeZahl(zahl));
        }
    }

    @Test
    public void generiereTippreiheTest() {
        
        List<Integer> unglückszahlen = Arrays.asList(1, 5, 13, 24, 37, 45);

        
        eurojackpot.generiereTippreihe(unglückszahlen);
        List<Integer> generierteZahlen = eurojackpot.getTippzahlen();
        List<Integer> generierteEuroZahlen = eurojackpot.getEurozahlen();

        assertEquals(5, generierteZahlen.size());
        assertEquals(2, generierteEuroZahlen.size());
        
        for (int zahl : generierteZahlen) {
            assertTrue(eurojackpot.istGültigeZahl(zahl));
        }
        for (int zahl : generierteEuroZahlen) {
            assertTrue(eurojackpot.istGültigeEurozahl(zahl));
        }

        Set<Integer> eindeutigeZahlen = new HashSet<>(generierteZahlen);
        assertEquals(5, eindeutigeZahlen.size());
        
        Set<Integer> eindeutigeEuroZahlen = new HashSet<>(generierteEuroZahlen);
        assertEquals(2, eindeutigeEuroZahlen.size());

        assertTrue(!generierteZahlen.containsAll(unglückszahlen));
        assertTrue(!generierteEuroZahlen.containsAll(unglückszahlen));
    }
}