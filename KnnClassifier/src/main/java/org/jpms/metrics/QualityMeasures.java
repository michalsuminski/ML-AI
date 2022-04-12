package org.jpms.metrics;

import org.jpms.features.Document;

import java.util.ArrayList;
import java.util.List;

public class QualityMeasures {

    private final int weightWestGermany = 1;
    private final int weightUSA = 4;
    private final int weightFrance = 1;
    private final int weightUK = 3;
    private final int weightCanada = 3;
    private final int weightJapan = 2;

    private ArrayList<Integer> weights = new ArrayList<Integer>();

    public QualityMeasures() {
        weights.add(weightUSA);
        weights.add(weightWestGermany);
        weights.add(weightUK);
        weights.add(weightJapan);
        weights.add(weightCanada);
        weights.add(weightFrance);
    }

    public double countAccuracy(List<Document> documents){
        // liczba wszystkich elementow w zbiorze testowym
        double N = documents.size();
        // licznik do zliczania ile zostało dobrze zaklasyfikowanych
        double goodClassified = 0;
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getPlaces().equals(documents.get(i).getPlacesFromClassification())){
                goodClassified ++;
            }
        }
        return goodClassified/N;
    }

    public double countPrecisionForOneClass(List<Document> documents, String country){
        double trueX = 0;
        double falseX = 0;
        for (int i = 0; i < documents.size(); i++) {
            // np. jesli powinno byc USA i sklasyfikowało do USA
            if (documents.get(i).getPlaces().equals(country) && documents.get(i).getPlacesFromClassification().equals(country)){
                trueX += 1;
            }
            // np. jesli sklasyfikowało do USA, a powinno do innego kraju
            if (!documents.get(i).getPlaces().equals(country) && documents.get(i).getPlacesFromClassification().equals(country)){
                falseX += 1;
            }
        }
        double sum = trueX + falseX;
        if (trueX == 0){
            return 0;
        }
        return trueX/sum;
    }

    // elementy do arraylisty muszą zostać pododawane w odpowiedniej kolejnosci
    public double countPrecisionForAll(List<Document> documents){
        List<Double> precisionsOfCountries = new ArrayList<>();
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "usa"));
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "west-germany"));
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "uk"));
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "canada"));
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "japan"));
        precisionsOfCountries.add(countPrecisionForOneClass(documents, "france"));
        double sumOfPrecisions = 0;
        double sumOfWeights = 0;
        for (int i = 0; i < precisionsOfCountries.size(); i++) {
            sumOfPrecisions += weights.get(i) * precisionsOfCountries.get(i);
            sumOfWeights += weights.get(i);
        }

        return sumOfPrecisions/sumOfWeights;
    }

    public double countRecallForOneClass(List<Document> documents, String country){
        double trueX = 0;
        double notClassifiedX = 0;
        for (int i = 0; i < documents.size(); i++) {
            // np. jesli powinno byc USA i sklasyfikowało do USA
            if (documents.get(i).getPlaces().equals(country) && documents.get(i).getPlacesFromClassification().equals(country)){
                trueX += 1;
            }
            // np. jesli powinno byc USA, a sklasyfikowało do innego kraju
            if (documents.get(i).getPlaces().equals(country) && !documents.get(i).getPlacesFromClassification().equals(country)){
                notClassifiedX += 1;
            }
        }
        double sum = trueX + notClassifiedX;
        if (trueX == 0){
            return 0;
        }
        return trueX/sum;
    }

    // elementy do arraylisty muszą zostać pododawane w odpowiedniej kolejnosci
    public double countRecallForAll(List<Document> documents){
        List<Double> recallsOfCountries = new ArrayList<>();
        recallsOfCountries.add(countRecallForOneClass(documents, "usa"));
        recallsOfCountries.add(countRecallForOneClass(documents, "west-germany"));
        recallsOfCountries.add(countRecallForOneClass(documents, "uk"));
        recallsOfCountries.add(countRecallForOneClass(documents, "canada"));
        recallsOfCountries.add(countRecallForOneClass(documents, "japan"));
        recallsOfCountries.add(countRecallForOneClass(documents, "france"));
        double sumOfRecalls = 0;
        double sumOfWeights = 0;
        for (int i = 0; i < recallsOfCountries.size(); i++) {
            sumOfRecalls += weights.get(i) * recallsOfCountries.get(i);
            sumOfWeights += weights.get(i);
        }
        return sumOfRecalls/sumOfWeights;
    }

    public double countF1ForOneClass(List<Document> documents, String country){
        double trueX = 0;
        double notClassifiedX = 0;
        double falseX = 0;
        for (int i = 0; i < documents.size(); i++) {
            // np. jesli powinno byc USA i sklasyfikowało do USA
            if (documents.get(i).getPlaces().equals(country) && documents.get(i).getPlacesFromClassification().equals(country)){
                trueX += 1;
            }
            // np. jesli powinno byc USA, a sklasyfikowało do innego kraju
            if (documents.get(i).getPlaces().equals(country) && !documents.get(i).getPlacesFromClassification().equals(country)){
                notClassifiedX += 1;
            }
            // np. jesli sklasyfikowało do USA, a powinno do innego kraju
            if (!documents.get(i).getPlaces().equals(country) && documents.get(i).getPlacesFromClassification().equals(country)){
                falseX += 1;
            }
        }
        double sum = 2*trueX + falseX + notClassifiedX;
        if (trueX == 0){
            return 0;
        }
        return 2*trueX/sum;
    }

    // elementy do arraylisty muszą zostać pododawane w odpowiedniej kolejnosci
    public double countF1ForAll(List<Document> documents){
        List<Double> f1OfCountries = new ArrayList<>();
        f1OfCountries.add(countF1ForOneClass(documents, "usa"));
        f1OfCountries.add(countF1ForOneClass(documents, "west-germany"));
        f1OfCountries.add(countF1ForOneClass(documents, "uk"));
        f1OfCountries.add(countF1ForOneClass(documents, "canada"));
        f1OfCountries.add(countF1ForOneClass(documents, "japan"));
        f1OfCountries.add(countF1ForOneClass(documents, "france"));
        double sumOfF1 = 0;
        double sumOfWeights = 0;
        for (int i = 0; i < f1OfCountries.size(); i++) {
            sumOfF1 += weights.get(i) * f1OfCountries.get(i);
            sumOfWeights += weights.get(i);
        }
        return sumOfF1/sumOfWeights;
    }
}
