package org.example.fuzzy;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class Quantifier {

    // name of the quantifier
    private String label;

    private FuzzySet fuzzySet;

    // if "0" -> absolute, if "1" -> relative
    private boolean isAbsolute;

    public Quantifier(String label, FuzzySet fuzzySet, boolean isAbsolute) {
        this.label = label;
        this.fuzzySet = fuzzySet;
        this.isAbsolute = isAbsolute;
    }

    // function to calculate degree of membership
    public double countDegreeOfMembership(double x) {
        return this.fuzzySet.countDegreeOfMembership(x);
    }

    // isNormal() checks if for every x in domain the degree of membership
    // is between [0,1] - requirement
    public boolean isNormal(List<Double> x) {
        throw new NotImplementedException("not implemented");
    }

    // isConvex() checks if between "r" and "s" each x is in domain
    // (called alfa-cut)
    public boolean isConvex(List<Double> x) {
        throw new NotImplementedException("not implemented");
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }

    public void setAbsolute(boolean absolute) {
        isAbsolute = absolute;
    }
}
