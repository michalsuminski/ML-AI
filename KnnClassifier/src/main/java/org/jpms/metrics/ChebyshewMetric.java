package org.jpms.metrics;

import org.jpms.features.Feature;

import java.util.ArrayList;
import java.util.List;

public class ChebyshewMetric {
    // obliczamy odległość tylko dla tych elementow wektora ktore maja wartosci int w zakresie [0,6] lub są cechami slownikowymi
    public double calculateChebyshewLength(List<Feature> testedVector, List<Feature> classifiedVector, int lengthOfSubstrings) {
        List<Double> lengths = new ArrayList<>(); // lista przechowująca odleglosci cechom o tych samych indeksach w wektorze testowym i zaklasyfikowanym
        nGramMetric nGramMetric = new nGramMetric();
        for (int i = 0; i < testedVector.size(); i++) {
            Feature currentTestedVector = testedVector.get(i); // aktualny element wektora testowego
            Feature currentClassifiedVector = classifiedVector.get(i); // aktualny element wektora zaklasyfikowanego
            if (currentTestedVector.getNumberFeature() == 0 && currentClassifiedVector.getNumberFeature() == 0 &&
                    currentTestedVector.getTextFeature() != null && currentClassifiedVector.getTextFeature() != null) { //cecha slownikowa
                lengths.add(1 - nGramMetric.compareWords(currentTestedVector.getTextFeature(),
                        currentClassifiedVector.getTextFeature(), lengthOfSubstrings));
                // cecha slownikowa kiedy text jest równa null
            }else if (currentTestedVector.getNumberFeature() == 0 && currentClassifiedVector.getNumberFeature() == 0 &&
                    (currentTestedVector.getTextFeature() == null || currentClassifiedVector.getTextFeature() == null)) { //cecha slownikowa
                lengths.add(1.0);
                // cecha liczbowa kiedy cecha liczbowa jest różna od 0 i w przedziale [-1,6]
            } else if (currentTestedVector.getNumberFeature() >= -1 && currentTestedVector.getNumberFeature() <= 6 && // cecha liczbowa
                    currentClassifiedVector.getNumberFeature() >= -1 && currentClassifiedVector.getNumberFeature() <= 6 &&
                    currentTestedVector.getNumberFeature() != 0 && currentClassifiedVector.getNumberFeature() != 0) {
                int numberFeatureTested = currentTestedVector.getNumberFeature();
                int numberFeatureClassified = currentClassifiedVector.getNumberFeature();
                lengths.add((double) Math.abs(numberFeatureTested - numberFeatureClassified));
            } else {  // w przypadku innych sytuacji dystans nie zostanie zmodyfikowany
                lengths.add((double) 0);
            }
        }
        // z listy lengths wybieramy element o najwiekszej wartosci i zwracamy go
        double maxLength = 0;
        for (Double length : lengths) {
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }
}


