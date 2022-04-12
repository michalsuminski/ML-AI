package org.jpms.features;

import org.jpms.text_operators.FileOperator;
import org.jpms.text_operators.TextCleaner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Document {
    // odczytany z pliku tekst
    private String text;
    // text jako lista słów
    private ArrayList<String> stemmedTextAsList = new ArrayList<>();
    // etykieta dokumentu
    private String places;
    // wektor cech dla danego dokumentu
    private ArrayList<Feature> features = new ArrayList<>();
    // etykieta dokumentu z klasyfikacji wykorzystywana tylko dla wektorow testowych
    private String placesFromClassification;

    private List<ArrayList<Double>> distanceFromTrainingVectors;



    public Document(ArrayList<String> textAndPlaces) throws IOException {
        places = textAndPlaces.get(0);
        text = textAndPlaces.get(1);
        // wystemowany tekst
        stemmedTextAsList = new TextCleaner().cleanAndStem(text);
        distanceFromTrainingVectors = new ArrayList<>();
        // operatory do ekstrakcji cech
        FONumber fon = new FONumber();
        FOText fot = new FOText();
//        LONumber lon = new LONumber();
        LOText lot = new LOText();
        MONumber mon = new MONumber();
        MOText mot = new MOText();



        ArrayList<ArrayList<String>> x1 = FileOperator.getFeatureDictionary("src/main/resources/features/x1.txt");
        ArrayList<ArrayList<String>> x2 = FileOperator.getFeatureDictionary("src/main/resources/features/x2.txt");
        ArrayList<ArrayList<String>> x3 = FileOperator.getFeatureDictionary("src/main/resources/features/x3.txt");
        ArrayList<ArrayList<String>> x4 = FileOperator.getFeatureDictionary("src/main/resources/features/x4.txt");
        ArrayList<ArrayList<String>> x5 = FileOperator.getFeatureDictionary("src/main/resources/features/x5.txt");
        ArrayList<ArrayList<String>> x6 = FileOperator.getFeatureDictionary("src/main/resources/features/x6.txt");
        ArrayList<ArrayList<String>> x7 = FileOperator.getFeatureDictionary("src/main/resources/features/x7.txt");
        ArrayList<ArrayList<String>> x8 = FileOperator.getFeatureDictionary("src/main/resources/features/x8.txt");
        ArrayList<ArrayList<String>> x9 = FileOperator.getFeatureDictionary("src/main/resources/features/x9.txt");
        ArrayList<ArrayList<String>> x10 = FileOperator.getFeatureDictionary("src/main/resources/features/x10.txt");

//         Najczęściej występująca nazwa walut
        features.add(mot.extractFeature(stemmedTextAsList, x1));
        // Pierwsza występująca nazwy krainy geograficznych
        features.add(fot.extractFeature(stemmedTextAsList, x2));
        // Ostatnia występująca nazwa charakterystycznego budynki dla danego kraju
        features.add(lot.extractFeature(stemmedTextAsList, x3));
//         Liczba występowania znanych osób zw. z polityką
        features.add(mon.extractFeature(stemmedTextAsList, x4));
        // Pierwsza występujące ważne wydarzenie zw. z krajami w latach 1987-88
        features.add(fon.extractFeature(stemmedTextAsList, x5));
        // Najczęściej występujące charakterystyczne dla krajów nazwy stanowisk
        features.add(mon.extractFeature(stemmedTextAsList, x6));
        // Pierwsza występująca w tekście nazwa dań
        features.add(fot.extractFeature(stemmedTextAsList, x7));
        // Ostatnia występująca w tekście nazwa partii politycznych
        features.add(lot.extractFeature(stemmedTextAsList, x8));
//        // Liczba występowania mias
        features.add(mon.extractFeature(stemmedTextAsList, x9));
        // Pierwsza występująca w tekście nazwa giełdy
        features.add(fon.extractFeature(stemmedTextAsList, x10));
    }

    public void getKNN(int k) {
        distanceFromTrainingVectors = distanceFromTrainingVectors.subList(0, k);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ArrayList<Double>> getDistanceFromTrainingVectors() {
        return distanceFromTrainingVectors;
    }

    public String getPlacesFromClassification() {
        return placesFromClassification;
    }

    public void setPlacesFromClassification(String placesFromClassification) {
        this.placesFromClassification = placesFromClassification;
    }

    public boolean comparePlaceToPlaceFromClassification() {
        return places.equals(placesFromClassification);
    }

    @Override
    public String toString() {
        return "Document{" +
                "features=" + features +
                '}';
    }


    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }

    public void sortDistanceFromTrainingVectors(){
        Collections.sort(distanceFromTrainingVectors, new Comparator<List<Double>>() {
            @Override
            public int compare(List<Double> a, List<Double> b) {
                return a.get(0).compareTo(b.get(0));
            }
        });
    }
}
