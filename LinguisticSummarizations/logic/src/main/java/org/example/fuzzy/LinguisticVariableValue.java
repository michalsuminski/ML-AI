package org.example.fuzzy;

public class LinguisticVariableValue {
    // name of the variable value
    private String name;

    private FuzzySet fuzzySet;

    public LinguisticVariableValue(String name, FuzzySet fuzzySet) {
        this.name = name;
        this.fuzzySet = fuzzySet;
    }

    public String getName() {
        return name;
    }

    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }
}
