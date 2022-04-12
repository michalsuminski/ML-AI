package org.jpms.features;

import org.jpms.features.Feature;
import org.jpms.features.MostOccurences;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MOText implements MostOccurences {
    @Override
    public Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary) {

        String stringDocument = String.join(" ", document);
        // licznik do najliczniej wystepowanego elementu
        int counterMax = 0;
        boolean flag;
        // "brak" w przypadku braku wystapienia danej cechy tesktowej
        String phraseFeature = null;
        for(int i=0; i<featureDictionary.size(); i++){

            for(int j=0; j<featureDictionary.get(i).size(); j++){
                String onePhrase = featureDictionary.get(i).get(j);
                int counter = 0;

                // jesli w ogóle jedna z fraz z slownika cech wystepujew tekscie to wejdz dalej
                if (stringDocument.contains(onePhrase)){
                    ArrayList<String> onePhraseSplittedToArray =
                            Stream.of(onePhrase.split(" "))
                                    .collect(Collectors.toCollection(ArrayList<String>::new));

                    // algorytm wyszukania pierwszego wystapienia frazy w tekscie
                    for (int k=0; k<document.size(); k++){
                        // szukamy najpierw pierwszego słowa z danej frazy(ze slownika) w tekscie
                        if (document.get(k).equals(onePhraseSplittedToArray.get(0))){
                            flag = true;
                            for (int l=1; l<onePhraseSplittedToArray.size(); l++) {
                                if (document.size() > k+l) {
                                    flag = document.get(k + l).equals(onePhraseSplittedToArray.get(l));
                                }
                            }
                            if (flag){
                                counter++;
                                // w przypadku równej liczby wystapien roznych fraz, jako cecha zostanie przekazana
                                // ta ktora ma wczesniejszy indeks liscie cech
                                if (counter > counterMax){
                                    counterMax = counter;
                                    phraseFeature = onePhrase;
                                }
                            }
                        }
                    }
                }
            }
        }
        return new Feature(phraseFeature);
    }
}
