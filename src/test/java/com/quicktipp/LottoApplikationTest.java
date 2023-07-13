package com.quicktipp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;

public class LottoApplikationTest {
    
    LottoApplikation app;
    DateiUtil dateiUtil;
    InputStream inputStream;

    @BeforeEach
    public void setUp() {
        app = new LottoApplikation();
        dateiUtil = new DateiUtil();
        String input = "";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"Lotto", "lotto", "LOTTO", "  lotto  ", "Eurojackpot", "eurojackpot", "EUROJACKPOT", "  Eurojackpot  ", "", "    " })
    public void testÜberprüfeLotterieEingabe_validEingabe(String eingabe) {
        boolean result = app.überprüfeLotterieEingabe(eingabe.trim());
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot."})
    public void testÜberprüfeLotterieEingabe_invalidEingabeException(String eingabe) {
        try {
            app.überprüfeLotterieEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Bitte geben Sie entweder Lotto oder Eurojackpot an.", e.getMessage());
        }
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"Lotto\n", "lotto\n", "LOTTO\n", "  lotto  \n", "\n", "    \n" })
    public void testHandleLotterieEingabe_lottoEingabe(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Assertions.assertDoesNotThrow(() -> {
            app.handleLotterieEingabe(new Scanner(System.in));
        });
        assertTrue(app.lotterie instanceof Lotto);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Eurojackpot\n", "eurojackpot\n", "EUROJACKPOT\n", "  Eurojackpot  \n" })
    public void testHandleLotterieEingabe_eurojackpotEingabe(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        app.handleLotterieEingabe(new Scanner(System.in));
        assertTrue(app.lotterie instanceof Eurojackpot);
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "49", "  1  "})
    public void testÜberprüfeZahlenEingabe_validEingabeLotto(String eingabe) {
        app.lotterie = new Lotto();
        boolean result = app.überprüfeZahlenEingabe(eingabe.trim());
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "50", "  1  "})
    public void testÜberprüfeZahlenEingabe_validEingabeEuroJackpot(String eingabe) {
        app.lotterie = new Eurojackpot();
        boolean result = app.überprüfeZahlenEingabe(eingabe.trim());
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot."})
    public void testÜberprüfeZahlenEingabe_invalidEingabeKeineZahlen(String eingabe) {
        app.lotterie = new Lotto();
        Assertions.assertThrows(NumberFormatException.class, () -> {
            app.überprüfeZahlenEingabe(eingabe.trim());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"50", "-1", "1 23 49 50"})
    public void testÜberprüfeZahlenEingabe_invalidEingabeFalscherZahlenraumLotto(String eingabe) {
        app.lotterie = new Lotto();
        try {
            app.überprüfeZahlenEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"51", "-1", "1 23 49 51"})
    public void testÜberprüfeZahlenEingabe_invalidEingabeFalscherZahlenraumEurojackpot(String eingabe) {
        app.lotterie = new Eurojackpot();
        try {
            app.überprüfeZahlenEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"1 2 3 4 5 6 7"})
    public void testÜberprüfeZahlenEingabe_invalidEingabeFalscheAnzahl(String eingabe) {
        app.lotterie = new Eurojackpot();
        try {
            app.überprüfeZahlenEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Sie können nur bis zu 6 Zahlen eingeben.", e.getMessage());
        }
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"3 13 24 40 1\n"})
    public void testHandleZahlenEingabe_lottoEingabeLotto(String eingabe) {
        app.lotterie = new Lotto();
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        List<Integer> erwarteteUnglückszahlen = List.of(3, 13, 24, 40, 1);

        app.handleZahlenEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"3 13 24 40 1\n"})
    public void testHandleZahlenEingabe_lottoEingabeEurojackpot(String eingabe) {
        app.lotterie = new Lotto();
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        List<Integer> erwarteteUnglückszahlen = List.of(3, 13, 24, 40, 1);

        app.handleZahlenEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\n"})
    public void testHandleZahlenEingabe_lottoLeereEingabe(String eingabe) {
        app.lotterie = new Lotto();
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        List<Integer> erwarteteUnglückszahlen = new ArrayList<>();

        app.handleZahlenEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"1", "1 2", "1 2 3", "1 2 3 4 5 6", "50", "  1  "})
    public void testÜberprüfeLöschEingabe_validEingabe(String eingabe) {
        boolean result = app.überprüfeLöschEingabe(eingabe.trim());
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot."})
    public void testÜberprüfeLöschEingabe_invalidEingabeKeineZahlen(String eingabe) {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            app.überprüfeLöschEingabe(eingabe.trim());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"51", "-1", "1 23 49 51"})
    public void testÜberprüfeLöschEingabe_invalidEingabeFalscherZahlenraumLotto(String eingabe) {
        try {
            app.überprüfeLöschEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Es gibt Zahlen, die nicht im korrekten Zahlenraum liegen.", e.getMessage());
        }
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"3 40 1\n"})
    public void testHandleLöschEingabe_lottoEingabe(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        dateiUtil.löscheAlleUnglückszahlen();
        dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

        List<Integer> erwarteteUnglückszahlen = List.of(13, 24);

        app.handleLöschEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Alle\n", "alle\n", "ALLE\n", "  alle\n  "})
    public void testHandleLöschEingabe_lottoEingabeAlle(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        dateiUtil.löscheAlleUnglückszahlen();
        dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

        List<Integer> erwarteteUnglückszahlen = new ArrayList<>();

        app.handleLöschEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\n", "   \n"})
    public void testHandleLöschEingabe_lottoEingabeLeer(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        dateiUtil.löscheAlleUnglückszahlen();
        dateiUtil.speichereUnglückszahlen(Arrays.asList(3, 13, 24, 40, 1));

        List<Integer> erwarteteUnglückszahlen = List.of(1, 3, 13, 24, 40);

        app.handleLöschEingabe(new Scanner(System.in));
        List<Integer> unglückszahlen = app.unglückszahlen;
        Assertions.assertEquals(erwarteteUnglückszahlen, unglückszahlen);
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"Ja", "ja", "JA", "   ja   ", "Nein", "nein", "NEIN", "   nein   "})
    public void testÜberprüfeAntwortEingabe_validEingabe(String eingabe) {
        boolean result = app.überprüfeAntwortEingabe(eingabe.trim());
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "Latte", "a", "  a  ", "!@#$%", "Lotto!", "Eurojackpot.", "123", "", "  "})
    public void testÜberprüfeAntwortEingabe_invalidEingabeFalscherZahlenraumLotto(String eingabe) {
        try {
            app.überprüfeAntwortEingabe(eingabe.trim());
            Assertions.fail("Expected InputMismatchException to be thrown.");
        } catch (InputMismatchException e) {
            Assertions.assertEquals(">> Inkorrekte Eingabe. Bitte geben Sie entweder Ja oder Nein an.", e.getMessage());
        }
    }
    /*
     * 
     */
    @ParameterizedTest
    @ValueSource(strings = {"Ja\n"})
    public void testHandleAntwortEingabe_EingabeJa(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Assertions.assertEquals("ja", app.handleAntwortEingabe(new Scanner(System.in)));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Nein\n"})
    public void testHandleAntwortEingabe_EingabeNein(String eingabe) {
        String input = eingabe;
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Assertions.assertEquals("nein", app.handleAntwortEingabe(new Scanner(System.in)));
    }
}

