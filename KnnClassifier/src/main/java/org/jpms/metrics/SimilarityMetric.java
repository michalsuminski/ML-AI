package org.jpms.metrics;

public interface SimilarityMetric {
    int howManyWordsAreContained(String testedString, String clasifiedString,
                                 int greaterLengthOfTwoStrings, int lengthOfSubstrings);
    double compareWords(String testedString, String clasifiedString, int lengthOfSubstrings);
}
