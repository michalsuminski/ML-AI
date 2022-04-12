package org.jpms.metrics;

//import org.jpms.metrics.SimilarityMetric;

public class nGramMetric implements SimilarityMetric{

    // funkcja zliczy ile takich samych n-elementowych ciagow 2 Stringi posiadaja
    public int howManyWordsAreContained(String testedString, String classifiedString, int greaterLengthOfTwoStrings, int lengthOfSubstrings) {
        int sum = 0;
        for (int i = 0; i < greaterLengthOfTwoStrings - lengthOfSubstrings + 1; i++) {
            String substringFromClassifiedString = classifiedString.substring(i,lengthOfSubstrings+i);
            if(testedString.contains(substringFromClassifiedString)) sum++;
        }
        return sum;
    }

    // implementacja metody n-gramow
    public double compareWords (String testedString, String classifiedString, int lengthOfSubstrings) {
        // ustalenie parametru N - parametr rowny dlugosci najdluzszego Stringa
        int greaterLengthOfTwoStrings = Math.max(testedString.length(), classifiedString.length());
        if (testedString.length() > classifiedString.length()) {
            int differenceInLength =   testedString.length() - classifiedString.length();
            StringBuilder clasifiedStringBuilder = new StringBuilder(classifiedString);
            for (int i = 0; i < differenceInLength; i++) {
                clasifiedStringBuilder.append(" ");
            }
            classifiedString = clasifiedStringBuilder.toString();
        } else if(testedString.length() < classifiedString.length()) {
            int differenceInLength =   classifiedString.length() - testedString.length();
            StringBuilder testedStringBuilder = new StringBuilder(testedString);
            for (int i = 0; i < differenceInLength; i++) {
                testedStringBuilder.append(" ");
            }
            testedString = testedStringBuilder.toString();
        }
        // teraz stringi są równej długości mozna przystapic do metody
        int sum = howManyWordsAreContained(testedString, classifiedString, greaterLengthOfTwoStrings, lengthOfSubstrings);
        return (double) sum/ (greaterLengthOfTwoStrings - 2);
    }
}
