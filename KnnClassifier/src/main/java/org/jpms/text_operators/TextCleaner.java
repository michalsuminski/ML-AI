package org.jpms.text_operators;

import opennlp.tools.stemmer.PorterStemmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextCleaner {
    public ArrayList<String> removeStopwords(String text) throws IOException {
//        String result = text.replaceAll("[-+.^:,;(){}]","");
        String result = text.replaceAll("[-+.^:,;(){}\"]", "");
        List<String> stopwords;
        stopwords = Files.readAllLines(Paths.get("src/main/resources/stopwords.txt"));
        ArrayList<String> allWords =
                Stream.of(result.split(" "))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(stopwords);
        return allWords;
    }

    public ArrayList<String> performStemmization(List<String> words) {
        PorterStemmer ps = new PorterStemmer();
        ArrayList<String> stemmedWords = new ArrayList<String>();
        for (String word : words) {
            // zamiana tekstu tylko na małe litery
            word = word.toLowerCase(Locale.ROOT);
            String txt = ps.stem(word);
            if (!txt.equals("")) {
                stemmedWords.add(txt);
            }
        }
        return stemmedWords;
    }

    public ArrayList<String> cleanAndStem(String text) throws IOException {
        ArrayList<String> words = removeStopwords(text);
        words = performStemmization(words);
        return words;
    }
}
