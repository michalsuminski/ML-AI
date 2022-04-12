package org.jpms.features;

import org.jpms.features.Feature;
import org.jpms.features.LastOccurence;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LOText implements LastOccurence {
    @Override
    public Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary) {

        String stringDocument = String.join(" ", document);
        // przyjmujemy taka wartosc poczatkowa, jakbysmy nie znalezli zadnego poszukiwanego elementu
        int indexMin = -1;
        boolean flag;
        // "brak" w przypadku braku wystapienia danej cechy tesktowej
        String phraseFeature = null;
        for(int i=0; i<featureDictionary.size(); i++){

            for(int j=0; j<featureDictionary.get(i).size(); j++){
                String onePhrase = featureDictionary.get(i).get(j);

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
                            if (flag && k > indexMin){
                                indexMin = k;
                                phraseFeature = onePhrase;
                            }
                        }
                    }
                }
            }
        }
        return new Feature(phraseFeature);
    }
}
