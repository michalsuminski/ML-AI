package org.example.logic;

import org.example.fuzzy.LinguisticVariable;
import org.example.fuzzy.LinguisticVariableValue;
import org.example.fuzzy.Quantifier;

import java.util.ArrayList;
import java.util.List;

public class Initializer {

    private DatabaseQueries databaseQueries = new DatabaseQueries();

    private Generator generator = new Generator();

    public List<LinguisticVariable> initializeLingusiticVariable() {

        // DATA SPRZEDAZY

        LinguisticVariableValue old = generator.generateLinguisticVariableValue("old sale date", "half-triangular", 20150101, 20150301, 0, 0, false, databaseQueries.selectDateOfSale(""), 20140501, 20150601, true);

        LinguisticVariableValue new_sale_date = generator.generateLinguisticVariableValue("new sale date", "half-triangular", 20150101, 20150301, 0, 0, true, databaseQueries.selectDateOfSale(""), 20140501, 20150601, true);

        List<LinguisticVariableValue> listDateOfSale = List.of(old, new_sale_date);

        LinguisticVariable dateOfSale = new LinguisticVariable("dateofsale", listDateOfSale);

        // CENA
        LinguisticVariableValue occasional = generator.generateLinguisticVariableValue("occasional price", "half-triangular", 75000, 100000, 1, 0, false, databaseQueries.selectPrice(""), 0, 1300000, true);

        LinguisticVariableValue cheap = generator.generateLinguisticVariableValue("cheap price", "trapezoid", 75000, 125000, 200000, 250000, false, databaseQueries.selectPrice(""), 0, 1300000, true);

        LinguisticVariableValue normal = generator.generateLinguisticVariableValue("normal price", "trapezoid", 200000, 250000, 400000, 450000, false, databaseQueries.selectPrice(""), 0, 1300000, true);

        LinguisticVariableValue expensive = generator.generateLinguisticVariableValue("expensive price", "trapezoid", 400000, 450000, 1000000, 1200000, false, databaseQueries.selectPrice(""), 0, 1300000, true);

        LinguisticVariableValue luxury = generator.generateLinguisticVariableValue("luxury price", "half-triangular", 1000000, 1200000, 0, 0, true, databaseQueries.selectPrice(""), 0, 1300000, true);

        List<LinguisticVariableValue> listPrice = List.of(occasional, cheap, normal, expensive, luxury);

        LinguisticVariable price = new LinguisticVariable("price", listPrice);

        // WIELKOSC DOMU
        LinguisticVariableValue verysmall = generator.generateLinguisticVariableValue("very small living space", "half-triangular", 500, 800, 1, 0, false, databaseQueries.selectSqftLiving(""), 0, 15000, true);

        LinguisticVariableValue small = generator.generateLinguisticVariableValue("small living space", "trapezoid", 500, 800, 2000, 2500, false, databaseQueries.selectSqftLiving(""), 0, 15000, true);

        LinguisticVariableValue large = generator.generateLinguisticVariableValue("large living space", "trapezoid", 2000, 2500, 5000, 7000, false, databaseQueries.selectSqftLiving(""), 0, 15000, true);

        LinguisticVariableValue huge = generator.generateLinguisticVariableValue("huge living space", "half-triangular", 5000, 7000, 0, 0, true, databaseQueries.selectSqftLiving(""), 0, 15000, true);

        List<LinguisticVariableValue> listLivingSpace = List.of(verysmall, small, large, huge);

        LinguisticVariable livingSpace = new LinguisticVariable("livingspace", listLivingSpace);

        // WIELKOSC DZIALKI
        LinguisticVariableValue verysmallLot = generator.generateLinguisticVariableValue("very small lot", "half-triangular", 1000, 5000, 1, 0, false, databaseQueries.selectSqftLot(""), 0, 100000, true);

        LinguisticVariableValue smallLot = generator.generateLinguisticVariableValue("small lot", "trapezoid", 1000, 5000, 10000, 15000, false, databaseQueries.selectSqftLot(""), 0, 100000, true);

        LinguisticVariableValue largeLot = generator.generateLinguisticVariableValue("large lot", "trapezoid", 10000, 15000, 50000, 75000, false, databaseQueries.selectSqftLot(""), 0, 100000, true);

        LinguisticVariableValue hugeLot = generator.generateLinguisticVariableValue("huge lot", "half-triangular", 50000, 75000, 0, 0, true, databaseQueries.selectSqftLot(""), 0, 100000, true);

        List<LinguisticVariableValue> listLot = List.of(verysmallLot, smallLot, largeLot, hugeLot);

        LinguisticVariable lot = new LinguisticVariable("lot", listLot);

        // WIELKOSC PIWNICY
        LinguisticVariableValue very_small_basement = generator.generateLinguisticVariableValue("very small basement", "half-triangular", 10, 50, 0, 0, false, databaseQueries.selectSqftBasement(""), 0, 1000, true);
        LinguisticVariableValue small_basement = generator.generateLinguisticVariableValue("small basement", "trapezoid", 10, 50, 150, 250, false, databaseQueries.selectSqftBasement(""), 0, 1000, true);
        LinguisticVariableValue large_basement = generator.generateLinguisticVariableValue("large basement", "trapezoid", 150, 250, 500, 750, false, databaseQueries.selectSqftBasement(""), 0, 1000, true);
        LinguisticVariableValue huge_basement = generator.generateLinguisticVariableValue("huge basement", "half-triangular", 500, 750, 0, 0, true, databaseQueries.selectSqftBasement(""), 0, 1000, true);
        List<LinguisticVariableValue> list_basement_space = List.of(very_small_basement, small_basement, large_basement, huge_basement);
        LinguisticVariable basementSpace = new LinguisticVariable("basement_space", list_basement_space);
        // DATA WYBUDOWANIA
        LinguisticVariableValue young_building = generator.generateLinguisticVariableValue("young building", "half-triangular", 30, 50, 0, 0, false, databaseQueries.selectYearBuilt(""), 0, 130, true);
        LinguisticVariableValue old_building = generator.generateLinguisticVariableValue("old building", "trapezoid", 30, 50, 65, 85, false, databaseQueries.selectYearBuilt(""), 0, 130, true);
        LinguisticVariableValue very_old_building = generator.generateLinguisticVariableValue("very old building", "half-triangular", 65, 85, 0, 0, true, databaseQueries.selectYearBuilt(""), 0, 130, true);
        List<LinguisticVariableValue> building_age_list = List.of(young_building, old_building, very_old_building);
        LinguisticVariable building_age = new LinguisticVariable("building age", building_age_list);
        // DATA RENOWACJI
        LinguisticVariableValue never_renovation = generator.generateLinguisticVariableValue("never renovated", "half-triangular", 0, 0, 0, 0, false, databaseQueries.selectYearRenovated(""), 1934, 2015, true);
        LinguisticVariableValue recently_renovation = generator.generateLinguisticVariableValue("recently renovated", "half-triangular", 1992, 2012, 0, 0, false, databaseQueries.selectYearRenovated(""), 1934, 2015, true);
        LinguisticVariableValue long_ago_renovation = generator.generateLinguisticVariableValue("long ago renovated", "half-triangular", 1992, 2012, 0, 0, true, databaseQueries.selectYearRenovated(""), 1934, 2015, true);
        List<LinguisticVariableValue> renovation_date_list = List.of(never_renovation, recently_renovation, long_ago_renovation);
        LinguisticVariable renovation_date = new LinguisticVariable("renovation date", renovation_date_list);

        return List.of(dateOfSale, price, livingSpace, lot, basementSpace, building_age, renovation_date);
    }

