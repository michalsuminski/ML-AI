package org.jpms.features;

import java.util.ArrayList;

public interface FirstOccurence extends FeatureExtractor {
    @Override
    Feature extractFeature(ArrayList<String> document, ArrayList<ArrayList<String>> featureDictionary);
}
