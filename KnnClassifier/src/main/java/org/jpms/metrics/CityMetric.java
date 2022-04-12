package org.jpms.metrics;

import org.jpms.features.Feature;

import java.util.ArrayList;
import java.util.List;

public class CityMetric {
    // obliczamy odległość tylko dla tych elementow wektora ktore maja wartosci int w zakresie [0,6] lub są cechami slownikowymi
    public double calculateCityLength(List<Feature> testedVector, List<Feature> classifiedVector, int lengthOfSubstrings) {
        double length = 0; // length - odleglosc dwoch wektorów
        nGramMetric nGramMetric = new nGramMetric();
        for (int i = 0; i < testedVector.size(); i++) {
            Feature currentTestedVector = testedVector.get(i); // aktualny element wektora testowego
            Feature currentClassifiedVector = classifiedVector.get(i); // aktualny element wektora zaklasyfikowanego
            // cecha slownikowa kiedy text jest rozny od null
            if (currentTestedVector.getNumberFeature() == 0 && currentClassifiedVector.getNumberFeature() == 0 &&
                    currentTestedVector.getTextFeature() != null && currentClassifiedVector.getTextFeature() != null) { //cecha slownikowa
                length += 1 - nGramMetric.compareWords(currentTestedVector.getTextFeature(),
                        currentClassifiedVector.getTextFeature(), lengthOfSubstrings);
                // cecha slownikowa kiedy text jest równa null
            }else if (currentTestedVector.getNumberFeature() == 0 && currentClassifiedVector.getNumberFeature() == 0 &&
                    (currentTestedVector.getTextFeature() == null || currentClassifiedVector.getTextFeature() == null)) { //cecha slownikowa
                length += 1;
                // cecha liczbowa kiedy cecha liczbowa jest różna od 0 i w przedziale [-1,6]
            } else if (currentTestedVector.getNumberFeature() >= -1 && currentTestedVector.getNumberFeature() <= 6 && // cecha liczbowa
                    currentClassifiedVector.getNumberFeature() >= -1 && currentClassifiedVector.getNumberFeature() <= 6 &&
                    currentTestedVector.getNumberFeature() != 0 && currentClassifiedVector.getNumberFeature() != 0) {
                int numberFeatureTested = currentTestedVector.getNumberFeature();
                int numberFeatureClassified = currentClassifiedVector.getNumberFeature();
                length += Math.abs(numberFeatureTested - numberFeatureClassified);
            } else {  // w przypadku innych sytuacji dystans nie zostanie zmodyfikowany
                length += 0;
            }
        }
        return length;
    }

    public static void main(String[] args) {
        List<Feature> test = new ArrayList<>();
        test.add(new Feature(2));
        test.add(new Feature(6));
        test.add(new Feature(5));
        test.add(new Feature(2));
        test.add(new Feature("summary"));
        test.add(new Feature("car"));
        List<Feature> classified = new ArrayList<>();
        classified.add(new Feature(1));
        classified.add(new Feature(4));
        classified.add(new Feature(0));
        classified.add(new Feature(3));
        classified.add(new Feature("summarization"));
        classified.add(new Feature("carpet"));
        CityMetric cityMetric = new CityMetric();
        System.out.println(cityMetric.calculateCityLength(test, classified, 3));
    }
}
