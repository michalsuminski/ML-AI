package org.jpms.features;

import org.jpms.features.Feature;
import org.jpms.features.FirstOccurence;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FONumber implements FirstOccurence {
    @Override
    public Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary) {

        String stringDocument = String.join(" ", document);
        // przyjmujemy taka wartosc poczatkowa, jakbysmy nie znalezli zadnego poszukiwanego elementu
        int indexMin = document.size()+1;
        // przyjmujemy tak samo w momencie kiedy nie znajdziemy zadnego poszukiwanego elementu
        int countryOfIndexMin = -1;
        boolean flag;
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
                            if (flag && k < indexMin){
                                indexMin = k;
                                countryOfIndexMin = i;
                            }
                        }
                    }
                }
            }
        }
        // jesli nie znajdziemy poszukiwanego elementu to zwracamy -1
        if (countryOfIndexMin == -1) {
            return new Feature(-1);
        }
        // +1 bo numerujemy od 1
        return new Feature(countryOfIndexMin+1);
    }
}
