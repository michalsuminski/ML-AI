package org.jpms;

import org.jpms.features.Document;
import org.jpms.features.FOText;
import org.jpms.features.Feature;
import org.jpms.knn.KnnClassifier;
import org.jpms.metrics.ChebyshewMetric;
import org.jpms.metrics.CityMetric;
import org.jpms.metrics.EuclidesMetric;
import org.jpms.metrics.QualityMeasures;
import org.jpms.text_operators.FileOperator;
import org.jpms.text_operators.TextCleaner;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jpms.knn.KnnClassifier.*;
import static org.jpms.text_operators.FileOperator.getNDocuments;

/**
 * Hello world!
 */
public class App {
    private static Scanner scanner;

    public static void main(String[] args) throws IOException {
        System.out.println("Jan Płoszaj 229985, Michal Sumiński 230013");
        System.out.println("Klasyfikacja dokumentów tekstowych");
        System.out.println();

        scanner = new Scanner(System.in);
        System.out.println("Enter number of documents to test: ");
        int numberOfDocuments = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter percentage of vector to learn: ");
        int percentageOfTestsAndLearnedVectors = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter value of k-nearest neighbours: ");
        int kNearestNeighbours = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter name of the metric euclides[1], city[2], chebyshew[3]: ");
        int chosenMetric = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter length of the nGramSubstring: ");
        int nGramSubstringElementLength = scanner.nextInt();
        scanner.nextLine();

        //funkcja na pobranie dokumentów-max 13441
        ArrayList<Document> documents = getNDocuments(numberOfDocuments);
        System.out.println(numberOfDocuments + " documents were read");
        //wywolanie klasyfikatora i zwrot training
        System.out.println("Classification is working...");
        List<Document> trainingSet = classifyTrainingDocuments(documents, percentageOfTestsAndLearnedVectors,
                kNearestNeighbours, chosenMetric, nGramSubstringElementLength);

        System.out.println("Classification done");
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println("Summary");
        System.out.println("--------------------------------------------");
        System.out.println();
        QualityMeasures qm = new QualityMeasures();


        System.out.println("Parameters");
        System.out.println("--------------------------------------------");
        System.out.println("Number of documents:" + numberOfDocuments);
        System.out.println("Ratio Of Learned to Tested Vectors: " + percentageOfTestsAndLearnedVectors + "/" + Integer.toString(100-percentageOfTestsAndLearnedVectors));
        System.out.println("K NearestNeighbours: " + kNearestNeighbours);
        System.out.println("Chosen metric: " + chosenMetric);
        System.out.println("Length of nGram substring: " + nGramSubstringElementLength);
        System.out.println();
        System.out.println("Classification quality measures");
        System.out.println("--------------------------------------------");
        System.out.println("Accuracy: " + qm.countAccuracy(trainingSet));

        System.out.println("Recall usa: " + qm.countRecallForOneClass(trainingSet, "usa"));
        System.out.println("Precision usa: " + qm.countPrecisionForOneClass(trainingSet, "usa"));
        System.out.println("F1 usa: " + qm.countF1ForOneClass(trainingSet, "usa"));

        System.out.println("Recall canada: " + qm.countRecallForOneClass(trainingSet, "canada"));
        System.out.println("Precision canada: " + qm.countPrecisionForOneClass(trainingSet, "canada"));
        System.out.println("F1 canada: " + qm.countF1ForOneClass(trainingSet, "canada"));

        System.out.println("Recall japan: " + qm.countRecallForOneClass(trainingSet, "japan"));
        System.out.println("Precision japan: " + qm.countPrecisionForOneClass(trainingSet, "japan"));
        System.out.println("F1 japan: " + qm.countF1ForOneClass(trainingSet, "japan"));

        System.out.println("Recall uk: " + qm.countRecallForOneClass(trainingSet, "uk"));
        System.out.println("Precision uk: " + qm.countPrecisionForOneClass(trainingSet, "uk"));
        System.out.println("F1 uk: " + qm.countF1ForOneClass(trainingSet, "uk"));

        System.out.println("Recall france: " + qm.countRecallForOneClass(trainingSet, "france"));
        System.out.println("Precision france: " + qm.countPrecisionForOneClass(trainingSet, "france"));
        System.out.println("F1 france: " + qm.countF1ForOneClass(trainingSet, "france"));

        System.out.println("Recall west-germany: " + qm.countRecallForOneClass(trainingSet, "west-germany"));
        System.out.println("Precision west-germany: " + qm.countPrecisionForOneClass(trainingSet, "west-germany"));
        System.out.println("F1 west-germany: " + qm.countF1ForOneClass(trainingSet, "west-germany"));

        System.out.println("Recall ALL: " + qm.countRecallForAll(trainingSet));
        System.out.println("Precision ALL: " + qm.countPrecisionForAll(trainingSet));
        System.out.println("F1 ALL: " + qm.countF1ForAll(trainingSet));
    }
}



