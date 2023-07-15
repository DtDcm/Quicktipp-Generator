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

public class LottoTest {
    
    Lotto lotto;

    @BeforeEach
    public void setUp() {
         lotto = new Lotto();
    }

        @Nested
    class istGültigeZahlTest {
        @ParameterizedTest
        @ValueSource(ints = {1, 49, 10, 23})
        public void validZahl(int zahl) {
            assertTrue(lotto.istGültigeZahl(zahl));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 50})
        public void invalidZahl(int zahl) {
            assertFalse(lotto.istGültigeZahl(zahl));
        }
    }

    @Test
    public void generiereTippreiheTest() {
        
        List<Integer> unglückszahlen = Arrays.asList(1, 5, 13, 24, 37, 45);
        
        lotto.generiereTippreihe(unglückszahlen);
        List<Integer> generierteZahlen = lotto.tippZahlen;

        assertEquals(6, generierteZahlen.size());
        
        for (int zahl : generierteZahlen) {
            assertTrue(lotto.istGültigeZahl(zahl));
        }

        Set<Integer> eindeutigeZahlen = new HashSet<>(generierteZahlen);
        assertEquals(6, eindeutigeZahlen.size());
        
        assertTrue(!generierteZahlen.containsAll(unglückszahlen));
    }
}