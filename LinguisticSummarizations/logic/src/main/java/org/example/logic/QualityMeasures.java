package org.example.logic;

import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;
import org.example.fuzzy.Summary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QualityMeasures {

    // T1
    public double degreeOfTruth(Summary summary) {
        Quantifier quantifier = summary.getQuantifier();
        double r = 0;
        var domain = summary.getSummarizers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain();
        List<Double> listOfMembershipFunctionValues = new ArrayList<>();
        double membershipFunctionValue = 0;
        var numberOfItems = summary.getSummarizers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain().size();
        double record;
        if (summary.getQualifiers() == null) {
            for (int i = 0; i < numberOfItems; i++) {
                for (LinguisticVariableValue summarizer : summary.getSummarizers()) {
                    record = summarizer.getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
                    listOfMembershipFunctionValues.add(summarizer.getFuzzySet().countDegreeOfMembership(record));
                }
                Collections.sort(listOfMembershipFunctionValues);
                membershipFunctionValue = listOfMembershipFunctionValues.get(0);
                listOfMembershipFunctionValues.clear();
                r += membershipFunctionValue;
            }
        } else {
            // przyjmujemy, że jest tylko jeden sumaryzator i jeden kwalifikator
            double summarizerValue;
            double qualifierValue;
            for (int i = 0; i < numberOfItems; i++) {
                summarizerValue = summary.getSummarizers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
                listOfMembershipFunctionValues.add(summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(summarizerValue));

                // przyjmujemy tylko rozmyte kwalifikatory
                qualifierValue = summary.getQualifiers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
                listOfMembershipFunctionValues.add(summary.getQualifiers().get(0).getFuzzySet().countDegreeOfMembership(qualifierValue));

                Collections.sort(listOfMembershipFunctionValues);
                membershipFunctionValue = listOfMembershipFunctionValues.get(0);
                listOfMembershipFunctionValues.clear();
                r += membershipFunctionValue;
            }
            // get(0) bo przyjmujemy tylko jeden kwalifikator
            r = r / summary.getQualifiers().get(0).getFuzzySet().getSigmaCount();
            if (quantifier.isAbsolute()) {
                return -1;
            } else {
                return quantifier.countDegreeOfMembership(r);
            }
        }

        if (quantifier.isAbsolute()) {
            return quantifier.countDegreeOfMembership(r);
        } else {
            double m = domain.size();
            return quantifier.countDegreeOfMembership(r / m);
        }
    }

    // T2
    public double degreeOfImprecision(Summary summary) {
        double result = 1;
        int n = summary.getSummarizers().size();
        for (int i = 0; i < n; i++) {
            result *= summary.getSummarizers().get(i).getFuzzySet().getDegreeOfFuzziness();
        }
        return 1 - Math.pow(result, 1 / (double) n);
    }

    // T3
    public double degreeOfCovering(Summary summary) {
        double t = 0;
        double h = 0;
        if (summary.getQualifiers() == null) {
            for (int i = 0; i < summary.getSummarizers().size(); i++) {
                var domain = summary.getSummarizers().get(i).getFuzzySet().getUniverseOfDiscourse().getDomain();
                for (Double aDouble : domain) {
                    h += 1;
                    var getMembershipFunctionValue = summary.getSummarizers().get(i).getFuzzySet().countDegreeOfMembership(aDouble);
                    if (getMembershipFunctionValue > 0) {
                        t += 1;
                    }
                }

            }
            return t / h;
        } else {
//            for (int i = 0; i < summary.getQualifiers().size(); i++) {
//                var database = summary.getQualifiers().get(i).getFuzzySet().getUniverseOfDiscourse().getDomain();
//                for (Double currentDomainValue : database) {
//                    var valueDegreeOfMembership = summary.getQualifiers().get(i).getFuzzySet().countDegreeOfMembership(currentDomainValue);
//                    if (valueDegreeOfMembership > 0) {
//                        h++;
//                        for (int k = 0; k < summary.getSummarizers().size(); k++) {
//                            if (summary.getSummarizers().get(k).getFuzzySet().countDegreeOfMembership(currentDomainValue) > 0) {
//                                t++;
//                            }
//                        }
//                    }
//                }
//            }

            int numberOfRecords = summary.getQualifiers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain().size();
            boolean flagQualifier;
            boolean flagSummarizer;
            for (int i = 0; i < numberOfRecords; i++) {
                flagQualifier = true;
                flagSummarizer = true;
                // sprawdzenie dla i-tego rekordu wartości każdego fuzzy seta wchodzącego w skład qualifiera
                for (int w = 0; w < summary.getQualifiers().size(); w++) {
                    double argument = summary.getQualifiers().get(w).getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
                    if ( summary.getQualifiers().get(w).getFuzzySet().countDegreeOfMembership(argument) <= 0 ){
                        flagQualifier = false;
                    }
                }
                // sprawdzenie dla i-tego rekordu wartości każdego fuzzy seta wchodzącego w skład sumaryzatora
                for (int s = 0; s < summary.getSummarizers().size(); s++) {
                    double argument = summary.getSummarizers().get(s).getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
                    if ( summary.getSummarizers().get(s).getFuzzySet().countDegreeOfMembership(argument) <= 0 ){
                        flagSummarizer = false;
                    }
                }

                // ti inkrementujemy tylko jeśli obie flagi są true
                if (flagQualifier && flagSummarizer) {
                    t+=1;
                }
                // hi inkrementujemy jeśli kwalifikator był wiekszy od 0
                if (flagQualifier){
                    h+=1;
                }
            }
//            System.out.println("T: " + t);
//            System.out.println("H: " + h);
            return t / h;
        }
    }


    // T4
    public double degreeOfAppropriateness(Summary summary) {
        double result = 1;
        int n = summary.getSummarizers().size();
        double r;
        double sumOfG;
        List<Double> database;
        for (int i = 0; i < n; i++) {
            database = summary.getSummarizers().get(i).getFuzzySet().getUniverseOfDiscourse().getDomain();
            sumOfG = 0;
            for (Double record : database) {
                if (summary.getSummarizers().get(i).getFuzzySet().countDegreeOfMembership(record) > 0) {
                    sumOfG += 1;
                }
            }
            r = sumOfG / database.size();
            result *= r;
        }
        return Math.abs(result - degreeOfCovering(summary));
    }

    // T5 -  długość podsumowania
    public static double LengthOfSummary(Summary summary) {
        int summarizersSize = summary.getSummarizers().size();
        return 2 * Math.pow(1.0 / 2.0, summarizersSize);
    }

    // T6
    public double degreeOfQuantifierImprecision(Summary summary) {
        return 1 - summary.getQuantifier().getFuzzySet().getDegreeOfFuzziness();
    }

    // T7 - stopień liczności kwantyfikatora
    public static double DegreeOfQuantifierCardinality(Summary summary) {
        double result = summary.getQuantifier().getFuzzySet().getCLM();
        if (summary.getQuantifier().isAbsolute()) {

            result /= summary.getSummarizers().get(0).getFuzzySet().getUniverseOfDiscourse().getDomain().size();
        }
        return 1 - result;
    }

    // T8
    public double degreeOfSummarizerCardinality(Summary summary) {
        double result = 1;
        int n = summary.getSummarizers().size();
        double nominator;
        // szerokość przestrzeni rozważań
        double denominator;
        for (int i = 0; i < n; i++) {
            // całka po funkcji przynależności sumaryzatora
            nominator = summary.getSummarizers().get(i).getFuzzySet().getCLM();
            denominator = summary.getSummarizers().get(i).getFuzzySet().getUniverseOfDiscourse().getInterval();
            result *= nominator / denominator;
        }
        return 1 - Math.pow(result, 1 / (double) n);
    }

    // T9 - stopień nieprecyzyjności kwalifikatora
    public static double DegreeOfQualifierImprecision(Summary summary) {
        if (summary.getQualifiers() == null) {
            return 0.0;
        }
        double result = 1.0;
        int n = summary.getQualifiers().size();
        for (int i = 0; i < summary.getQualifiers().size(); i++) {
            result *= summary.getQualifiers().get(i).getFuzzySet().getDegreeOfFuzziness();
        }
        return 1 - Math.pow(result, 1 / (double) n);
    }

    // T10
    public double degreeOfQualifierCardinality(Summary summary) {
        if (summary.getQualifiers() == null) {
            return 0.0;
        }
        double result = 1;
        int n = summary.getQualifiers().size();
        double nominator;
        // liczba wszystkich elementów w bazie
        double denominator;
        for (int i = 0; i < n; i++) {
            nominator = summary.getQualifiers().get(i).getFuzzySet().getCLM();
            denominator = summary.getQualifiers().get(i).getFuzzySet().getUniverseOfDiscourse().getInterval();
            result *= nominator / denominator;
        }
        return 1 - Math.pow(result, 1 / (double) n);
    }

    // T11 - długość kwalifikatora
    public static double LengthOfQualifier(Summary summary) {
        if (summary.getQualifiers() == null) {
            return 1.0;
        }
        int qualifierSize = summary.getQualifiers().size();
        return 2 * Math.pow(1.0 / 2.0, qualifierSize);
    }

    // miara jakości podsumowania optymalnego
    public double optimalSummary(Summary summary, List<Double> weights) {
        List<Double> measures = new ArrayList<>(Arrays.asList(
                degreeOfTruth(summary),
                degreeOfImprecision(summary),
                degreeOfCovering(summary),
                degreeOfAppropriateness(summary),
                LengthOfSummary(summary),
                degreeOfQuantifierImprecision(summary),
                DegreeOfQuantifierCardinality(summary),
                degreeOfSummarizerCardinality(summary),
                DegreeOfQualifierImprecision(summary),
                degreeOfQualifierCardinality(summary),
                LengthOfQualifier(summary)
        ));
        double T = 0.0;
        for (int i = 0; i < measures.size(); i++) {
            T += weights.get(i) * measures.get(i);
        }
        return T;
    }

    public double degreeOfTruthFormOne(Summary summary) {
        double sigmaCountFirstSubject = 0;
        double sigmaCountSecondSubject = 0;
        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
        int sizeOfFirstSubjectDomain = firstSubject.size();
        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
        int sizeOfSecondSubjectDomain = secondSubject.size();
        for (int i = 0; i < sizeOfFirstSubjectDomain; i++) {
            sigmaCountFirstSubject += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i));
        }
        for (int i = 0; i < sizeOfSecondSubjectDomain; i++) {
            sigmaCountSecondSubject += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i));
        }
        double nominator = sigmaCountFirstSubject / sizeOfFirstSubjectDomain;
        double denominator = nominator + sigmaCountSecondSubject / sizeOfSecondSubjectDomain;

        Quantifier quantifier = summary.getQuantifier();
        if (quantifier.isAbsolute()) {
            return -1;
        } else {
            return quantifier.countDegreeOfMembership(nominator / denominator);
        }
    }

