package org.jpms.features;

import org.jpms.features.Feature;
import org.jpms.features.FeatureExtractor;

import java.util.ArrayList;

public interface MostOccurences extends FeatureExtractor {
    @Override
    Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary);
}
