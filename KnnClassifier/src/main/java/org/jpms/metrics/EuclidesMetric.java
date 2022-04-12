package org.jpms.metrics;

import org.jpms.features.Feature;

import java.util.List;

public class EuclidesMetric {
    // obliczamy odległość tylko dla tych elementow wektora ktore maja wartosci int w zakresie [0,6] lub są cechami slownikowymi
    public double calculateEuclidesLength(List<Feature> testedVector, List<Feature> classifiedVector, int lengthOfSubstrings) {
        nGramMetric nGramMetric = new nGramMetric();
        double length = 0; // length - odleglosc dwoch wektorów
        for (int i = 0; i < testedVector.size(); i++) {
            Feature currentTestedVector = testedVector.get(i); // aktualny element wektora testowego
            Feature currentClassifiedVector = classifiedVector.get(i); // aktualny element wektora zaklasyfikowanego

            // cecha slownikowa kiedy text jest rozny od null
            if (currentTestedVector.getNumberFeature() == 0 && currentClassifiedVector.getNumberFeature() == 0 &&
                    currentTestedVector.getTextFeature() != null && currentClassifiedVector.getTextFeature() != null) { //cecha slownikowa
                length += Math.pow((1 - nGramMetric.compareWords(currentTestedVector.getTextFeature(),
                        currentClassifiedVector.getTextFeature(), lengthOfSubstrings)),2);
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
                length += (numberFeatureTested - numberFeatureClassified) *
                        (numberFeatureTested - numberFeatureClassified);
            } else {  // w przypadku innych sytuacji dystans nie zostanie zmodyfikowany
                length += 0;
            }
        }
        return Math.sqrt(length);
    }
}
