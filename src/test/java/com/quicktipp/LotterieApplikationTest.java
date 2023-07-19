package com.quicktipp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;

public class LotterieApplikationTest {
    
    private String testDateiPfad = "TestUnglückszahlen.txt";
    LotterieApplikation app;
    DateiUtil dateiUtil;

    @BeforeEach
    public void setUp() {
        app = new LotterieApplikation();
        app.dateiUtil = new DateiUtil(testDateiPfad);
    }

    @AfterEach
    public void tearDown() {
        File testDatei = new File(testDateiPfad);
        if (testDatei.exists()) {
            testDatei.delete();
        }
    }

    @Nested
    class ÜberprüfeLotterieEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"Lotto", "lotto", "LOTTO", "  lotto  ", "", "    " })
        public void validEingabeLotto(String eingabe) {
            app.überprüfeLotterieEingabe(eingabe.trim());
            assertTrue(app.lotterie instanceof Lotto);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Eurojackpot", "eurojackpot", "EUROJACKPOT", "  Eurojackpot  "})
        public void validEingabeEurojackpot(String eingabe) {
            app.überprüfeLotterieEingabe(eingabe.trim());
            assertTrue(app.lotterie instanceof Eurojackpot);
        }

        @ParameterizedTest
        @ValueSource(strings = {"123", "invalid", "Latte", "a", "!@#$%", "Lotto!", "Eurojackpot."})
        public void invalidEingabe(String eingabe) {
            try {
                app.überprüfeLotterieEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Bitte geben Sie entweder 'Lotto' oder 'Eurojackpot' an.", e.getMessage());
            }
        }
    }

    @Nested
    class HandleLotterieEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"Lotto\n", "lotto\n", "LOTTO\n", "  lotto  \n", "\n", "    \n" })
        public void lottoEingabe(String eingabe) {
            app.handleLotterieEingabe(new Scanner(eingabe));
            assertTrue(app.lotterie instanceof Lotto);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Eurojackpot\n", "eurojackpot\n", "EUROJACKPOT\n", "  Eurojackpot  \n", "EUROJACKPOT\n" })
        public void eurojackpotEingabe(String eingabe) {
            app.handleLotterieEingabe(new Scanner(eingabe));
            assertTrue(app.lotterie instanceof Eurojackpot);
        }
    }
    
    @Nested
    class ÜberprüfeZahlenEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "49", "  1  ", "1  2 33  14 5  46", "1 1 1 1"})
        public void validZahlEingabeLotto(String eingabe) {
            app.lotterie = new Lotto();
            boolean result = app.überprüfeZahlenEingabe(eingabe.trim());
            Assertions.assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "50", "  1  ", "1  2 33  14 5  46", "1 1 1 1"})
        public void validZahlEingabeEuroJackpot(String eingabe) {
            app.lotterie = new Eurojackpot();
            boolean result = app.überprüfeZahlenEingabe(eingabe.trim());
            Assertions.assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "Latte", "a", "!@#$%", "Lotto!", "Eurojackpot.", "1.23", "1,23"})
        public void invalidZahlEingabe(String eingabe) {
            app.lotterie = new Lotto();
            Assertions.assertThrows(NumberFormatException.class, () -> {
                app.überprüfeZahlenEingabe(eingabe.trim());
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"50", "-1", "1 23 49 50", "0"})
        public void invalidZahlenraumEingabeLotto(String eingabe) {
            app.lotterie = new Lotto();
            try {
                app.überprüfeZahlenEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Die Zahlen bei Lotto dürfen nur zwischen 1 und 49.", e.getMessage());
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {"51", "-1", "1 23 49 51", "0"})
        public void invalidZahlenraumFalscherEurojackpot(String eingabe) {
            app.lotterie = new Eurojackpot();
            try {
                app.überprüfeZahlenEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Die Zahlen bei Eurojackpot dürfen nur zwischen 1 und 50.", e.getMessage());
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {"1 2 3 4 5 6 7"})
        public void invalidAnzahlEingabe(String eingabe) {
            app.lotterie = new Eurojackpot();
            try {
                app.überprüfeZahlenEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.", e.getMessage());
            }
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"31"})
        public void invalidAnzahlMaxGespeicherterZahlen(String eingabe) {
            app.lotterie = new Eurojackpot();
            for(int i = 1; i <= 30; i++){
                app.unglückszahlen.add(i);
            }

            try {
                app.überprüfeZahlenEingabe(eingabe.trim());
                Assertions.fail("Expected IllegalStateException to be thrown.");
            } catch (IllegalStateException e) {
                Assertions.assertEquals(">> Mit den eingebenen Zahlen würde das Maximum von 30 gespeicherten Zahlen überschritten werden.", e.getMessage());
            }
            app.unglückszahlen.clear();
        }
    }
    
    @Nested
    class HandleZahlenEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"3 49 24 40 1\n"})
        public void validZahlEingabeLotto(String eingabe) {
            app.lotterie = new Lotto();
            
            List<Integer> erwarteteUnglückszahlen = List.of(3, 49, 24, 40, 1);

            app.handleZahlenEingabe(new Scanner(eingabe));
            List<Integer> tatsächlichUnglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, tatsächlichUnglückszahlen);
        }

        @ParameterizedTest
        @ValueSource(strings = {"3 50 24 40 1\n"})
        public void validZahlEingabeEurojackpot(String eingabe) {
            app.lotterie = new Eurojackpot();
            
            List<Integer> erwarteteUnglückszahlen = List.of(3, 50, 24, 40, 1);

            app.handleZahlenEingabe(new Scanner(eingabe));
            List<Integer> unglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
        }

        @ParameterizedTest
        @ValueSource(strings = {"\n", "    \n"})
        public void validLeereEingabe(String eingabe) {
            app.lotterie = new Lotto();
            
            List<Integer> erwarteteUnglückszahlen = new ArrayList<>();

            app.handleZahlenEingabe(new Scanner(eingabe));
            List<Integer> tatsächlichUnglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, tatsächlichUnglückszahlen);
        }
    }
    
    @Nested
    class ÜberprüfeLöschEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "1 2 3 4 5 6 7 8 9 10", "50", "  1  ", "1  2 33  14 5  46", "1 1 1 1"})
        public void validZahlenEingabe(String eingabe) {
            boolean result = app.überprüfeLöschEingabe(eingabe.trim());
            Assertions.assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot.", "1.23", "1,23"})
        public void invalidZahlenEingabe(String eingabe) {
            Assertions.assertThrows(NumberFormatException.class, () -> {
                app.überprüfeLöschEingabe(eingabe.trim());
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"51", "-1", "1 23 49 51"})
        public void invalidZahlenraumEingabe(String eingabe) {
            try {
                app.überprüfeLöschEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.", e.getMessage());
            }
        }
    }
    
    @Nested
    class HandleLöschEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"3 40 1\n", "3 40  1\n"})
        public void validLöschEingabe(String eingabe) {
            
            app.dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

            List<Integer> erwarteteUnglückszahlen = List.of(13, 24);

            app.handleLöschEingabe(new Scanner(eingabe));
            List<Integer> tatsächlichUnglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, tatsächlichUnglückszahlen);
        }

        @ParameterizedTest
        @ValueSource(strings = {"Alle\n", "alle\n", "ALLE\n", "  alle\n  "})
        public void validLöschEingabeAlle(String eingabe) {
            
            app.dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

            List<Integer> erwarteteUnglückszahlen = new ArrayList<>();

            app.handleLöschEingabe(new Scanner(eingabe));
            List<Integer> tatsächlichUnglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, tatsächlichUnglückszahlen);
        }

        @ParameterizedTest
        @ValueSource(strings = {"\n", "   \n"})
        public void validLöschEingabeLeer(String eingabe) {
            
            app.dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

            List<Integer> erwarteteUnglückszahlen = List.of(1, 3, 13, 24, 40);

            app.handleLöschEingabe(new Scanner(eingabe));
            List<Integer> tatsächlichUnglückszahlen = app.unglückszahlen;
            Assertions.assertEquals(erwarteteUnglückszahlen, tatsächlichUnglückszahlen);
        }
    }
    
    @Nested
    class ÜberprüfeAntwortEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"Ja", "ja", "JA", "   ja   ", "Nein", "nein", "NEIN", "   nein   "})
        public void validEingabe(String eingabe) {
            boolean result = app.überprüfeAntwortEingabe(eingabe.trim());
            Assertions.assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot.", "123", "", "  "})
        public void invalidEingabe(String eingabe) {
            try {
                app.überprüfeAntwortEingabe(eingabe.trim());
                Assertions.fail("Expected InputMismatchException to be thrown.");
            } catch (InputMismatchException e) {
                Assertions.assertEquals(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.", e.getMessage());
            }
        }
    }
    
    @Nested
    class HandleAntwortEingabeTests {
        @ParameterizedTest
        @ValueSource(strings = {"Ja\n"})
        public void validEingabeJa(String eingabe) {
            
            Assertions.assertEquals("ja", app.handleAntwortEingabe(new Scanner(eingabe)));
        }
        @ParameterizedTest
        @ValueSource(strings = {"Nein\n"})
        public void validEingabeNein(String eingabe) {

            Assertions.assertEquals("nein", app.handleAntwortEingabe(new Scanner(eingabe)));
        }
    }

    @Nested
    class FügeZahlenEinTests {
        @ParameterizedTest
        @ValueSource(strings = {"51 6"})
        public void validEingabe(String eingabe){
            List<Integer> neueZahlen = new ArrayList<>(Arrays.asList(1, 22, 13, 44));
            List<Integer> erwarteteZahlen = Arrays.asList(1, 22, 13, 44, 51, 6);

            List<Integer> tatsächlicheZahlen = app.fügeZahlenEin(neueZahlen, eingabe);
            Assertions.assertEquals(erwarteteZahlen, tatsächlicheZahlen);
        }
    }
}

