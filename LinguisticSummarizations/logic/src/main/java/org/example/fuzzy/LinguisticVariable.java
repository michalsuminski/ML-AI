package org.example.fuzzy;

import java.util.List;

public class LinguisticVariable {
    private String name;

//    private ClassicSet universeOfDiscourse;
    // zakomentowany bo posiadamy dostęp do linguisticVaribleValue ktore maja fuzzySet, który ma universe of discourse

    private List<LinguisticVariableValue> linguisticVariableList;

    public LinguisticVariable(String name, List<LinguisticVariableValue> linguisticVariableList) {
        this.name = name;
        this.linguisticVariableList = linguisticVariableList;
    }

    public String getName() {
        return name;
    }

    public List<LinguisticVariableValue> getLinguisticVariableList() {
        return linguisticVariableList;
    }
}
