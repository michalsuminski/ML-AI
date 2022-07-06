package org.example.fuzzy;

import java.util.List;

public class ClassicSet {
    // lista punktów na osi reprezentująca dziedzinę, może być różnego typu, np. liczba, data
    private List<Double> domain;
    private double beginOfInterval;
    private double endOfInterval;
    private boolean isContinuous;

    public double getBeginOfInterval() {
        return beginOfInterval;
    }

    public double getEndOfInterval() {
        return endOfInterval;
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public List<Double> getDomain() {
        return domain;
    }

    public double getInterval() { return endOfInterval-beginOfInterval;}

    public ClassicSet(List<Double> domain, double beginOfInterval, double endOfInterval, boolean isContinuous) {
        this.domain = domain;
        this.beginOfInterval = beginOfInterval;
        this.endOfInterval = endOfInterval;
        this.isContinuous = isContinuous;
    }
}