//    public double degreeOfTruthFormTwo(Summary summary) {
//        double s1p1s2p1 = 0;
//        double s2p1 = 0;
//        double s1p2 = 0;
//        // price
//        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
//        int sizeOfFirstSubjectDomain = firstSubject.size();
//        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
//        int sizeOfSecondSubjectDomain = secondSubject.size();
//
//        // sqft living
//        List<Double> firstSubject2 = summary.getSubjects().get(2).getDomain();
//        List<Double> secondSubject2 = summary.getSubjects().get(3).getDomain();
//
//
//        List<Double> listOfMembershipFunctionValues = new ArrayList<>();
//        double membershipFunctionValue = 0;
//        for (int i = 0; i < sizeOfFirstSubjectDomain; i++) {
//            // sigma count s1p1 iloczyn s2p1
//            listOfMembershipFunctionValues.add(summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i)));
//            listOfMembershipFunctionValues.add(summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(firstSubject2.get(i)));
//            Collections.sort(listOfMembershipFunctionValues);
//            membershipFunctionValue = listOfMembershipFunctionValues.get(0);
//            listOfMembershipFunctionValues.clear();
//            s1p1s2p1 += membershipFunctionValue;
//
//            // sigma count s2p1
//            s2p1 += summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(firstSubject2.get(i));
//        }
//
//        for(int i = 0; i< sizeOfSecondSubjectDomain; i++){
//            // sigma count s1p2
//            s1p2 += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i));
//        }
//
//        double nominator = s1p1s2p1 / sizeOfFirstSubjectDomain;
//        double denominator = s2p1 / sizeOfFirstSubjectDomain + s1p2 / sizeOfSecondSubjectDomain;
//
//        Quantifier quantifier = summary.getQuantifier();
//        if (quantifier.isAbsolute()) {
//            return -1;
//        } else {
//            return quantifier.countDegreeOfMembership(nominator / denominator);
//        }
//    }

    public double degreeOfTruthFormTwo(Summary summary) {
        double s1p2s2p2 = 0;
        double s1p1 = 0;
        // W
        double s2p2 = 0;
        // price
        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
        int sizeOfFirstSubjectDomain = firstSubject.size();
        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
        int sizeOfSecondSubjectDomain = secondSubject.size();

        // sqft living
        List<Double> firstSubject2 = summary.getSubjects().get(2).getDomain();
        List<Double> secondSubject2 = summary.getSubjects().get(3).getDomain();


        List<Double> listOfMembershipFunctionValues = new ArrayList<>();
        double membershipFunctionValue = 0;
        for (int i = 0; i < sizeOfSecondSubjectDomain; i++) {
            // sigma count s1p1 iloczyn s2p1
            listOfMembershipFunctionValues.add(summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i)));
            listOfMembershipFunctionValues.add(summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(secondSubject2.get(i)));
            Collections.sort(listOfMembershipFunctionValues);
            membershipFunctionValue = listOfMembershipFunctionValues.get(0);
            listOfMembershipFunctionValues.clear();
            s1p2s2p2 += membershipFunctionValue;
        }

        for(int i = 0; i< sizeOfFirstSubjectDomain; i++){
            // sigma count s1p2
            s1p1 += summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(firstSubject2.get(i));
        }

        double nominator = s1p1 / sizeOfFirstSubjectDomain;
        double denominator = s1p1 / sizeOfFirstSubjectDomain + s1p2s2p2 / sizeOfSecondSubjectDomain;

        Quantifier quantifier = summary.getQuantifier();
        if (quantifier.isAbsolute()) {
            return -1;
        } else {
            return quantifier.countDegreeOfMembership(nominator / denominator);
        }
    }

