package org.example.fuzzy;

import java.util.List;

public class Summary {
    private Quantifier quantifier;

    List<LinguisticVariableValue> qualifiers;

    List<LinguisticVariableValue> summarizers;

    List<ClassicSet> subjects;


    public Summary(Quantifier quantifier, List<LinguisticVariableValue> qualifiers, List<LinguisticVariableValue> summarizers, List<ClassicSet> subjects) {
        this.quantifier = quantifier;
        this.qualifiers = qualifiers;
        this.summarizers = summarizers;
        this.subjects = subjects;
    }

    public List<ClassicSet> getSubjects() {
        return subjects;
    }

    public Quantifier getQuantifier() {
        return quantifier;
    }

    public List<LinguisticVariableValue> getQualifiers() {
        return qualifiers;
    }

    public List<LinguisticVariableValue> getSummarizers() {
        return summarizers;
    }
}
