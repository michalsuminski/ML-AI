package org.example;

import org.example.fuzzy.*;
import org.example.logic.DatabaseQueries;
import org.example.logic.Initializer;
import org.example.logic.QualityMeasures;

import java.util.List;

public class App {

    public static void main(String[] args) {
        DatabaseQueries databaseQueries = new DatabaseQueries();
        Initializer initializer = new Initializer();
        QualityMeasures qualityMeasures = new QualityMeasures();

        // stworzenie naszych zmiennych lingwistycznych
        List<LinguisticVariable> linguisticVariables = initializer.initializeLingusiticVariable();

        //stworzenie naszych kwantyfikatorów
        List<Quantifier> quantifiers = initializer.initializeQuantifierVariable(10000, 7500, 7500, 5550, 10000);

        // podanie kwantyfikatora
        Quantifier almostNone = quantifiers.get(0);
        Quantifier almostAll = quantifiers.get(1);
        Quantifier aboutHalf = quantifiers.get(2);
        Quantifier muchMoreThan_1_4 = quantifiers.get(3);
        Quantifier much_less_than_3_4 = quantifiers.get(4);
        Quantifier less_than_1 = quantifiers.get(5);
        Quantifier less_than_2 = quantifiers.get(6);
        Quantifier more_than_1 = quantifiers.get(7);
        Quantifier more_than_2 = quantifiers.get(8);
        Quantifier about_1 = quantifiers.get(9);
        // podanie sumaryzatora
        // expensive price
        LinguisticVariableValue summarizer = linguisticVariables.get(1).getLinguisticVariableList().get(3);
        // small living space
        LinguisticVariableValue summarizer2 = linguisticVariables.get(2).getLinguisticVariableList().get(1);
        // large basement
        LinguisticVariableValue summarizer3 = linguisticVariables.get(4).getLinguisticVariableList().get(2);
        // recently rennovated
        LinguisticVariableValue summarizer4 = linguisticVariables.get(6).getLinguisticVariableList().get(1);
        // long ago rennovated
        LinguisticVariableValue summarizer5 = linguisticVariables.get(6).getLinguisticVariableList().get(2);
        // occasional price
        LinguisticVariableValue summarizer6 = linguisticVariables.get(1).getLinguisticVariableList().get(0);
        // huge living space
        LinguisticVariableValue summarizer7 = linguisticVariables.get(2).getLinguisticVariableList().get(3);
        // huge lot
        LinguisticVariableValue summarizer8 = linguisticVariables.get(3).getLinguisticVariableList().get(3);
        // very small lot
        LinguisticVariableValue summarizer9 = linguisticVariables.get(3).getLinguisticVariableList().get(0);
        // luxury price
        LinguisticVariableValue summarizer10 = linguisticVariables.get(1).getLinguisticVariableList().get(4);
        // small basement
        LinguisticVariableValue summarizer11 = linguisticVariables.get(4).getLinguisticVariableList().get(1);
        // large lot
        LinguisticVariableValue summarizer12 = linguisticVariables.get(3).getLinguisticVariableList().get(2);
        // normal price
        LinguisticVariableValue summarizer13 = linguisticVariables.get(1).getLinguisticVariableList().get(2);
        // very small basement
        LinguisticVariableValue summarizer14 = linguisticVariables.get(4).getLinguisticVariableList().get(0);
        // recently rennovated
        LinguisticVariableValue summarizer15 = linguisticVariables.get(6).getLinguisticVariableList().get(1);
        // huge basement
        LinguisticVariableValue summarizer16 = linguisticVariables.get(4).getLinguisticVariableList().get(2);
        // small basement
        LinguisticVariableValue summarizer17 = linguisticVariables.get(4).getLinguisticVariableList().get(1);
        // cheap price
        LinguisticVariableValue summarizer18 = linguisticVariables.get(1).getLinguisticVariableList().get(1);
        // very small living space
        LinguisticVariableValue summarizer19 = linguisticVariables.get(2).getLinguisticVariableList().get(0);

        List<Double> parter =databaseQueries.selectPrice(" where floors==1");

        List<Double> twostorey =databaseQueries.selectPrice(" where floors==2");
//        System.out.println(parter.size());

        List<Double> storey =databaseQueries.selectPrice(" where floors <> 1 and floors <> 2");
//        System.out.println(storey.size());

        List<Double> parter2 =databaseQueries.selectSqftLiving(" where floors==1");
//        System.out.println(parter.size());

        List<Double> twostorey2 =databaseQueries.selectSqftLiving(" where floors==2");

        List<Double> storey2 = databaseQueries.selectSqftLiving(" where floors <> 1 and floors <> 2");

        List<Double> all = databaseQueries.selectSqftLiving(" where floors <> 1");

        List<Double> all2 = databaseQueries.selectSqftLiving(" where floors <> 1");

        // FORMA PIERWSZA
        // prawie wszytskie domy pietrowe wn stosunku do parterowych sa cheap
//        Summary s1 = new Summary(almostAll, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//        Summary s2 = new Summary(almostNone, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey)));
//        Summary s3 = new Summary(aboutHalf, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey)));
//        Summary s4 = new Summary(muchMoreThan_1_4, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey)));
//        Summary s5 = new Summary(much_less_than_3_4, null, List.of(summarizer13), List.of(new ClassicSet(parter), new ClassicSet(storey)));

//        System.out.println(qualityMeasures.degreeOfTruthFormOne(s1));
//        System.out.println(qualityMeasures.degreeOfTruthFormOne(s2));
//        System.out.println(qualityMeasures.degreeOfTruthFormOne(s3));
//        System.out.println(qualityMeasures.degreeOfTruthFormOne(s4));
//        System.out.println(qualityMeasures.degreeOfTruthFormOne(s5));

        // FORMA DRUGA
        // prawie wszystkie domy parterowe w odniesieniu do domów pietrowych majacych huge living space jest cheap
        Summary s1 = new Summary(almostAll, null, List.of(summarizer, summarizer2), List.of(new ClassicSet(twostorey, 0, 0, false), new ClassicSet(parter, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false), new ClassicSet(parter2, 0, 0, false)));
        Summary s2 = new Summary(almostNone, null, List.of(summarizer, summarizer2), List.of(new ClassicSet(twostorey, 0, 0, false), new ClassicSet(parter, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false), new ClassicSet(parter2, 0, 0, false)));
        Summary s3 = new Summary(aboutHalf, null, List.of(summarizer, summarizer2), List.of(new ClassicSet(twostorey, 0, 0, false), new ClassicSet(parter, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false), new ClassicSet(parter2, 0, 0, false)));
        Summary s4 = new Summary(muchMoreThan_1_4, null, List.of(summarizer, summarizer2), List.of(new ClassicSet(twostorey, 0, 0, false), new ClassicSet(parter, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false), new ClassicSet(parter2, 0, 0, false)));
        Summary s5 = new Summary(much_less_than_3_4, null, List.of(summarizer, summarizer2), List.of(new ClassicSet(twostorey, 0, 0, false), new ClassicSet(parter, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false), new ClassicSet(twostorey2, 0, 0, false)));

        System.out.println(qualityMeasures.degreeOfTruthFormTwo(s1));
        System.out.println(qualityMeasures.degreeOfTruthFormTwo(s2));
        System.out.println(qualityMeasures.degreeOfTruthFormTwo(s3));
        System.out.println(qualityMeasures.degreeOfTruthFormTwo(s4));
        System.out.println(qualityMeasures.degreeOfTruthFormTwo(s5));

        // FORMA TRZECIA
        // prawie wszystkie domy parterowe w odniesieniu do domów pietrowych majacych huge living space jest cheap
//        Summary s1 = new Summary(almostAll, null, List.of(summarizer13, summarizer7), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//        Summary s2 = new Summary(almostNone, null, List.of(summarizer13, summarizer7), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//        Summary s3 = new Summary(aboutHalf, null, List.of(summarizer13, summarizer7), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//        Summary s4 = new Summary(muchMoreThan_1_4, null, List.of(summarizer13, summarizer7), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//        Summary s5 = new Summary(much_less_than_3_4, null, List.of(summarizer13, summarizer7), List.of(new ClassicSet(parter), new ClassicSet(storey), new ClassicSet(parter2), new ClassicSet(storey2)));
//
//        System.out.println(qualityMeasures.degreeOfTruthFormThree(s1));
//        System.out.println(qualityMeasures.degreeOfTruthFormThree(s2));
//        System.out.println(qualityMeasures.degreeOfTruthFormThree(s3));
//        System.out.println(qualityMeasures.degreeOfTruthFormThree(s4));
//        System.out.println(qualityMeasures.degreeOfTruthFormThree(s5));

        // FORMA CZWARTA
        // wiecej domow parterowych niz pietrowych jest
//        Summary s1 = new Summary(almostAll, null, List.of(summarizer13), parter); // expensive
//        Summary s2 = new Summary(almostNone, null, List.of(summarizer18), parter); // cheap
//        Summary s3 = new Summary(aboutHalf, null, List.of(summarizer), parter); // expensive
//        Summary s4 = new Summary(muchMoreThan_1_4, null, List.of(summarizer10), parter); // luxury
//        Summary s5 = new Summary(much_less_than_3_4, null, List.of(summarizer6), parter); // occasional
//
//        System.out.println(qualityMeasures.degreeOfTruthFormFour(s1));
//        System.out.println(qualityMeasures.degreeOfTruthFormFour(s2));
//        System.out.println(qualityMeasures.degreeOfTruthFormFour(s3));
//        System.out.println(qualityMeasures.degreeOfTruthFormFour(s4));
//        System.out.println(qualityMeasures.degreeOfTruthFormFour(s5));


//        Summary s1 = new Summary(less_than_1, null, List.of(summarizer12), null);
//        Summary s2 = new Summary(about_1, null, List.of(summarizer, summarizer2), null);
//
//
//        System.out.print(" & " + qualityMeasures.degreeOfTruth(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfImprecision(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfCovering(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfAppropriateness(s1));
//
//        System.out.print(" & " + qualityMeasures.LengthOfSummary(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfQuantifierImprecision(s1));
//
//        System.out.print(" & " + qualityMeasures.DegreeOfQuantifierCardinality(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfSummarizerCardinality(s1));
//
//        System.out.print(" & " + qualityMeasures.DegreeOfQualifierImprecision(s1));
//
//        System.out.print(" & " + qualityMeasures.degreeOfQualifierCardinality(s1));
//
//        System.out.print(" & " + qualityMeasures.LengthOfQualifier(s1));
//
//
//        System.out.print(" & " + qualityMeasures.optimalSummary(s1, List.of(0.2, 0.2, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0, 0.0)));
//
//
//        System.out.println(" DWOJKAAAAAAAAAAAA");
//
//        System.out.print(" & " + qualityMeasures.degreeOfTruth(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfImprecision(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfCovering(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfAppropriateness(s2));
//
//        System.out.print(" & " + qualityMeasures.LengthOfSummary(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfQuantifierImprecision(s2));
//
//        System.out.print(" & " + qualityMeasures.DegreeOfQuantifierCardinality(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfSummarizerCardinality(s2));
//
//        System.out.print(" & " + qualityMeasures.DegreeOfQualifierImprecision(s2));
//
//        System.out.print(" & " + qualityMeasures.degreeOfQualifierCardinality(s2));
//
//        System.out.print(" & " + qualityMeasures.LengthOfQualifier(s2));
//
//
//        System.out.print(" & " + qualityMeasures.optimalSummary(s2, List.of(0.2, 0.2, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0, 0.0)));


    }
}