//    public double degreeOfTruthFormThree(Summary summary) {
//        double s1p1s2p1 = 0;
//        double s1p1 = 0;
//        double s1p2 = 0;
//        // price
//        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
//        int sizeOfFirstSubjectDomain = firstSubject.size();
//        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
//        int sizeOfSecondSubjectDomain = secondSubject.size();
//
//        // sqft living
//        List<Double> firstSubject2 = summary.getSubjects().get(2).getDomain();
//        List<Double> secondSubject2 = summary.getSubjects().get(3).getDomain();
//
//
//        List<Double> listOfMembershipFunctionValues = new ArrayList<>();
//        double membershipFunctionValue = 0;
//        for (int i = 0; i < sizeOfFirstSubjectDomain; i++) {
////                for (LinguisticVariableValue summarizer : summary.getSummarizers()) {
////                    record = summarizer.getFuzzySet().getUniverseOfDiscourse().getDomain().get(i);
////                    listOfMembershipFunctionValues.add(summarizer.getFuzzySet().countDegreeOfMembership(record));
////                }
//
//            // sigma count s1p1 iloczyn s2p1
//            listOfMembershipFunctionValues.add(summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i)));
//            listOfMembershipFunctionValues.add(summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(firstSubject2.get(i)));
//            Collections.sort(listOfMembershipFunctionValues);
//            membershipFunctionValue = listOfMembershipFunctionValues.get(0);
//            listOfMembershipFunctionValues.clear();
//            s1p1s2p1 += membershipFunctionValue;
//
//            // sigma count s1p1
//            s1p1 += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i));
//
//        }
//
//        for(int i = 0; i< sizeOfSecondSubjectDomain; i++){
//            // sigma count s1p2
//            s1p2 += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i));
//        }
//
//        double nominator = s1p1s2p1 / sizeOfFirstSubjectDomain;
//        double denominator = s1p1 / sizeOfFirstSubjectDomain + s1p2 / sizeOfSecondSubjectDomain;
//
//        Quantifier quantifier = summary.getQuantifier();
//        if (quantifier.isAbsolute()) {
//            return -1;
//        } else {
//            return quantifier.countDegreeOfMembership(nominator / denominator);
//        }
//    }

    public double degreeOfTruthFormThree(Summary summary) {
        double s1p1s2p1 = 0;
        double s1p2 = 0;
        // price
        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
        int sizeOfFirstSubjectDomain = firstSubject.size();
        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
        int sizeOfSecondSubjectDomain = secondSubject.size();

        // sqft living
        List<Double> firstSubject2 = summary.getSubjects().get(2).getDomain();
        List<Double> secondSubject2 = summary.getSubjects().get(3).getDomain();


        List<Double> listOfMembershipFunctionValues = new ArrayList<>();
        double membershipFunctionValue = 0;
        for (int i = 0; i < sizeOfFirstSubjectDomain; i++) {
            // sigma count s1p1 iloczyn s2p1
            listOfMembershipFunctionValues.add(summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i)));
            listOfMembershipFunctionValues.add(summary.getSummarizers().get(1).getFuzzySet().countDegreeOfMembership(firstSubject2.get(i)));
            Collections.sort(listOfMembershipFunctionValues);
            membershipFunctionValue = listOfMembershipFunctionValues.get(0);
            listOfMembershipFunctionValues.clear();
            s1p1s2p1 += membershipFunctionValue;
        }

        for(int i = 0; i< sizeOfSecondSubjectDomain; i++){
            // sigma count s1p2
            s1p2 += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i));
        }

        double nominator = s1p1s2p1 / sizeOfFirstSubjectDomain;
        double denominator = s1p1s2p1 / sizeOfFirstSubjectDomain + s1p2 / sizeOfSecondSubjectDomain;

        Quantifier quantifier = summary.getQuantifier();
        if (quantifier.isAbsolute()) {
            return -1;
        } else {
            return quantifier.countDegreeOfMembership(nominator / denominator);
        }
    }


    // normalnie jest I(a,b) ale z racji tego, że zawsze jedno z (a,b) jest 0 to wystarczy jeden argument
    public double implicationOfLukasiewicz(double a, double b){
        double tmp = 1 - a + b;
        if (tmp > 1){
            return 1;
        } else{
            return tmp;
        }
    }

    public double implicationOfReichenbach(double a, double b){
        return 1 - a + (a * b);
    }

    public double degreeOfTruthFormFour(Summary summary) {
//        double sigmaCountFirstSubject = 0;
//        double sigmaCountSecondSubject = 0;
//        List<Double> firstSubject = summary.getSubjects().get(0).getDomain();
//        int sizeOfFirstSubjectDomain = firstSubject.size();
//        List<Double> secondSubject = summary.getSubjects().get(1).getDomain();
//        int sizeOfSecondSubjectDomain = secondSubject.size();
//        for (int i = 0; i < sizeOfFirstSubjectDomain; i++) {
//            sigmaCountFirstSubject += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(firstSubject.get(i));
//        }
//        for (int i = 0; i < sizeOfSecondSubjectDomain; i++) {
//            sigmaCountSecondSubject += summary.getSummarizers().get(0).getFuzzySet().countDegreeOfMembership(secondSubject.get(i));
//        }
//        double nominator = sigmaCountFirstSubject / sizeOfFirstSubjectDomain;
//        double denominator = nominator + sigmaCountSecondSubject / sizeOfSecondSubjectDomain;
//
//        return nominator / denominator;

        List<Double> P1 = summary.getSubjects().get(0).getDomain();
        List<Double> P2 = summary.getSubjects().get(1).getDomain();

        double size = P1.size() + P2.size();
//        System.out.println("Size: " + size);

        LinguisticVariableValue summarizer = summary.getSummarizers().get(0);
        double sigmaCountOfInclusion = 0;

        for (Double value : P1){
            var tmp = implicationOfLukasiewicz(0, summarizer.getFuzzySet().countDegreeOfMembership(value));
            sigmaCountOfInclusion += tmp;
        }
        for (Double value : P2){
            var tmp = implicationOfLukasiewicz(summarizer.getFuzzySet().countDegreeOfMembership(value), 0);
            sigmaCountOfInclusion += tmp;
        }
        return 1 - (sigmaCountOfInclusion / size);
    }
}