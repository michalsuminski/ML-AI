//package org.jpms;
//
//import org.jpms.metrics.QualityMeasures;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//
//public class QualityMeasuresTest {
//
//    ArrayList<String> realValue = new ArrayList<String>();
//    ArrayList<String> valueFromClassifier = new ArrayList<String>();
//    QualityMeasures q = new QualityMeasures();
//
//    @Before
//    public void setUp() throws Exception {
//        int i;
//        // lacznie jest 30 dokumentow
//        for (i = 0; i < 9; i++) {
//            realValue.add("usa");
//        }
//        for (i = 0; i < 3; i++) {
//            realValue.add("west-germany");
//        }
//        for (i = 0; i < 5; i++) {
//            realValue.add("uk");
//        }
//        for (i = 0; i < 4; i++) {
//            realValue.add("canada");
//        }
//        for (i = 0; i < 3; i++) {
//            realValue.add("japan");
//        }
//        for (i = 0; i < 6; i++) {
//            realValue.add("france");
//        }
//        // powinno byc usa
//        for (i = 0; i < 9; i++) {
//            if (i < 5) {
//                valueFromClassifier.add("usa");
//            }
//            if (i == 5) { valueFromClassifier.add("japan");}
//            if (i == 6) { valueFromClassifier.add("france");}
//            if (i == 7) { valueFromClassifier.add("canada");}
//            if (i == 8) { valueFromClassifier.add("canada");}
//        }
//        // powinno byc west-germany
//        for (i = 0; i < 3; i++) {
//            if (i < 2) {
//                valueFromClassifier.add("west-germany");
//            }
//            if (i == 2) { valueFromClassifier.add("france");}
//        }
//        // powinno byc uk
//        for (i = 0; i < 5; i++) {
//            if (i < 4) {
//                valueFromClassifier.add("uk");
//            }
//            if (i == 4) { valueFromClassifier.add("france");}
//        }
//        // powinno byc canada
//        for (i = 0; i < 4; i++) {
//            if (i < 3) {
//                valueFromClassifier.add("canada");
//            }
//            if (i == 3) { valueFromClassifier.add("usa");}
//        }
//        // powinno byc japan
//        for (i = 0; i < 3; i++) {
//            if (i < 2) {
//                valueFromClassifier.add("japan");
//            }
//            if (i == 2) { valueFromClassifier.add("west-germany");}
//        }
//        // powinno byc france
//        for (i = 0; i < 6; i++) {
//            if (i < 4) {
//                valueFromClassifier.add("france");
//            }
//            if (i == 4) { valueFromClassifier.add("usa");}
//            if (i == 5) { valueFromClassifier.add("west-germany");}
//        }
//    }
//
//    @Test
//    public void countPrecisionForOneClassTest(){
//        assertEquals(0.71, q.countPrecisionForOneClass(realValue, valueFromClassifier, "usa"), 0.01);
//        assertEquals(0.50, q.countPrecisionForOneClass(realValue, valueFromClassifier, "west-germany"), 0.01);
//        assertEquals(1.00, q.countPrecisionForOneClass(realValue, valueFromClassifier, "uk"), 0.01);
//        assertEquals(0.67, q.countPrecisionForOneClass(realValue, valueFromClassifier, "japan"), 0.01);
//        assertEquals(0.60, q.countPrecisionForOneClass(realValue, valueFromClassifier, "canada"), 0.01);
//        assertEquals(0.57, q.countPrecisionForOneClass(realValue, valueFromClassifier, "france"), 0.01);
//    }
//
//    @Test
//    public void countPrecisionForAllTest(){
//        ArrayList<Double> precisionsOfCountries = new ArrayList<>();
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "usa"));
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "west-germany"));
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "uk"));
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "japan"));
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "canada"));
//        precisionsOfCountries.add(q.countPrecisionForOneClass(realValue, valueFromClassifier, "france"));
//        assertEquals(0.71, q.countPrecisionForAll(precisionsOfCountries), 0.01);
//    }
//
//    @Test
//    public void countRecallForOneClassTest(){
//        assertEquals(0.56, q.countRecallForOneClass(realValue, valueFromClassifier, "usa"), 0.01);
//        assertEquals(0.67, q.countRecallForOneClass(realValue, valueFromClassifier, "west-germany"), 0.01);
//        assertEquals(0.80, q.countRecallForOneClass(realValue, valueFromClassifier, "uk"), 0.01);
//        assertEquals(0.67, q.countRecallForOneClass(realValue, valueFromClassifier, "japan"), 0.01);
//        assertEquals(0.75, q.countRecallForOneClass(realValue, valueFromClassifier, "canada"), 0.01);
//        assertEquals(0.67, q.countRecallForOneClass(realValue, valueFromClassifier, "france"), 0.01);
//    }
//
//    @Test
//    public void countRecallForAllTest(){
//        ArrayList<Double> recallOfCountries = new ArrayList<>();
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "usa"));
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "west-germany"));
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "uk"));
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "japan"));
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "canada"));
//        recallOfCountries.add(q.countRecallForOneClass(realValue, valueFromClassifier, "france"));
//        assertEquals(0.68, q.countRecallForAll(recallOfCountries), 0.01);
//    }
//
//    @Test
//    public void countF1ForOneClassTest(){
//        assertEquals(0.63, q.countF1ForOneClass(realValue, valueFromClassifier, "usa"), 0.01);
//        assertEquals(0.57, q.countF1ForOneClass(realValue, valueFromClassifier, "west-germany"), 0.01);
//        assertEquals(0.89, q.countF1ForOneClass(realValue, valueFromClassifier, "uk"), 0.01);
//        assertEquals(0.67, q.countF1ForOneClass(realValue, valueFromClassifier, "japan"), 0.01);
//        assertEquals(0.67, q.countF1ForOneClass(realValue, valueFromClassifier, "canada"), 0.01);
//        assertEquals(0.62, q.countF1ForOneClass(realValue, valueFromClassifier, "france"), 0.01);
//    }
//
//    @Test
//    public void countF1ForAllTest(){
//        ArrayList<Double> f1OfCountries = new ArrayList<>();
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "usa"));
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "west-germany"));
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "uk"));
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "japan"));
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "canada"));
//        f1OfCountries.add(q.countF1ForOneClass(realValue, valueFromClassifier, "france"));
//        assertEquals(0.7, q.countF1ForAll(f1OfCountries), 0.01);
//    }
//}