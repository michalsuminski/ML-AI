package org.jpms.knn;

import org.jpms.features.Document;
import org.jpms.metrics.ChebyshewMetric;
import org.jpms.metrics.CityMetric;
import org.jpms.metrics.EuclidesMetric;

import javax.print.Doc;
import java.util.*;

public class KnnClassifier {
    public static ArrayList<Document> createTrainingSet(ArrayList<Document> documents,
                                                        double percentageOfTestsAndLearnedVectors) {
        ArrayList<Document> trainingSet = new ArrayList<>();
        int sizeOfTrainingSet = (int) (documents.size() * percentageOfTestsAndLearnedVectors/100);
        Random rng = new Random();
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < sizeOfTrainingSet)
        {
            Integer next = rng.nextInt(documents.size());
            generated.add(next);
        }
        for (Integer index :generated) {
            trainingSet.add(documents.get(index));
        }
       return trainingSet;
    }

    public static List<Document> createTrainingSetTrim(ArrayList<Document> documents,
                                                       double percentageOfTestsAndLearnedVectors) {
        int sizeOfTrainingSet = (int) (documents.size() * (100 - percentageOfTestsAndLearnedVectors) / 100);
        int firstIndexOfTrainingDoc = documents.size() - sizeOfTrainingSet;
        return documents.subList(firstIndexOfTrainingDoc, documents.size());
    }


    public static List<Document> createLearningSetTrim(ArrayList<Document> documents,
                                                       double percentageOfTestsAndLearnedVectors) {
        int sizeOfLearningSet = (int) (documents.size() * percentageOfTestsAndLearnedVectors/100);
        return documents.subList(0, sizeOfLearningSet);

    }

    public static List<Document> classifyTrainingDocuments(ArrayList<Document> documents, int percentageOfTestsAndLearnedVectors, int kNearestNeighbours,
                                                    int chosenMetric, int nGramSubstringElementLength) {
        int index;
        String country;
        int usaCounter;
        int ukCounter;
        int canadaCounter;
        int franceCounter;
        int japanCounter;
        int westGermanyCounter;
        int max;
        List<Document> learningSet = createLearningSetTrim(documents, percentageOfTestsAndLearnedVectors);
        List<Document> trainingSet = createTrainingSetTrim(documents, percentageOfTestsAndLearnedVectors);
        documents = null;
        EuclidesMetric euclidesMetric = new EuclidesMetric();
        ChebyshewMetric chebyshewMetric = new ChebyshewMetric();
        CityMetric cityMetric = new CityMetric();
        for (Document document : trainingSet) {
//          dostac aktualny wektor i obliczyc mu odleglosci od wektorow uczacych
            for (int j = 0; j < learningSet.size(); j++) {
                if (chosenMetric == 1) { //euklides
                    document.getDistanceFromTrainingVectors().add(new ArrayList<>(Arrays.asList(cityMetric.calculateCityLength(document.getFeatures(),
                            learningSet.get(j).getFeatures(), nGramSubstringElementLength), (double) j)));
                } else if (chosenMetric == 2) {//city
                    document.getDistanceFromTrainingVectors().add(new ArrayList<>(Arrays.asList(euclidesMetric.calculateEuclidesLength(document.getFeatures(),
                            learningSet.get(j).getFeatures(), nGramSubstringElementLength), (double) j)));
                } else { //3 -> chebyshew
                    document.getDistanceFromTrainingVectors().add(new ArrayList<>(Arrays.asList(chebyshewMetric.calculateChebyshewLength(document.getFeatures(),
                            learningSet.get(j).getFeatures(), nGramSubstringElementLength), (double) j)));
                }
            }
            //sortowanie po odleglosciach
            document.sortDistanceFromTrainingVectors();
            // wybranie k elementow
            document.getKNN(kNearestNeighbours);
            // podanie kraju ktory najczesciej wystepuje
            country = "";
            usaCounter = 0;
            ukCounter = 0;
            canadaCounter = 0;
            franceCounter = 0;
            japanCounter = 0;
            westGermanyCounter = 0;
            max = 0;
            for (int l = 0; l < kNearestNeighbours; l++) {
                index = (document.getDistanceFromTrainingVectors().get(l).get(1)).intValue();
                switch (learningSet.get(index).getPlaces()) {
                    case "usa":
                        usaCounter++;
                        if (usaCounter > max) {
                            max = usaCounter;
                            country = "usa";
                        }
                        break;
                    case "west-germany":
                        westGermanyCounter++;
                        if (westGermanyCounter > max) {
                            max = westGermanyCounter;
                            country = "west-germany";
                        }
                        break;
                    case "canada":
                        canadaCounter++;
                        if (canadaCounter > max) {
                            max = canadaCounter;
                            country = "canada";
                        }
                        break;
                    case "japan":
                        japanCounter++;
                        if (japanCounter > max) {
                            max = japanCounter;
                            country = "japan";
                        }
                        break;
                    case "uk":
                        ukCounter++;
                        if (ukCounter > max) {
                            max = ukCounter;
                            country = "uk";
                        }
                        break;
                    case "france":
                        franceCounter++;
                        if (franceCounter > max) {
                            max = franceCounter;
                            country = "france";
                        }
                        break;
                    default:
                        System.out.println("Wrong label");
                        return null;
                }
            }
            document.setPlacesFromClassification(country);
        }
          return trainingSet;
    }

}

