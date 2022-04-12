package org.jpms.features;

import org.jpms.features.Feature;

import java.util.ArrayList;

public interface FeatureExtractor {
    Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary);
}
