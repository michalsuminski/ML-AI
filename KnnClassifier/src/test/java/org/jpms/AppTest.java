package org.jpms;

import static org.junit.Assert.assertEquals;

import org.jpms.features.*;
import org.jpms.text_operators.TextCleaner;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    String tmp = "Kannada Influences periods; Old Kannada is a Southern Dravidian periods; Old Kannada language periods; Old Kannada, and according to Dravidian scholar Sanford " +
            "Steever, its history can be conventionally divided into three periods; Old Kannada (halegannada) from " +
            "450–1200 A.D., Middle Kannada (Nadugannada) from 1200–1700 A.D., and Modern Kannada from 1700 to the " +
            "present.[20] Kannada is influenced to an appreciable history can be conventionally extent by Sanskrit. Influences of other languages " +
            "such as Prakrit and Pali history can be conventionally periods; Old Kannada can also be found history can be conventionally in Kannada language.";

    TextCleaner tc = new TextCleaner();
    ArrayList<String> words = tc.cleanAndStem(tmp);

    String tmp2 = "ala ma kota,a kot ma ale";

    ArrayList<String> words2 = tc.cleanAndStem(tmp2);

    ArrayList<ArrayList<String>> dictionary = new ArrayList<ArrayList<String>>();

    ArrayList<String> one =
            Stream.of(("kannada influenc,miała baba placek").split(","))
                    .collect(Collectors.toCollection(ArrayList<String>::new));

    ArrayList<String> two =
            Stream.of(("dupa dupa,histori convention").split(","))
                    .collect(Collectors.toCollection(ArrayList<String>::new));

    ArrayList<String> three =
            Stream.of(("period old kannada,kannada nadugannada").split(","))
                    .collect(Collectors.toCollection(ArrayList<String>::new));

    public AppTest() throws IOException {
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void FONumberTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new FONumber().extractFeature(words, dictionary);

        assertEquals(1, f.getNumberFeature());
    }

    // test w przypadku braku wystapienia danej cechy
    @Test
    public void FONumberTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new FONumber().extractFeature(words2, dictionary);

        assertEquals(0, f.getNumberFeature());
    }

    @Test
    public void FOTextTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new FOText().extractFeature(words, dictionary);

        assertEquals("kannada influenc", f.getTextFeature());
    }

    // test w przypadku braku wystapienia danej cechy
    @Test
    public void FOTextTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new FOText().extractFeature(words2, dictionary);

        assertEquals("brak", f.getTextFeature());
    }

    @Test
    public void LONumberTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new LONumber().extractFeature(words, dictionary);

        assertEquals(2, f.getNumberFeature());
    }

    // test w przypadku braku wystapienia danej cechy
    @Test
    public void LONumberTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new LONumber().extractFeature(words2, dictionary);

        assertEquals(0, f.getNumberFeature());
    }

    @Test
    public void LOTextTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new LOText().extractFeature(words, dictionary);

        assertEquals("histori convention", f.getTextFeature());
    }
    // test w przypadku braku wystapienia danej cechy
    @Test
    public void LOTextTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new LOText().extractFeature(words2, dictionary);

        assertEquals("brak", f.getTextFeature());
    }


    @Test
    public void MONumberTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new MONumber().extractFeature(words, dictionary);

        assertEquals(3, f.getNumberFeature());
    }

    // test w przypadku braku wystapienia danej cechy
    @Test
    public void MONumberTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new MONumber().extractFeature(words2, dictionary);

        assertEquals(0, f.getNumberFeature());
    }

    @Test
    public void MOTextTest()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new MOText().extractFeature(words, dictionary);

        assertEquals("period old kannada", f.getTextFeature());
    }

    // test w przypadku braku wystapienia danej cechy
    @Test
    public void MOTextTestNegative()
    {
        dictionary.add(one);
        dictionary.add(two);
        dictionary.add(three);

        Feature f = new MOText().extractFeature(words2, dictionary);

        assertEquals("brak", f.getTextFeature());
    }
}
