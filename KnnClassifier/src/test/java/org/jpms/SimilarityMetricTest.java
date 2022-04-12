package org.jpms;

import org.jpms.features.Feature;
import org.jpms.metrics.ChebyshewMetric;
import org.jpms.metrics.CityMetric;
import org.jpms.metrics.EuclidesMetric;
//import org.jpms.metrics.SimilarityMetric;
import org.jpms.metrics.nGramMetric;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimilarityMetricTest {
    List<Feature> testedVector = new ArrayList<>();
    List<Feature> classifiedVector = new ArrayList<>();
    EuclidesMetric euclidesMetric = new EuclidesMetric();
    CityMetric cityMetric = new CityMetric();
    ChebyshewMetric chebyshewMetric = new ChebyshewMetric();
    nGramMetric nGramMetric = new nGramMetric();
    int lengthOfSubstring = 3;


    @Before
    public void setUp() {
        testedVector.add(new Feature(-1));
        testedVector.add(new Feature(6));
        testedVector.add(new Feature(5));
        testedVector.add(new Feature(-1));
        testedVector.add(new Feature("summary"));
        testedVector.add(new Feature("car"));
        classifiedVector.add(new Feature(-1));
        classifiedVector.add(new Feature(4));
        classifiedVector.add(new Feature(0));
        classifiedVector.add(new Feature(4));
        classifiedVector.add(new Feature("summarization"));
        classifiedVector.add(new Feature("carpet"));

    }

    @Test
    public void correctCalculatingOfEuclidesMetric() {
        assertEquals(5.474, euclidesMetric.calculateEuclidesLength(testedVector, classifiedVector, lengthOfSubstring), 0.001);
    }

    @Test
    public void correctCalculatingOfCityMetric() {
        assertEquals(8.386, cityMetric.calculateCityLength(testedVector, classifiedVector, lengthOfSubstring), 0.001);
    }

    @Test
    public void correctCalculatingOfChebyshewMetric() {
        assertEquals(5.0, chebyshewMetric.calculateChebyshewLength(testedVector, classifiedVector, lengthOfSubstring), 0.001);
    }

    @Test
    public void correctNGramValue() {
        assertEquals(0.36, nGramMetric.compareWords("summary", "summarization", 3), 0.01);
    }

    @Test
    public void correctNGramValue2() {
        assertEquals(0.25, nGramMetric.compareWords("car", "carpet", 3), 0.01);
    }

    @Test
    public void correctWordsCount() {
        assertEquals(5, nGramMetric.howManyWordsAreContained("summary", "summarizationary",
                16, 3), 0.01);
    }
}
