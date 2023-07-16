package com.quicktipp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateiUtilTest {
    private String testDateiPfad = "TestUnglückszahlen.txt";;
    private DateiUtil dateiUtil;

    @Before
    public void setUp() {
        dateiUtil = new DateiUtil();
        dateiUtil.setDateiPfad(testDateiPfad); 
    }

    @After
    public void tearDown() {
        File testDatei = new File(testDateiPfad);
        if (testDatei.exists()) {
            testDatei.delete();
        }
    }

    @Test
    public void speichereUndLadeUnglückszahlenTest() {
        List<Integer> unglückszahlen = Arrays.asList(13, 4 ,7);

        dateiUtil.speichereUnglückszahlen(unglückszahlen);

        List<Integer> geladeneUnglückszahlen = dateiUtil.ladeUnglückszahlen();

        assertEquals(3, geladeneUnglückszahlen.size());
        assertTrue(geladeneUnglückszahlen.contains(4));
        assertTrue(geladeneUnglückszahlen.contains(7));
        assertTrue(geladeneUnglückszahlen.contains(13));
    }

    @Test
    public void löscheUnglückszahlenTest() {
        List<Integer> unglückszahlen = Arrays.asList(13, 4 ,7, 10);
        dateiUtil.speichereUnglückszahlen(unglückszahlen);

        List<Integer> zahlenZuLöschen = Arrays.asList(7, 10);

        dateiUtil.löscheUnglückszahlen(zahlenZuLöschen);

        List<Integer> geladeneUnglückszahlen = dateiUtil.ladeUnglückszahlen();
        assertEquals(2, geladeneUnglückszahlen.size());
        assertTrue(geladeneUnglückszahlen.contains(4));
        assertTrue(geladeneUnglückszahlen.contains(13));
    }

    @Test
    public void löscheAlleUnglückszahlenTest() {
        List<Integer> unglückszahlen = Arrays.asList(13, 4 ,7);

        dateiUtil.speichereUnglückszahlen(unglückszahlen);

        dateiUtil.löscheAlleUnglückszahlen();

        List<Integer> geladeneUnglückszahlen = dateiUtil.ladeUnglückszahlen();
        assertEquals(0, geladeneUnglückszahlen.size());
    }
}