    public List<Quantifier> initializeQuantifierVariable(int lessThan1, int lessThan2, int moreThan1, int moreThan2, int about1) {
        int databaseSize = databaseQueries.selectAll().size();
        List<Double> absoluteQuantifierDomain = new ArrayList<>();
        List<Double> relativeQuantifierDomain = new ArrayList<>();
        for (int i = 0; i < databaseSize + 1; i++) {
            absoluteQuantifierDomain.add((double) i);
            relativeQuantifierDomain.add((double) i / databaseSize);
        }
        // ALMOST NONE
        var almostNone = generator.generateQuantifier("Almost none", "half-triangular", false, 0, 0.2, 1, 0, false, relativeQuantifierDomain, 0, 1 ,true);

        // ALMOST ALL
        var almostAll = generator.generateQuantifier("Almost all", "half-triangular", false, 0.8, 1, 0, 0, true, relativeQuantifierDomain, 0, 1 ,true);

        // ABOUT HALF
        var aboutHalf = generator.generateQuantifier("About half", "triangular", false, 0.3, 0.5, 0.7, 0, false, relativeQuantifierDomain, 0, 1 ,true);

        // MUCH MORE THAN 1/4
        var muchMoreThan_1_4 = generator.generateQuantifier("Much more than 1/4", "half-triangular", false, 0.3, 0.8, 0, 0, true, relativeQuantifierDomain, 0, 1 ,true);

        var much_less_than_3_4 = generator.generateQuantifier("Much less than 3/4", "half-triangular", false, 0.2, 0.7, 0, 0, false, relativeQuantifierDomain, 0, 1 ,true);
        var less_than_1 = generator.generateQuantifier("Less than " + lessThan1, "half-triangular", true, lessThan1 - 0.1 * (double) databaseSize, lessThan1, 0, 0, false, absoluteQuantifierDomain, 0, 21613, false);
        var less_than_2 = generator.generateQuantifier("Less than " + lessThan2, "half-triangular", true, lessThan2 - 0.1 * (double) databaseSize, lessThan2, 0, 0, false, absoluteQuantifierDomain, 0, 21613, false);
        var more_than_1 = generator.generateQuantifier("More than " + moreThan1, "half-triangular", true, moreThan1, moreThan1 + 0.1 * (double) databaseSize, 0, 0, true, absoluteQuantifierDomain, 0, 21613, false);
        var more_than_2 = generator.generateQuantifier("More than " + moreThan2, "half-triangular", true, moreThan2, moreThan2 + 0.1 * (double) databaseSize, 0, 0, true, absoluteQuantifierDomain, 0, 21613, false);
        var about_1 = generator.generateQuantifier("About " + about1, "triangular", true, about1 - 0.1 * (double) databaseSize, about1, about1 + 0.1 * (double) databaseSize, 0, false, absoluteQuantifierDomain, 0, 21613, false);
        return List.of(almostNone, almostAll, aboutHalf, muchMoreThan_1_4, much_less_than_3_4, less_than_1, less_than_2, more_than_1, more_than_2, about_1);
    }
}
